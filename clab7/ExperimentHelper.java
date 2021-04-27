import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        int totalLength = 0;
        for (int i = 1; i <= N; i++) {
            totalLength += log2(i);
        }
        return totalLength;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return (double) optimalIPL(N) / N;
    }

    public static void ranInsert(BST bst) {
        bst.add(RandomGenerator.getRandomInt(5000));
    }

    public static void ranDelSuc(BST bst) {
        bst.deleteTakingSuccessor(bst.getRandomKey());
    }

    public static void ranDelRan(BST bst) {
        bst.deleteTakingRandom(bst.getRandomKey());
    }

    public static int log2(int n) {
        return (int) (Math.log(n) / Math.log(2));
    }
}
