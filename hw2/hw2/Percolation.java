package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int SITE_STATUS_OPEN = 1;
    private static final int SITE_STATUS_BLOCKED = 0;

    private int[] openStatus;
    private WeightedQuickUnionUF sites;
    private int numberOfOpenSites;
    private int byScale;

    // Create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        openStatus = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            openStatus[i] = SITE_STATUS_BLOCKED;
        }
        sites = new WeightedQuickUnionUF(N * N);
        int bottomLeft = N * (N - 1);
        for (int i = 0; i < N; i++) {
            sites.union(0, i);
            sites.union(bottomLeft, bottomLeft + i);
        }
        numberOfOpenSites = 0;
        byScale = N;
    }

    // Open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= byScale || col >= byScale) {
            throw new IndexOutOfBoundsException();
        }
        int site = row * byScale + col;
        if (openStatus[site] == SITE_STATUS_BLOCKED) {
            openStatus[site] = SITE_STATUS_OPEN;
            numberOfOpenSites++;
            int topSite = site - byScale;
            int leftSite = site - 1;
            int rightSite = site + 1;
            int bottomSite = site + byScale;
            // Top
            if (row > 0 && openStatus[topSite] == SITE_STATUS_OPEN) {
                sites.union(site, topSite);
            }
            // Left
            if (col > 0 && openStatus[leftSite] == SITE_STATUS_OPEN) {
                sites.union(site, leftSite);
            }
            // Right
            if (col < byScale - 1 && openStatus[rightSite] == SITE_STATUS_OPEN) {
                sites.union(site, rightSite);
            }
            // Bottom
            if (row < byScale - 1 && openStatus[bottomSite] == SITE_STATUS_OPEN) {
                sites.union(site, bottomSite);
            }
        }
    }

    // Is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= byScale || col >= byScale) {
            throw new IndexOutOfBoundsException();
        }
        return openStatus[row * byScale + col] == SITE_STATUS_OPEN;
    }

    // Is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= byScale || col >= byScale) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            if (row == 0) {
                return true;
            } else {
                int site = row * byScale + col;
                if (sites.connected(site, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // Does the system percolate?
    public boolean percolates() {
       /*for (int i = 0; i < byScale; i++) {
            if (isFull(byScale - 1, i)) {
                return true;
            }
        }
        return false;*/
        return isFull(byScale - 1, byScale - 1);
    }

    // use for unit testing
    public static void main(String[] args) {

    }
}
