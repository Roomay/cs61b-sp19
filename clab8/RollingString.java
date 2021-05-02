import java.util.Iterator;
import java.util.LinkedList;

/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 */
class RollingString {


    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    /**
     * Private classes and methods.
     *
     * hashSum store current sum of hashed value;
     * hashChar(char c, int digit) generate a hash value for a char.
     */
    private int hashSum;
    private int length;
    private LinkedList<Character> stringPiece;
    private int hashChar(char c, int digit) {
        if (digit == 1) {
            return ((int) c) % PRIMEBASE;
        }
        return ((UNIQUECHARS  * hashChar(c, digit - 1)) % PRIMEBASE);
        //Correctness is not guaranteed.
    }

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */
    RollingString(String s, int length) {
        assert (s.length() == length);
        this.length = length;
        hashSum = 0;
        stringPiece = new LinkedList<>();
        for (int i = length - 1; i >= 0; i--) { // Sum the hash in reverse string order.
            char cur = s.charAt(i);
            hashSum = (hashSum + hashChar(cur, length - i)) % PRIMEBASE;
            stringPiece.addFirst(cur);
        }
    }

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        hashSum = (hashSum + PRIMEBASE - hashChar(stringPiece.removeFirst(), length)
                + hashChar(c, 1)) % PRIMEBASE;
        stringPiece.addLast(c);
    }


    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (char c
                :
                stringPiece) {
            strb.append(c);
        }
        return strb.toString();
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return length;
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(RollingString.class)) {
            return false;
        }
        Iterator<Character> iThis = stringPiece.iterator();
        char[] charArrayO = o.toString().toCharArray();
        for (char charO
                :
                charArrayO) {
            if (!iThis.hasNext()) {
                return false;
            }
            if (!iThis.next().equals(charO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        return hashSum;
    }
}
