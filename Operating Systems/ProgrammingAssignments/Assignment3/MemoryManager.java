import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MemoryManager {
	
	private final int POLICY;
	private SegmentedMM smem;
	private PagedMM pmem;
	
	public MemoryManager(int bytes, int policy) 
	{
		// intialize memory with these many bytes.
		POLICY = policy;
		if(policy == 0)
			smem = new SegmentedMM(bytes);
		else
			pmem = new PagedMM(bytes);
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
		{
			int allocation = pmem.allocate(bytes, pid);
			if(allocation != 0)
				System.out.println("Allocation failed: not enough memory\n");
			return allocation == 0 ? 1 : -1;
		}
	 
	}

	public int deallocate(int pid)
	{
		if(POLICY==0)
		{
			return smem.deallocate(pid);
		}
		else
		{
			return pmem.deallocate(pid);
		}
		//deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message 
	}

	public void printMemoryState()
	{
		if(POLICY==0)
		{
			smem.printMemoryState();
		}
		else
			pmem.printMemoryState();
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File("/home/mitchel/Documents/Division-2/Semester-6/Operating Systems/ProgrammingAssignments/Assignment3/test.txt"));
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
		scan.close();
	}

}
