import java.util.*;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /*private ArrayList<Entry> keys;*/
    private int size;
    private float loadFactor;
    private HashSet<K> keys;
    private ArrayList<Entry>[] buckets;

    private class Entry {
        private K key;
        private V val;

        Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
        return h & (buckets.length - 1);
    }

    private void resize() {
        ArrayList<Entry>[] oldBuckets = buckets;
        clear();
        buckets = (ArrayList<Entry>[]) new ArrayList[oldBuckets.length * 2];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
        for (ArrayList<Entry> maps
                :
                oldBuckets) {
            for (Entry map
                    :
                 maps) {
                buckets[hash(map.key)].add(new Entry(map.key, map.val));
                keys.add(map.key);
                size++;
            }
        }
    }

    public MyHashMap() {
        loadFactor = 0.75f;
        keys = new HashSet<>();
        buckets = (ArrayList<Entry>[]) new ArrayList[16];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public MyHashMap(int initialSize) {
        loadFactor = 0.75f;
        keys = new HashSet<>(initialSize);
        buckets = (ArrayList<Entry>[]) new ArrayList[initialSize];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public MyHashMap(int initialSize, float loadFactor) {
        this.loadFactor = loadFactor;
        keys = new HashSet<>(initialSize, loadFactor);
        buckets = (ArrayList<Entry>[]) new ArrayList[initialSize];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        keys = new HashSet<>();
        buckets = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (containsKey(key)) {
            ArrayList<Entry> maps = buckets[hash(key)];
            for (Entry map
                    :
                    maps) {
                if (map.key.equals(key)) {
                    return map.val;
                }
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        ArrayList<Entry> map = buckets[hash(key)];
        if (containsKey(key)) {
            for (Entry item
                    :
                    map) {
                if (item.key.equals(key)) {
                    item.val = value;
                }
            }
        } else {
            map.add(new Entry(key, value));
            keys.add(key);
            size++;
            if ((float) size / (float) buckets.length > loadFactor) {
                resize();
            }
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keys;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }
}
