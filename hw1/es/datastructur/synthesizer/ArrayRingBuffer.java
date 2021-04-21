package es.datastructur.synthesizer;
import org.hamcrest.internal.ArrayIterator;

import java.util.Iterator;

// Make sure to that this class and all of its methods are public
// Make sure to add the override tag for all overridden methods
// Make sure to make this class implement BoundedQueue<T>

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;

    /* Array for storing the buffer data. */
    private T[] rb;
    private int capacity;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        //  Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;

    }

    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    public void enqueue(T x) {
        //  Enqueue the item. Don't forget to increase fillCount and update
        //       last.
        if (isFull()) {
            throw new RuntimeException("Ring Buffer overflow");
        } else {
            rb[last] = x;
            last = (last + 1) % capacity();
            fillCount++;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T dequeue() {
        //  Dequeue the first item. Don't forget to decrease fillCount and
        //       update first.
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        } else {
            first = (first + 1) % capacity();
            fillCount--;
            return rb[(first - 1 + capacity()) % capacity()];
        }
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T peek() {
        //  Return the first item. None of your instance variables should
        //       change.
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        } else {
            return rb[first];
        }
    }

    //  When you get to part 4, implement the needed code to support
    //       iteration and equals.

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int pos;

        ArrayIterator() {
            pos = first;
        }

        public boolean hasNext() {
            return (!isEmpty() && (pos - first) < fillCount());
        }

        public T next() {
            pos++;
            return rb[(pos - 1 + capacity()) % capacity()];
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean result = true;
        Iterator<T> thisIterator = iterator();
        if (!(o instanceof ArrayRingBuffer)) {
            return false;
        } else {
            for (T item
                    :
                    (ArrayRingBuffer<T>) o) {
                if (thisIterator.hasNext()) {
                    result = result && (item.equals(thisIterator.next()));
                } else {
                    return false;
                }
            }
            if (thisIterator.hasNext()) {
                return false;
            }
            return result;
        }
    }
}
    //  Remove all comments that say TODO when you're done.
