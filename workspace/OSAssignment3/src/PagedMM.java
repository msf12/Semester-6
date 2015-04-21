import java.util.LinkedList;
import java.util.List;


public class PagedMM {
	
	private final int MAXPAGES;
	boolean[] physpages;
	private int numpages,
				allocfails;
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
	}
	
	public int allocate(int bytes, int pid)
	{
		int pagecount = (int) Math.ceil(bytes/32.0);
		if(pagecount + numpages > MAXPAGES)
		{
			allocfails++;
			return -1;
		}
		
		int[][] pages = new int[pagecount][2];
		for(int i = 0; i < pagecount; i++)
		{
			pages[i][0] = -1;
		}
		
		for(int i = 0,j = 0; i < MAXPAGES && pages[pagecount-1][0] == -1; i++)
		{
			if(!physpages[i])
			{
				pages[j][0] = i;
				pages[j][1] = 32;
			}
		}
		pages[pagecount-1][1] = (bytes+31)%32 + 1;
		
		prcs.add(new Process(pid, bytes, pages));
		
		return 0;
	}

	public int deallocate(int pid)
	{
		for(int i = 0; i < prcs.size(); i++)
		{
			if(prcs.get(i).id == pid)
			{
				prcs.remove(i);
				return 0;
			}
		}
		System.out.println("Process with id " + pid + " not found");
		return -1;
		//deallocate memory allocated to this process
		// return 1 if successful, -1 otherwise with an error message 
	}

}
