
public class Test {

	public static void main(String[] args) {
		PercolationStats st = new PercolationStats(200,100);
		
		StdOut.println(st.mean());
		StdOut.println(st.stddev());
		StdOut.println(st.confidenceLow());
		StdOut.println(st.confidenceHigh());
		
	}

}
