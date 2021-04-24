import java.util.Arrays;

public class BubbleGrid {
    private int[][] grid;

    public BubbleGrid(int[][] gridInput) {
        // Assuming the grid input is valid.
        grid = new int[gridInput.length][];
        for (int i = 0; i < gridInput.length; i++) {
            grid[i] = Arrays.copyOf(gridInput[i], gridInput[i].length);
        }
    }

    public int[] popBubbles(int[][] darts) {
        int[] bubbleFall = new int[darts.length];
        int[] dartHits = new int[darts.length];
        // Initializing the stuck bubble counts;
        int[] bubbleStuck = new int[darts.length + 1];
        bubbleStuck[0] = stuckBubblesCount(grid);
        int rows = grid.length;

        // Initializing the darted grid.
        int[][] dartedGrid = new int[rows][];
        for (int i = 0; i < rows; i++) {
            dartedGrid[i] = grid[i].clone();
        }

        /* For each time of darts, set darted bubble point = 0 (pop),
            and count the stuck bubbles.
         */
        for (int i = 0; i < darts.length; i++) {
            if (darts[i].length != 2) {
                throw new IllegalArgumentException();
            }
            if (dartedGrid[darts[i][0]][darts[i][1]] == 1) {
                dartedGrid[darts[i][0]][darts[i][1]] = 0;
                dartHits[i] = 1;
            } else {
                dartHits[i] = 0;
            }
            bubbleStuck[i + 1] = stuckBubblesCount(dartedGrid);
        }

        // Compare stuckUnions and count falls.
        for (int i = 0; i < darts.length; i++) {
            bubbleFall[i] = bubbleStuck[i] - bubbleStuck[i + 1] - dartHits[i];
        }

        return bubbleFall;
    }

    /*  Return the total number of stuck bubbles in a (darted) grid.
        The absolute value can be unprecise but the relative value difference is correct.
     */
    public static int stuckBubblesCount(int[][] gridInput) {
        int rows = gridInput.length;
        int columns = gridInput[0].length;
        int bubbleStuck = 0;
        UnionFind stuckUnion = new UnionFind(rows * columns);
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (gridInput[i][j] == 1) {
                    if (gridInput[i - 1][j] == 1) {
                        stuckUnion.union(i * columns + j, (i - 1) * columns + j);
                    } else if (j > 0 && gridInput[i][j - 1] == 1) {
                        stuckUnion.union(i * columns + j, i * columns + j - 1);
                    } else if (j < columns - 1 && gridInput[i][j + 1] == 1) {
                        stuckUnion.union(i * columns + j, i * columns + j + 1);
                    } else {
                        gridInput[i][j] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < columns; i++) {
            bubbleStuck += stuckUnion.sizeOf(gridInput[0][i]);
        }
        return bubbleStuck;
    }

}
