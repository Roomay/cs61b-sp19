package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] thresholdArray;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        thresholdArray = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation grid = pf.make(N);
            while (!grid.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                grid.open(row, col);
                thresholdArray[i] = (double) grid.numberOfOpenSites() / (double) (N * N);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        return mean - 1.96 * stddev / Math.sqrt(thresholdArray.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        return mean + 1.96 * stddev / Math.sqrt(thresholdArray.length);
    }
}
