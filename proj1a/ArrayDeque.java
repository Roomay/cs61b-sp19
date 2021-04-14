public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 4;
    }
/*
    public ArrayDeque(ArrayDeque other) {
        size = other.size();
        front = other.front;
        items = (T[]) new Object[other.items.length];
        System.arraycopy(other.items, 0, items, 0, items.length);
    }
 */

    public void addFirst(T item) {
        if (front == 0) {
            frontResize(size + 2);
        }
        front--;
        items[front] = item;
        size++;
    }

    public void addLast(T item) {
        if (front + size == items.length) {
            backResize(size + 2);
        }
        items[front + size] = item;
        size++;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (!isEmpty()) {
            System.out.print(items[front]);
            for (int i = front + 1; i < front + size; i++) {
                System.out.print(" ");
                System.out.print(items[i]);
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T element = items[front];
            front++;
            size--;
            if (items.length > 4 * size) {
                halfSize();
            }
            return element;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            size--;
            T element = items[front + size];
            if (items.length > 4 * size) {
                halfSize();
            }
            return element;
        }
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        } else {
            return items[front + index];
        }
    }

    private void frontResize(int extraSize) {
        T[] newItems = (T[]) new Object[front + size + extraSize];
        System.arraycopy(items, 0, newItems, extraSize, front + size);
        items = newItems;
        front = extraSize;
    }
    private void backResize(int extraSize) {
        T[] newItems = (T[]) new Object[front + size + extraSize];
        System.arraycopy(items, 0, newItems, 0, front + size);
        items = newItems;
    }
    private void halfSize() {
        T[] newItems = (T[]) new Object[size * 2];
        System.arraycopy(items, front, newItems, size / 2, size);
        items = newItems;
        front = size / 2;
    }
}
