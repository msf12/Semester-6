public class PercolationStats {
	
	private double[] averageOpenSites;
	private int numExperiments;
	
	public PercolationStats(int N, int T)   // perform T independent experiments on an N-by-N grid
	{
		if(N <= 0)
			throw new IllegalArgumentException("grid dimension " + N + " must be greater than 0");
		if(T <= 0)
			throw new IllegalArgumentException("number of experiments " + T + " must be greater than 0");
		
		averageOpenSites = new double[T];
		numExperiments = T;
		
		for (int k = 0; k < T; k++)
		{
			Percolation perc = new Percolation(N);
			int openCount = 0;
			
			for(int i = StdRandom.uniform(N), j = StdRandom.uniform(N); !perc.percolates(); i = StdRandom.uniform(N), j = StdRandom.uniform(N))
			{
				if(!perc.isOpen(i, j))
				{
					openCount++;
					perc.open(i, j);
				}
			}
						
			averageOpenSites[k] = ((double)openCount)/((double)(N*N));
	
		}
	}
	
	public double mean()                    // sample mean of percolation threshold
	{
		return StdStats.mean(averageOpenSites);
	}
	
	public double stddev()                  // sample standard deviation of percolation threshold
	{
		return StdStats.stddev(averageOpenSites);
	}
	
	public double confidenceLow()           // low  endpoint of 95% confidence interval
	{
		double mean = mean();
		double stddev = stddev();
		
		return mean - (1.96*stddev/(Math.sqrt(numExperiments)));
	}
	
	public double confidenceHigh()          // high endpoint of 95% confidence interval
	{
		double mean = mean();
		double stddev = stddev();
		
		return mean + (1.96*stddev/(Math.sqrt(numExperiments)));
	}
	
}