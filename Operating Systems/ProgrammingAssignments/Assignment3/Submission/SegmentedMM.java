import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SegmentedMM {
	
	private final int MEMSIZE; //memory size constant
	private int freemem, //amount of free memory (to track if allocation failures are due to fragmentation
				intfrag, //total amount of internal fragmentation
				nem, //total not-enough-memory allocation failures
				memf; //total memory-fragmentation allocation failures
	private List<Hole> memory; //list of free memory
	private Map<Integer,Map<Integer,Integer>> table;
	private Map<Integer,Integer> frag;
	
	private class Hole implements Comparable<Object> {
		
		private int address, size; //location in memory and size
		
		public Hole(int address, int size)
		{
			this.address = address;
			this.size = size;
		}
		
		public int compareTo(Object arg0) {
			Hole other = (Hole)arg0;
			if(this.size > other.size)
				return 1;
			else if(this.size < other.size)
				return -1;
			else
				return 0;
		}
			
	}
	
	public SegmentedMM(int memsize)
	{
		MEMSIZE = memsize;
		freemem = memsize;
		intfrag = 0;
		nem = 0;
		memf = 0;
		memory = new ArrayList<Hole>();
		memory.add(new Hole(0,memsize));
		table = new HashMap<Integer,Map<Integer,Integer>>();
		frag = new HashMap<Integer, Integer>();
	}
	
	public int allocate(int pid, int text_size, int data_size, int heap_size)
	{
		int allocated = -1; //was allocation successful
	
		int[] addresses = new int[]{-1,-1,-1}; //three memory addresses for the process
		int[] chosenHoles = new int[]{-1,-1,-1}; //locations in memory list of the holes
		
		int[] segments = new int[]{text_size,data_size,heap_size};
		
		//the size-sorted order of the segments
		int[] order;
		
		//find the correct order such that smallest segment's location in segments[] is order[0]
		if(segments[0] > segments[1] && segments[0] > segments[2])
			if(segments[1] > segments[2])
				order = new int[]{2,1,0};
			else
				order = new int[]{1,2,0};
		else if(segments[1] > segments[2] && segments[1] > segments[0])
			if(segments[0] > segments[2])
				order = new int[]{2,0,1};
			else
				order = new int[]{0,2,1};
		else
			if(segments[0] > segments[1])
				order = new int[]{1,0,2};
			else
				order = new int[]{0,1,2};
		
		//find the addresses to allocate at
		for(int i = 0,j = 0;i<memory.size()&&addresses[order[2]]==-1;i++)
		{
			Hole temp = memory.get(i);
			int segmentSize = segments[order[j]];
			if(segmentSize <= temp.size) //is the current Hole's size enough to fit the current segment
			{
				addresses[order[j]] = temp.address;
				chosenHoles[order[j]] = i;
				
				j++; //increment to the next smallest segment
				
				//loop until the remaining space in the current hole can't hold the next segment
				int newsize,newaddress;
				for(newsize = temp.size-segmentSize, newaddress = temp.address+segmentSize; j<3;j++)
				{
					segmentSize = segments[order[j]];
					if(segmentSize>newsize)
						break;
					chosenHoles[order[j]] = i;
					addresses[order[j]] = newaddress;
					newsize -= segmentSize;
					newaddress += segmentSize;
				}
			}
		}
		
		if(addresses[0] >= 0 && addresses[1] >= 0 && addresses[2] >= 0) //if the largest segment can be allocated the whole process can be
		{
			allocated = 0;
		
			List<Hole> todelete = new LinkedList<Hole>();
			for(int i = 0; i < 3; i++)
			{
				int segmentSize = segments[order[i]];
				Hole temp = memory.get(chosenHoles[i]);
				temp.size -= segmentSize;
				temp.address += segmentSize;
				if(temp.size <= 16 && temp.size > 0)
				{
					todelete.add(temp);
					segments[order[i]] += temp.size;
					intfrag += temp.size;
					frag.put(pid, temp.size);
				}
				
				freemem -= segmentSize; //update amount of free memory
			}
			
			for(Hole h: todelete)
				memory.remove(h);
			
			Collections.sort(memory);
			
			Map<Integer, Integer> m = new LinkedHashMap<Integer,Integer>(); //association of addresses and sizes
			for(int i = 0; i < 3; i++)
				m.put(addresses[i], segments[i]);
			
			table.put(pid,m); //PID used as key for map of addresses of segments and their sizes
		}
		else
		{
			if(freemem > segments[0]+segments[1]+segments[2])
			{
				allocated = 1;
				memf++;
			}
			else
				nem++;
		}
		return allocated;
	}
	


	public int deallocate(int pid)
	{
		if(!table.containsKey(pid))
			return -1;
		
		Map<Integer,Integer> process = table.remove(pid);
		if(frag.containsKey(pid))
		{
			intfrag -= frag.get(pid);
			frag.remove(pid);
		}
		
		for(Map.Entry<Integer, Integer> e : process.entrySet())
		{
			memory.add(new Hole(e.getKey(),e.getValue()));
			freemem += e.getValue();
		}
		
		Collections.sort(memory, new Comparator<Object>(){

			public int compare(Object arg0, Object arg1) {
				Hole h0 = (Hole) arg0;
				Hole h1 = (Hole) arg1;
				
				if(h0.address < h1.address)
					return -1;
				else if(h0.address > h1.address)
					return 1;
				return 0;
			}
		});
		
		for(int i = 0; i < memory.size()-1;)
		{
			Hole current = memory.get(i);
			Hole next = memory.get(i+1);
			if(current.address + current.size == next.address)
			{
				current.size+=next.size;
				memory.remove(i+1);
			}
			else
				i++;
		}
		
		return 1;
		
		// deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message 
	}
	
	public void printMemoryState()
	{
		String s = "policy: 0 (segmentation)\n"
				+ "Memory size = " + MEMSIZE + " bytes, allocated bytes = " + (MEMSIZE-freemem) + ", free = " + freemem
				+ "\n------------------------------------------------------------\n"
				+ "Hole List:\n\n";
		
		for(int i = 0; i < memory.size(); i++)
		{
			Hole temp = memory.get(i);
			s += "hole " + i + ": start location = " + temp.address + ", size = " + temp.size + "\n"; 
		}
		
		s += "------------------------------------------------------------\n"
				+ "Process List:\n";
		
		for(Map.Entry<Integer, Map<Integer,Integer>> e : table.entrySet())
		{
			s += "\nprocess id = " + e.getKey() + ", size = ";
			int size = 0;
			for(int i : e.getValue().values())
			{
				size += i;
			}
			s += size + "\ntext data heap ";
			int offset = 10; //offset used to splice the values next to the text, data, and heap labels
			
			for(Map.Entry<Integer, Integer> se : e.getValue().entrySet())
			{
				s = s.substring(0,s.length()-offset) + "start = " + se.getKey() + ", size = " + se.getValue() + "\n" + s.substring(s.length()-offset);
				offset -= 5;
			}
		}
		
		s += "------------------------------------------------------------\n"
				+ "Fragmentation:\n"
				+ "Total Internal Fragmentation = " + intfrag + " bytes\n"
				+ "Failed allocations (No memory) = " + nem + "\n"
				+ "Failed allocations (External Fragmentation) = " + memf + "\n";
		System.out.println(s);
	}

}
