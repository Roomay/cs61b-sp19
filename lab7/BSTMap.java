import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    int size = 0;
    private Entry root;

    private class Entry {
        private K key;
        private V val;
        private Entry left, right;
        private int size;

        Entry(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    public void clear() {
        size = 0;
        root = null;
    }

    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }
    public V get(K key) {
        return get(root, key);
    }

    private V get(Entry node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.val;
        }
    }

    public int size() {
        return size(root);
    }

    private int size(Entry node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }

    public void put(K key, V value) {
        if (key == null) {
            throw  new IllegalArgumentException("calls put() with a null key");
        }
        root = put(root, key, value);
    }

    private Entry put(Entry node, K key, V val) {
        if (node == null) {
            return new Entry(key, val, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, val);
        } else if (cmp > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    public class BSTMapIterator implements Iterator<K> {
        private Entry cur;
        Stack<Entry> stack = null;
        public BSTMapIterator() {
            cur = root;
            stack = new Stack<>();
        }

        @Override
        public boolean hasNext() {
            return cur != null || !stack.isEmpty();
        }

        @Override
        public K next() {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            Entry target = stack.pop();
            cur = target.right;
            return target.key;
        }
    }

    public void printInOrder() {
        for (K key
                :
                this) {
            System.out.print(get(key) + " ");
        }
    }
}
