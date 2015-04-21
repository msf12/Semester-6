import java.util.LinkedList;
import java.util.List;

public class PagedMM {
	
	private final int MAXPAGES;
	boolean[] physpages; //true = occupied
	private int numpages,
				allocfails,
				numprcs,
				intfrag;
	List<Process> prcs;

	private class Process {
		int id;
		int size;
		int[][] pages; //[virtpagenum][0] = physpagenum; [1] = bytes used
		
		public Process(int id, int size, int[][] pages)
		{
			this.id = id;
			this.size = size;
			this.pages = pages;
		}
		
	}
	
	public PagedMM(int memsize)
	{
		MAXPAGES = memsize/32;
		numpages = 0;
		prcs = new LinkedList<Process>();
		allocfails = 0;
		physpages = new boolean[MAXPAGES];
		numprcs = 0;
		intfrag = 0;
	}
	
	public int allocate(int bytes, int pid)
	{
		int pagecount = (int) Math.ceil(bytes/32.0);
		if(pagecount + numpages > MAXPAGES)
		{
			allocfails++;
			return -1;
		}
		
		numprcs++;
		
		int[][] pages = new int[pagecount][2];
		for(int i = 0; i < pagecount; i++)
		{
			pages[i][0] = -1;
		}
		
		for(int i = 0,j = 0; i < MAXPAGES && pages[pagecount-1][0] == -1; i++)
		{
			if(!physpages[i])
			{
				numpages++;
				physpages[i] = true;
				pages[j][0] = i;
				pages[j][1] = 32;
				j++;
			}
		}
		pages[pagecount-1][1] = (bytes+31)%32 + 1;
		intfrag += 32-pages[pagecount-1][1];
		
		prcs.add(new Process(pid, bytes, pages));
		
		return 0;
	}

	public int deallocate(int pid)
	{
		for(Process p : prcs)
		{
			if(p.id == pid)
			{
				for(int[] i : p.pages)
				{
					physpages[i[0]] = false;
					if(i[1] < 32)
						intfrag -= 32-i[1];
					numpages--;
				}
				numprcs--;
				prcs.remove(p);
				return 0;
			}
		}
		System.out.println("Process with id " + pid + " not found");
		return -1;
		//deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message 
	}
	
	public void printMemoryState()
	{
		String s = "policy: 0 (segmentation)\n"
				+ "Memory size = " + (MAXPAGES*32) + " bytes, allocated bytes = " + (MAXPAGES*32) + ", free = " + ((MAXPAGES-numprcs)*32)
				+ "\n------------------------------------------------------------\n"
				+ "Number of processes: " + numprcs + "\n"
				+ "Free page list:\n";
		
		for(int i = 0; i < physpages.length; i++)
		{
			if(!physpages[i])
				s+=i + " ";
		}
		
		s += "\n------------------------------------------------------------\n"
				+ "Process List:\n";
		
		for(Process p : prcs)
		{
			s += "\nprocess id = " + p.id + ", size = " + p.size + "\n";

			for(int i = 0; i < p.pages.length; i++)
			{
				s += "Virt Page " + i + " -> Phys Page " + p.pages[i][0] + " used: " + p.pages[i][1] + " bytes\n";
			}
		}
		
		s += "------------------------------------------------------------\n"
				+ "Fragmentation:\n"
				+ "Total Internal Fragmentation = " + intfrag + " bytes\n"
				+ "Failed allocations (No memory) = " + allocfails + "\n";
		System.out.println(s);
	}

}
