import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryManager {
	
	private final int MEMSIZE,POLICY;
	private List<int[]> table;
	private List<MemChunk> memory;
	
	private class MemChunk implements Comparable {
		
		private int fAddress, size; //location in memory and size
		private boolean empty; //is there a process here
		
		public MemChunk(int fAddress, int size)
		{
			this.fAddress = fAddress;
			this.size = size;
			empty = true;
		}
		
		public int compareTo(Object arg0) {
			MemChunk other = (MemChunk)arg0;
			if(other.empty && !this.empty)
				return 1;
			else if(this.empty && !other.empty)
				return -1;
			else if(this.empty && other.empty)
			{
				if(this.size > other.size)
					return 1;
				else if(this.size < other.size)
					return -1;
				else
					return 0;
			}
			else
				return 0;
		}
		
		
		
	}
	
	public MemoryManager(int bytes, int policy) 
	{ 
		// intialize memory with these many bytes.
		MEMSIZE = bytes;
		POLICY = policy;
		table = new ArrayList<int[]>();
		memory = new ArrayList<MemChunk>();
		memory.add(new MemChunk(0,MEMSIZE));
	}

	public int allocate(int bytes, int pid, int text_size, int data_size, int heap_size)
	{ 
		if(POLICY==0)
		{
			if(bytes != (text_size+data_size+heap_size))
				return -1;
			return segmentAllocate(pid, text_size, data_size, heap_size);
		}
		else
			return pageAllocate(bytes, pid);
		//allocate this many bytes to the process with this id 
		//assume that each pid is unique to a process 
		//if using the Segmentation allocator: size of each segment is: text_size,
		//..data_size, and heap_size.
		//Verify that text_size + data_size + heap_size = bytes
		//If using the paging allocator, simply ignore the segment size variables 
		//Return 1 if successful 
		//Return -1 if unsuccessful 
		//Print an error indicating whether there wasn't sufficient memory or whether you
		//..ran into external fragmentation
	 
	}
	
	private int segmentAllocate(int pid, int text_size, int data_size, int heap_size)
	{
		int allocated = -1; //was allocation successful
		int[] addresses = new int[3]; //three memory addresses for the process
		
		int[] sizes = new int[]{text_size,data_size,heap_size}; //array made for easy size sorting of the three segments
		Arrays.sort(sizes);
		
		for(int i = 0,j=0; memory.get(i).empty; i++) //iterate through memory - since the segments are sorted
		{
			MemChunk temp = memory.get(i);
			if(temp.size >= sizes[j])
			{
				memory.add(new MemChunk(temp.fAddress,sizes[j]));
				if(temp.fAddress+sizes[j] == temp.fAddress+temp.size)
					memory.remove(temp);
				else
					temp.fAddress += sizes[j];
				
			}
		}
		
		table.add(pid, addresses);
		return allocated;
	}
	
	private int pageAllocate(int bytes, int pid)
	{
		return -1;
	}

	public int deallocate(int pid)
	{ 
		return -1;
		//deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message 
	}

	public void printMemoryState()
	{ 
		// print out current state of memory
		// the output will depend on the memory allocator being used.


		// SEGMENTATION Example:
		// Memory size = 1024 bytes, allocated bytes = 179, free = 845
		// There are currently 10 holes and 3 active process
		//
		// Hole list:
		// 	hole 1: start location = 0, size = 202
		// ...
		//
		// Process list:
		// process id=34, size=95 allocation=95
		// 	text start=202, size=25
		// 	data start=356, size=16
		// 	heap start=587, size=54
		// process id=39, size=55 allocation=65
		// ...
		//
		// Total Internal Fragmentation = 10 bytes
		// Failed allocations (No memory) = 2
		// Failed allocations (External Fragmentation) = 7 

		// PAGING Example:
		// Memory size = 1024 bytes, total pages = 32
		// allocated pages = 6, free pages = 26
		// There are currently 3 active process
		// Free Page list:
		// 	2,6,7,8,9,10,11,12...
		//
		// Process list:
		// Process id=34, size=95 bytes, number of pages=3
		// 	Virt Page 0 -> Phys Page 0 used: 32 bytes
		// 	Virt Page 1 -> Phys Page 3 used: 32 bytes
		// 	Virt Page 2 -> Phys Page 4 used: 31 bytes
		//
		// Process id=39, size=55 bytes, number of pages=2
		// 	Virt Page 0 -> Phys Page 1 used: 32 bytes
		// 	Virt Page 1 -> Phys Page 13 used: 23 bytes
		//
		// Process id=46, size=29 bytes, number of pages=1
		// 	Virt Page 0 -> Phys Page 5 used: 29 bytes 
		//
		// Total Internal Fragmentation = 13 bytes
		// Failed allocations (No memory) = 2
		// Failed allocations (External Fragmentation) = 0 
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
