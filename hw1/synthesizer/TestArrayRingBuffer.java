package synthesizer;
import org.junit.Test;

import java.lang.reflect.Array;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(4);
        Array expected;
        Array actual;

        assertTrue(arb.isEmpty());
        assertFalse(arb.isFull());
        assertEquals(arb.fillCount(), 0);
        assertEquals(arb.capacity(), 4);

        arb.enqueue(33.1);
        assertEquals(33.1, arb.peek(), 0.01);
        assertEquals(arb.fillCount(), 1);

        arb.enqueue(44.8);
        assertEquals(arb.fillCount(), 2);

        arb.enqueue(62.3);
        assertEquals(arb.fillCount(), 3);

        arb.enqueue(-3.4);
        assertEquals(arb.fillCount(), 4);

        assertTrue(arb.isFull());
        assertFalse(arb.isEmpty());
        assertEquals(33.1, arb.peek(), 0.01);
        assertEquals(arb.fillCount(), 4);

        assertEquals(33.1, arb.dequeue(), 0.01);
        assertEquals(arb.fillCount(), 3);
        assertEquals(44.8, arb.peek(), 0.01);

        arb.dequeue();
        assertEquals(arb.fillCount(), 2);
        assertEquals(62.3, arb.peek(), 0.01);

        arb.dequeue();
        assertEquals(arb.fillCount(), 1);
        assertEquals(-3.4, arb.peek(), 0.01);

        arb.dequeue();
        assertEquals(arb.fillCount(), 0);
        assertTrue(arb.isEmpty());
    }

    @Test
    public void testIteration() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<>(3);
        arb.enqueue(1.0);
        arb.enqueue(2.0);
        arb.enqueue(3.0);

        for (Double item :
                arb) {
            assertEquals(item, arb.dequeue(), 0.0001);
            System.out.print(item + " ");
        }
    }


    @Test
    public void testEqual() {
        ArrayRingBuffer<Double> arb1 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Double> arb2 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Double> arb3 = new ArrayRingBuffer<>(2);
        ArrayRingBuffer<Double> arb4 = new ArrayRingBuffer<>(1);
        ArrayRingBuffer<String> arb5 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Double> arb6 = new ArrayRingBuffer<>(2);

        arb1.enqueue(3.0);
        arb1.enqueue(2.0);

        arb2.enqueue(3.0);
        arb2.enqueue(2.0);
        arb2.enqueue(1.0);

        arb3.enqueue(3.0);
        arb3.enqueue(2.0);

        arb4.enqueue(3.0);

        arb5.enqueue("3.0");
        arb5.enqueue("3.0");
        arb5.enqueue("3.0");

        arb6.enqueue(3.0);
        arb6.enqueue(2.0);

        assertFalse(arb1.equals(arb2));
        assertTrue(arb1.equals(arb3));
        assertFalse(arb1.equals(arb4));
        assertFalse(arb1.equals(arb5));
        assertTrue(arb1.equals(arb6));
    }
}
