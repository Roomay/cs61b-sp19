package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] bucket = new int[M];
        for (int i = 0; i < M; i++) {
            bucket[i] = 0;
        }
        for (Oomage item
                :
                oomages) {
            int bucketNum = (item.hashCode() & 0x7FFFFFFF) % M;
            bucket[bucketNum]++;
        }
        for (int i = 0; i < M; i++) {
            if (bucket[i] * 50 < oomages.size() || Math.floor(bucket[i] * 2.5) > oomages.size()) {
                return false;
            }
        }
        return true;
    }
}
