package bearmaps;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import edu.princeton.cs.algs4.Stopwatch;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    @Test
    public void testOperations() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        assertEquals(pq.size(), 0);

        pq.add(3, 1);
        pq.add(5, 4);
        pq.add(1, 2);
        pq.add(4, 5);
        pq.add(2, 3);

        pq.changePriority(3, 3);
        pq.changePriority(5, 5);
        pq.changePriority(1, 1);
        pq.changePriority(4, 4);
        pq.changePriority(2, 2);

        assertEquals(pq.size(), 5);

        assertTrue(pq.contains(1));
        assertTrue(pq.contains(2));
        assertTrue(pq.contains(3));
        assertTrue(pq.contains(4));
        assertTrue(pq.contains(5));

        assertFalse(pq.contains(6));
        assertFalse(pq.contains(7));

        assertEquals(pq.getSmallest(), (Integer) 1);
        assertEquals(pq.removeSmallest(), (Integer) 1);
        assertFalse(pq.contains(1));

        assertEquals(pq.size(), 4);

        assertEquals(pq.getSmallest(), (Integer) 2);
        assertEquals(pq.removeSmallest(), (Integer) 2);
        assertFalse(pq.contains(2));

        assertEquals(pq.size(), 3);

        assertTrue(pq.contains(3));
        assertTrue(pq.contains(4));
        assertTrue(pq.contains(5));
        assertEquals(pq.getSmallest(), (Integer) 3);

        pq.changePriority(5, 1);
        assertEquals(pq.getSmallest(), (Integer) 5);
        assertEquals(pq.removeSmallest(), (Integer) 5);
        assertFalse(pq.contains(5));

        assertEquals(pq.size(), 2);

        pq.removeSmallest();
        pq.removeSmallest();
        assertEquals(pq.size(), 0);
    }

    @Test
    public void testRuntime() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        long start = System.currentTimeMillis();
        Stopwatch sw = new Stopwatch();

        for (int i = 0; i < 100000; i++) {
            /*
            double item = ThreadLocalRandom.current().nextDouble(0, 100);
            pq.add(item, item);
            */
            pq.add(i, ThreadLocalRandom.current().nextDouble(0, 100));
        }

        System.out.println("Average time per operation: " + sw.elapsedTime() / 100000 + " seconds.");

        for (int i = 0; i < 100000; i++) {
            /*
            double item = ThreadLocalRandom.current().nextDouble(0, 100);
            pq.changePriority(item, item);
            */
            pq.changePriority(i, i);
        }

        System.out.println("Average time per operation: " + sw.elapsedTime() / 103000 + " seconds.");

        double curItem = 0.0;
        double lastItem = 0.0;
        curItem = pq.removeSmallest();
        for (int i = 1; i < 100000; i++) {
            lastItem = curItem;
            curItem = pq.removeSmallest();
            assertTrue(Double.compare(lastItem, curItem) <= 0);
        }



        long end = System.currentTimeMillis();

        System.out.println("Total time elapsed: " + (end - start) / 1000.0 + " seconds.");
        System.out.println("Total time elapsed: " + sw.elapsedTime() + " seconds.");
    }
}
