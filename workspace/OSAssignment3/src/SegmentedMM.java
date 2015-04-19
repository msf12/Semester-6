import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SegmentedMM {
	
	private final int MEMSIZE; //memory size constant
	private int freemem; //amount of free memory (to track if allocation failures are due to fragmentation
	private List<Hole> memory; //list of free memory
	private List<Map<Integer,Integer>> table;
	
	private class Hole implements Comparable<Object> {
		
		private int address, size; //location in memory and size
		private boolean empty; //is there a process here
		
		public Hole(int address, int size)
		{
			this.address = address;
			this.size = size;
			empty = true;
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
		memory = new ArrayList<Hole>();
		memory.add(new Hole(0,memsize));
		table = new ArrayList<Map<Integer,Integer>>();
	}
	
	public int allocate(int pid, int text_size, int data_size, int heap_size)
	{
		int allocated = -1; //was allocation successful
	
		int[] addresses = new int[]{-1,-1,-1}; //three memory addresses for the process
		int[] chosenHoles = new int[]{-1,-1,-1}; //locations in memory list of the holes
		
		int[] segments = new int[]{text_size,data_size,heap_size};
		
		//the size-sorted order of the segments
		int[] order = new int[3];
		
		//find the correct order such that smallest segment's location in segments[] is order[0]
		if(segments[0] < segments[1])
			order[1]++;
		else
			order[0]++;
		if(segments[1] < segments[2])
			order[2]++;
		else
			order[1]++;
		if(segments[0]<segments[2])
			order[2]++;
		else
			order[0]++;
		
		//find the addresses to allocate at
		for(int i = 0,j = 0;i<memory.size()&&addresses[2]==-1;i++)
		{
			Hole temp = memory.get(i);
			int segmentSize = segments[order[j]];
			if(segmentSize <= temp.size) //is the current Hole's size enough to fit the current segment
			{
				addresses[order[j]] = temp.address;
				chosenHoles[j] = i;
				
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
					newaddress+= segmentSize;
				}
			}
		}
		
		if(addresses[order[2]]!=-1) //if the largest segment can be allocated the whole process can be
		{
			allocated = 0;
		
			List<Hole> todelete = new LinkedList<Hole>();
			for(int i = 0; i < 3; i++)
			{
				int segmentSize = segments[order[i]];
				Hole temp = memory.get(chosenHoles[i]);
				temp.size -= segmentSize;
				temp.address += segmentSize;
				if(temp.size <= 16)
				{
					todelete.add(temp);
					segments[order[i]] += temp.size;
				}
				
				freemem -= segmentSize; //update amount of free memory
			}
			
			for(Hole h: todelete)
				memory.remove(h);
			
			Collections.sort(memory);
			
			Map<Integer, Integer> m = new LinkedHashMap<Integer,Integer>(); //association of addresses and sizes
			for(int i = 0; i < 3; i++)
				m.put(addresses[i], segments[i]);
			
			table.add(pid-1,m); //PID used as index for map of addresses of segments and their sizes
		}
		else
		{
			if(freemem > segments[0]+segments[1]+segments[2])
				allocated = 1;
		}
		return allocated;
	}
	


	public int deallocate(int pid)
	{
		if(table.get(pid-1) == null)
			return -1;
		
		Map<Integer,Integer> process = table.remove(pid-1);
		
		for(Map.Entry<Integer, Integer> e : process.entrySet())
		{
			memory.add(new Hole(e.getKey(),e.getValue()));
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

}
