import java.util.Set;
import java.util.TreeSet;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Iterator;

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

    @Override
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

    @Override
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

    @Override
    public Set<K> keySet() {
        Set<K> retSet = new TreeSet();
        for (K key
                :
                this) {
            retSet.add(key);
        }
        return retSet;
    }

    public K min() {
        if (size() == 0) {
            throw new NoSuchElementException("calls min() with empty symbol table");
        }
        return min(root).key;
    }

    private Entry min(Entry node) {
        if (node.left == null) {
            return node;
        } else {
            return min(node.left);
        }
    }

    private Entry deleteMin(Entry node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private class DeletedReturn {
        Entry newNode;
        V deletedNodeVal;
        DeletedReturn(Entry node) {
            newNode = node;
            deletedNodeVal = null;
        }
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls delete() with a null key");
        }
        DeletedReturn dr = delete(root, key);
        root = dr.newNode;
        return dr.deletedNodeVal;
    }

    private DeletedReturn delete(Entry node, K key) {
        DeletedReturn ret = new DeletedReturn(node);
        if (node == null) {
            return ret;
        }
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            DeletedReturn pass = delete(node.left, key);
            node.left = pass.newNode;
            ret.deletedNodeVal = pass.deletedNodeVal;
        } else if (cmp > 0) {
            DeletedReturn pass = delete(node.right, key);
            node.right = pass.newNode;
            ret.deletedNodeVal = pass.deletedNodeVal;
        } else {
            ret.deletedNodeVal = node.val;
            if (node.right == null) {
                ret.newNode = node.left;
                return ret;
            } else if (node.left == null) {
                ret.newNode = node.right;
                return ret;
            }
            Entry tmp = node;
            node = min(tmp.right);
            node.right = deleteMin(tmp.right);
            node.left = tmp.left;
        }
        node.size = size(node.left) + size(node.right) + 1;
        ret.newNode = node;
        return ret;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls delete() with a null key");
        }
        DeletedReturn dr = delete(root, key, value);
        root = dr.newNode;
        return dr.deletedNodeVal;
    }

    private DeletedReturn delete(Entry node, K key, V value) {
        DeletedReturn ret = new DeletedReturn(node);
        if (node == null) {
            return ret;
        }
        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            DeletedReturn pass = delete(node.left, key);
            node.left = pass.newNode;
            ret.deletedNodeVal = pass.deletedNodeVal;
        } else if (cmp > 0) {
            DeletedReturn pass = delete(node.right, key);
            node.right = pass.newNode;
            ret.deletedNodeVal = pass.deletedNodeVal;
        } else if (node.val.equals(value)) {
            ret.deletedNodeVal = node.val;
            if (node.right == null) {
                ret.newNode = node.left;
                return ret;
            } else if (node.left == null) {
                ret.newNode = node.right;
                return ret;
            }
            Entry tmp = node;
            ret.deletedNodeVal = value;
            node = min(tmp.right);
            node.right = deleteMin(tmp.right);
            node.left = tmp.left;
        }
        node.size = size(node.left) + size(node.right) + 1;
        ret.newNode = node;
        return ret;
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
