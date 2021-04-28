package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int SITE_STATUS_OPEN = 1;
    private static final int SITE_STATUS_BLOCKED = 0;

    private int[] openStatus;
    private WeightedQuickUnionUF sites;
    private WeightedQuickUnionUF fullSites;
    private int numberOfOpenSites;
    private int byScale;
    private int VIRTUAL_TOP_SITE;
    private int VIRTUAL_BOTTOM_SITE;

    // Create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        openStatus = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            openStatus[i] = SITE_STATUS_BLOCKED;
        }
        sites = new WeightedQuickUnionUF(N * N + 2);
        fullSites = new WeightedQuickUnionUF(N * N + 1);
        numberOfOpenSites = 0;
        byScale = N;
        VIRTUAL_TOP_SITE = N * N;
        VIRTUAL_BOTTOM_SITE = N * N + 1;
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
            // Top Virtual site check
            if (row == 0) {
                sites.union(site, VIRTUAL_TOP_SITE);
                fullSites.union(site, VIRTUAL_TOP_SITE);
            }
            // Bottom Virtual site check
            if (row == byScale - 1) {
                sites.union(site, VIRTUAL_BOTTOM_SITE);
            }

            int topSite = site - byScale;
            int leftSite = site - 1;
            int rightSite = site + 1;
            int bottomSite = site + byScale;

            // Top
            if (row > 0 && openStatus[topSite] == SITE_STATUS_OPEN) {
                sites.union(site, topSite);
                fullSites.union(site, topSite);
            }
            // Left
            if (col > 0 && openStatus[leftSite] == SITE_STATUS_OPEN) {
                sites.union(site, leftSite);
                fullSites.union(site, leftSite);
            }
            // Right
            if (col < byScale - 1 && openStatus[rightSite] == SITE_STATUS_OPEN) {
                sites.union(site, rightSite);
                fullSites.union(site, rightSite);
            }
            // Bottom
            if (row < byScale - 1 && openStatus[bottomSite] == SITE_STATUS_OPEN) {
                sites.union(site, bottomSite);
                fullSites.union(site, bottomSite);
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
        int site = row * byScale + col;
        if (fullSites.connected(site, VIRTUAL_TOP_SITE)) {
            return true;
        }
        return false;
    }

    // Number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return sites.connected(VIRTUAL_BOTTOM_SITE, VIRTUAL_TOP_SITE);
    }

    // use for unit testing
    public static void main(String[] args) {

    }
}
