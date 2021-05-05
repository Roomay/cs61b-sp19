package bearmaps;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    @Test
    public void testOperations() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();

        assertEquals(pq.size(), 0);

        pq.add(3, 3);
        pq.add(5, 5);
        pq.add(1, 1);
        pq.add(4, 4);
        pq.add(2, 2);

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
}
