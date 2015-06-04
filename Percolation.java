/**
 * The Percolation class represents a model for percolation system. It supports
 * open operation along with some other methods which give us the status of
 * sites in a percolation system, such as isOpen,isFull,percolates.
 * 
 * @author Bicai Ji
 */
public class Percolation {
    private WeightedQuickUnionUF UF1; // test percolation
	private WeightedQuickUnionUF UF2; // test full
	private boolean[][] opened; // all sites blocked
	private int count;
	private int virtualTop;
	private int virtualBottom;
	
	/**
	 * Initializes an percolation system model with N by N grid, represented by
	 * two union-find data structures with N*N isolated components 0 through
	 * N*N-1. One is to test percolates(UF1), another is to test isFull(UF2).
	 * Initializes opened array to store information of whether sites is
	 * opened(true) or not(false). Connect first row of both union find data
	 * structure to a virtual top. Connect last row of UF1 to a virtual bottom.
	 * @param N: Represents N by N grid.
	 */
	public Percolation(int N) {
		if (N <= 0)
			throw new IllegalArgumentException("N can not be negative");
		count = N;
		virtualTop = N * N; // set first node as virtual top
		virtualBottom = N * N + 1; // last node as virtual bottom
		UF1 = new WeightedQuickUnionUF(N * N + 2);
		UF2 = new WeightedQuickUnionUF(N * N + 2);
		opened = new boolean[N][N];

		for (int i = 0; i < N; i++) {
			UF1.union(virtualTop, i);
			UF2.union(virtualTop, i);
		}

		for (int i = N * (N - 1); i < N * N; i++) {
			UF1.union(virtualBottom, i);
		}

	}

	/**
	 * This private method turns 2-dimensional (row, column) pair into a
	 * 1-dimensional union find object index
	 * @param x:Index for row
	 * @param y:Index for column
	 * @return int Index for union find data structure
	 */
	private int xyTo1D(int x, int y) {
		int result;
		result = count * x + y;
		return result;
	}


	/**
	 * open site (row i, column j) if it is not open already
	 * 
	 * @param i: row index
	 * @param j: column index
	 */
	public void open(int i, int j) {
		int I = i - 1;
		int J = j - 1;
		if (!opened[I][J]) {
			opened[I][J] = true;
		}
		if ((J - 1) >= 0 && opened[I][J - 1]) {
			UF1.union(xyTo1D(I, J), xyTo1D(I, J - 1));
			UF2.union(xyTo1D(I, J), xyTo1D(I, J - 1));
		}
		if ((J + 1) < count && opened[I][J + 1]) {
			UF1.union(xyTo1D(I, J), xyTo1D(I, J + 1));
			UF2.union(xyTo1D(I, J), xyTo1D(I, J + 1));
		}
		if ((I - 1) >= 0 && opened[I - 1][J]) {
			UF1.union(xyTo1D(I, J), xyTo1D(I - 1, J));
			UF2.union(xyTo1D(I, J), xyTo1D(I - 1, J));
		}
		if ((I + 1) < count && opened[I + 1][J]) {
			UF1.union(xyTo1D(I, J), xyTo1D(I + 1, J));
			UF2.union(xyTo1D(I, J), xyTo1D(I + 1, J));
		}
	}

	/**
	 * Tell if site (row i, column j) is open
	 * @param i:row index
	 * @param j:column index
	 * @return boolean, whether site(i,j) is open(true or false)
	 */
	public boolean isOpen(int i, int j) {
		return opened[i - 1][j - 1];
	}

	/**
	 * Tell if site (row i, column j) is full i:row index
	 * @param j:column index
	 * @return boolean, whether site(i,j) is full(true or false)
	 */
	public boolean isFull(int i, int j) { // is site (row i, column j) full?
		if (opened[i - 1][j - 1]) {
			return UF2.connected(0, xyTo1D(i - 1, j - 1));
		} else
			return false;
	}

	/**
	 * Tell if the system percolates
	 * @return boolean
	 */
	public boolean percolates() {
		if (count == 1)
			return isOpen(1, 1);
		else
			return UF1.connected(virtualTop, virtualBottom);
	}

}
