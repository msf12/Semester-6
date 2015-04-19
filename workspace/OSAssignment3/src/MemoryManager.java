import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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
				System.out.println("Allocation failed: external fragmentation\n");
			else if (allocation < 0)
				System.out.println("Allocation failed: not enough memory\n");
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
		if(POLICY==0)
		{
			smem.printMemoryState();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File("/home/mitchel/Documents/Division-2/Semester-6/Operating Systems/ProgrammingAssignments/Assignment3/sample.txt"));
		MemoryManager m = new MemoryManager(scan.nextInt(),scan.nextInt());
		while(scan.hasNext())
		{
			switch (scan.next()) {
			case "A":
				m.allocate(scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt());
				break;
			case "D":
				m.deallocate(scan.nextInt());
				break;
			case "P":
				m.printMemoryState();
				break;
			default:
				break;
			}
		}
	}

}
