
public class Percolation {
	private boolean[][] sites;
	private WeightedQuickUnionUF uf;
	private int N;
	
	public Percolation(int N)               // create N-by-N grid, with all sites blocked
	{
		if(N <= 0)
			throw new IllegalArgumentException("grid dimension " + N + " must be greater than 0");
		
		this.sites = new boolean[N][N];
		this.uf = new WeightedQuickUnionUF(N*N+2); //position N*N is the virtual start and N*N+1 is the virtual end for use testing percolation
		this.N = N;
	}
	
	public void open(int i, int j)          // open site (row i, column j) if it is not open already
	{
		if(i < 0 || i > N-1)
			throw new IndexOutOfBoundsException("row index " + i + "must be between 0 and " + (N-1));
		if(j < 0 || j > N-1)
			throw new IndexOutOfBoundsException("column index " + j + "must be between 0 and " + (N-1));
		
		/* uf uses a 1D array such that an item at position
		 * (i,j) is i*this.N deep in the 1D array and
		 * offset by j
		 */
		
		sites[i][j] = true;
		
		int currentSite = (i*N)+j;
		
		if(i == 0)
			uf.union(currentSite, N*N);
		else if(sites[i-1][j] == true)
			uf.union(currentSite, ((i-1)*N)+j);
		
		if(i == N-1)
			uf.union(currentSite,N*N+1);
		else if(sites[i+1][j] == true)
			uf.union(currentSite, ((i+1)*N)+j);
		
		if(j != 0 && sites[i][j-1] == true)
			uf.union(currentSite, (i*N)+(j-1));
		if(j != N-1 && sites[i][j+1] == true)
			uf.union(currentSite, (i*N)+(j+1));
	}
	
	public boolean isOpen(int i, int j)     // is site (row i, column j) open?
	{
		return sites[i][j];
	}
	
	public boolean isFull(int i, int j)     // is site (row i, column j) full?
	{
		return uf.connected((i*N)+j, N*N);
	}
	
	public boolean percolates()             // does the system percolate?
	{
		return isFull(N-1,N+1); //(N-1)*N + N+1 = N*N+1
	}
}