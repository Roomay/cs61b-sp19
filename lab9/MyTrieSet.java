import java.util.ArrayList;
import java.util.List;


public class MyTrieSet implements TrieSet61B {

    private static final int ASCII = 128;

    private Node root;
    private int size;
    private static class Node {
        private boolean isKey = false;
        private Node[] next = new Node[ASCII];
    }

    private boolean get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = getHelper(root, key, 0);
        if (x == null) {
            return false;
        }
        return x.isKey;
    }
    private Node getHelper(Node x, String key, int depth) {
        if (x == null) {
            return null;
        }
        if (depth == key.length()) {
            return x;
        }
        char c = key.charAt(depth);
        return getHelper(x.next[c], key, depth + 1);
    }

    private Node addHelper(Node x, String key, int depth) {
        if (x == null) {
            x = new Node();
        }
        if (depth == key.length()) {
            if (x.isKey = false) {
                size++;
            }
            x.isKey = true;
            return x;
        }
        char c = key.charAt(depth);
        x.next[c] = addHelper(x.next[c], key, depth + 1);
        return x;
    }

    private void collect(Node x, StringBuilder prefix, List<String> results) {
        if (x == null) {
            return;
        }
        if (x.isKey) {
            results.add(prefix.toString());
        }
        for (char c = 0; c < ASCII; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public MyTrieSet() {
        size = 0;
    }

    /** Clears all items out of Trie */
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(key);
    }

    /** Inserts string KEY into Trie */
    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        root = addHelper(root, key, 0);
    }



    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        ArrayList<String> results = new ArrayList<>();
        Node x = getHelper(root, prefix, 0);
        collect(x, new StringBuilder((prefix)), results);
        return results;
    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

}
