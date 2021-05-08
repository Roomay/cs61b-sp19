import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<String> tas = new Queue<>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");

        Queue<Integer> tai = new Queue<>();
        tai.enqueue(5);
        tai.enqueue(2);
        tai.enqueue(3);
        tai.enqueue(1);
        tai.enqueue(4);

        tas = MergeSort.mergeSort(tas);
        tai = MergeSort.mergeSort(tai);

        assertTrue(isSorted(tai));
        assertTrue(isSorted(tas));
    }

    @Test
    public void testMergeSort() {
        Queue<String> tas = new Queue<>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");

        Queue<Integer> tai = new Queue<>();
        tai.enqueue(5);
        tai.enqueue(2);
        tai.enqueue(3);
        tai.enqueue(1);
        tai.enqueue(4);

        tas = MergeSort.mergeSort(tas);
        tai = MergeSort.mergeSort(tai);

        assertTrue(isSorted(tai));
        assertTrue(isSorted(tas));

    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
