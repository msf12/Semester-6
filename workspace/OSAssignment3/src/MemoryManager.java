
public class MemoryManager {
	
	private final int MEMSIZE,POLICY;
	private SegmentedMM smem;
	
	public MemoryManager(int bytes, int policy) 
	{ 
		// intialize memory with these many bytes.
		MEMSIZE = bytes;
		POLICY = policy;
		if(policy == 0)
			smem = new SegmentedMM(bytes);
	}

	public int allocate(int bytes, int pid, int text_size, int data_size, int heap_size)
	{
		if(POLICY==0)
		{
			if(bytes != (text_size+data_size+heap_size))
				return -1;
			int allocation = smem.allocate(pid, text_size, data_size, heap_size);
			if(allocation > 0)
				System.out.println("Allocation failed: external fragmentation");
			else if (allocation < 0)
				System.out.println("Allocation failed: not enough memory");
			else
				return 1;
			return -1;
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
	
	private int pageAllocate(int bytes, int pid)
	{
		return -1;
	}

	public int deallocate(int pid)
	{ 
		if(POLICY==0)
		{
			return smem.deallocate(pid);
		}
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
		MemoryManager m = new MemoryManager(1000,0);
		m.allocate(300, 1, 50, 150, 100);
		m.allocate(300, 2, 50, 150, 100);
		m.allocate(300, 3, 50, 150, 100);
		m.allocate(300, 4, 50, 150, 100);
		m.deallocate(1);
		m.deallocate(2);
	}

}
