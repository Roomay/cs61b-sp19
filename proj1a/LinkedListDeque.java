public class LinkedListDeque<T> {

    private class DequeNode {
        public T element;
        public DequeNode previous;
        public DequeNode next;
        public DequeNode (T item, DequeNode p, DequeNode n) {
            element = item;
            previous = p;
            next = n;
        }
        public T getRecursive(int index) {
            if (index == 0) {
                return element;
            }
            else {
                return next.getRecursive(index - 1);
            }
        }
    }

    private int size;
    public DequeNode sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new DequeNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
    }

    public LinkedListDeque(LinkedListDeque<T> other) {
        size = 0;
        sentinel = new DequeNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        DequeNode pSrc = other.sentinel.next;
        while (pSrc != other.sentinel) {
            addFirst(pSrc.element);
            pSrc = pSrc.next;
        }
    }

    public void addFirst(T item) {
        DequeNode oldFirst = sentinel.next;
        DequeNode newFirst = new DequeNode(item, sentinel, oldFirst);
        oldFirst.previous = newFirst;
        sentinel.next = newFirst;
        size ++;
    }

    public void addLast(T item) {
        DequeNode oldLast = sentinel.previous;
        DequeNode newLast = new DequeNode(item, oldLast, sentinel);
        oldLast.next = newLast;
        sentinel.previous = newLast;
        size ++;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (!isEmpty()) {
            DequeNode pPrinted = sentinel.next;
            while (pPrinted != sentinel) {
                System.out.print(pPrinted.element);
                if (pPrinted.next != sentinel) {
                    System.out.print(" ");
                }
                pPrinted = pPrinted.next;
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else {
            DequeNode oldFirst = sentinel.next;
            sentinel.next = oldFirst.next;
            oldFirst.next.previous = sentinel;
            size --;
            return oldFirst.element;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        else {
            DequeNode oldLast = sentinel.previous;
            sentinel.previous = oldLast.previous;
            oldLast.previous.next = sentinel;
            size --;
            return oldLast.element;
        }
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        else {
            DequeNode pNode = sentinel.next;
            for (int i = 0; i < index; i++) {
                pNode = pNode.next;
            }
            return pNode.element;
        }
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return sentinel.next.getRecursive(index);
    }
}