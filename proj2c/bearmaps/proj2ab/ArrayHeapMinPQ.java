package bearmaps.proj2ab;

import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<HeapNode> minHeap;
    private HashMap<T, HeapNode> keyMap;

    public ArrayHeapMinPQ() {
        minHeap = new ArrayList<>();
        minHeap.add(null); // First node as a sentinel, in convenience for indices.
        keyMap = new HashMap<>();
    }

    /* Heap Definitions and Operations begin.*/
    private class HeapNode implements Comparable<HeapNode> {
        T item;
        double priority;

        HeapNode(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double newPriority) {
            priority = newPriority;
        }

        @Override
        public int compareTo(HeapNode other) {
            if (other == null) {
                throw new IllegalArgumentException();
            }
            return Double.compare(getPriority(), other.getPriority());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((HeapNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }


    private void swimUp(int index) {
        if (minHeap.size() == 1) {
            return;
        }
        int parent = index / 2;
        if (parent != 0 && minHeap.get(parent).compareTo(minHeap.get(index)) > 0) {
            Collections.swap(minHeap, index, parent);
            swimUp(parent);
        }
    }

    private void sinkDown(int index) {
        if (minHeap.size() == 1) {
            return;
        }

        int leftChild = index * 2;
        int rightChild = index * 2 + 1;
        int targetChild = -1;

        HeapNode indexNode = minHeap.get(index);

        if (leftChild == minHeap.size() - 1) {
            if (indexNode.compareTo(minHeap.get(leftChild)) > 0) {
                targetChild = leftChild;
            }
        } else if (rightChild < minHeap.size()) {
            HeapNode leftNode = minHeap.get(leftChild);
            HeapNode rightNode = minHeap.get(rightChild);
            if (indexNode.compareTo(leftNode) > 0) {
                if (leftNode.compareTo(rightNode) > 0) {
                    targetChild = rightChild;
                } else {
                    targetChild = leftChild;
                }
            } else if (indexNode.compareTo(rightNode) > 0) {
                targetChild = rightChild;
            }
        }

        if (targetChild != -1) {
            Collections.swap(minHeap, index, targetChild);
            sinkDown(targetChild);
        }
    }

    /* Heap Operations end.*/

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null. */

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        HeapNode newNode = new HeapNode(item, priority);
        minHeap.add(newNode);
        keyMap.put(item, newNode);
        swimUp(minHeap.size() - 1);
    }

    @Override
    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {
        return keyMap.containsKey(item);
    }

    @Override
    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() {
        if (keyMap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return minHeap.get(1).getItem();
    }

    @Override
    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() {
        if (keyMap.isEmpty()) {
            throw new NoSuchElementException();
        }
        T item = getSmallest();
        keyMap.remove(item);
        Collections.swap(minHeap, 1, minHeap.size() - 1);
        minHeap.remove(minHeap.size() - 1);
        sinkDown(1);
        return item;
    }

    @Override
    /* Returns the number of items in the PQ. */
    public int size() {
        return keyMap.size();
    }

    @Override
    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = minHeap.indexOf(keyMap.get(item));
        minHeap.get(index).setPriority(priority);
        swimUp(index);
        sinkDown(index);
    }
}
