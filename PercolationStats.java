/**
 * This class perform simulation to percolation system, through Monte Carlo
 * simulation to To estimate the percolation threshold
 * 
 * @author Bicai Ji
 */
public class PercolationStats {
    private double[] x; // fraction of open sites
	private int times;

	/**
	 * constructor for class
	 * 
	 * @param N: N by N grid.
	 * @param T: time of simulations
	 */
	public PercolationStats(int N, int T) { // perform T independent experiments
											// on an N-by-N grid
		if (N <= 0)
			throw new IllegalArgumentException("Illegal input N");
		if (T <= 0)
			throw new IllegalArgumentException("Illegal input T");
		x = new double[T];
		times = T;
		for (int i = 0; i < T; i++) {
			double numberOfOpenSite = 0;
			double totalSiteNumber = N * N;
			Percolation pero = new Percolation(N);
			while (!pero.percolates()) {
				int p = StdRandom.uniform(N) + 1;
				int q = StdRandom.uniform(N) + 1;
				while (pero.isOpen(p, q)) {
					p = StdRandom.uniform(N) + 1;
					q = StdRandom.uniform(N) + 1;
				}
				pero.open(p, q);
				numberOfOpenSite++;
			}
			x[i] = numberOfOpenSite / totalSiteNumber;
		}
	}

	/**
	 * This method is to mean of calculate percolation threshold
	 * @return double mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(x);
	}

	/**
	 * This method is to calculate standard derivation of percolation threshold 
	 * @return double standard derivation of percolation threshold
	 */
	public double stddev() {
		return StdStats.stddev(x);
	}

	/**
	 * calculate low endpoint of 95% confidence interval
	 * @return double low endpoint of 95% confidence interval
	 */
	public double confidenceLo() {
		double lowThreshold;
		lowThreshold = mean() - (1.96 * stddev()) / Math.sqrt(times);
		return lowThreshold;
	}

	/**
	 * calculate high endpoint of 95% confidence interval
	 * @return double high endpoint of 95% confidence interval
	 */
	public double confidenceHi() {
		double highThreshold;
		highThreshold = mean() + (1.96 * stddev()) / Math.sqrt(times);
		return highThreshold;
	}

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats testPercolationStats = new PercolationStats(N, T);
		StdOut.printf("mean          =%10.10f\n", testPercolationStats.mean());
		StdOut.printf("stddev        =%10.10f\n", testPercolationStats.stddev());
		StdOut.printf("confidenceLo  =%10.10f\n",
				testPercolationStats.confidenceLo());
		StdOut.printf("confidenceHi  =%10.10f\n",
				testPercolationStats.confidenceHi());
	}

}
