
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        this.bears = new LinkedList<>(bears);
        this.beds = new LinkedList<>(beds);
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        return quickSort(bears, beds);
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return quickSort(beds, bears);
    }

    private static <Item extends HiddenComparable, AnotherItem extends HiddenComparable> int partition(
            List<Item> unsorted, AnotherItem pivot,
            List<Item> less, List<Item> equal, List<Item> greater) {
        if (unsorted.isEmpty() || (unsorted.get(0).getClass() == pivot.getClass())) {
            throw new IllegalArgumentException();
        }
        int retIndex = 0;
        for (Item item
                :
                unsorted) {
            int cmp = pivot.getClass() == Bear.class ?
                    ((Bear) pivot).compareTo((Bed) item) : ((Bed) pivot).compareTo((Bear) item);
            if (cmp < 0) {
                less.add(item);
            } else if (cmp == 0) {
                equal.add(item);
                retIndex = unsorted.indexOf(item);
            } else {
                greater.add(item);
            }
        }
        return retIndex;
    }

    private static <Item extends HiddenComparable> List<Item> catenate(List<Item> l1, List<Item> l2) {
        List<Item> catenated = new LinkedList<>(l1);
        catenated.addAll(l2);
        return catenated;
    }

    private static List<Bear> bearQuickSort(List<Bear> bears, List<Bed> beds) {
        if (bears.size() <= 1) {
            return bears;
        }
        List<Bear> lessBear = new LinkedList<>();
        List<Bear> equalBear = new LinkedList<>();
        List<Bear> greaterBear = new LinkedList<>();
        List<Bed> lessBed = new LinkedList<>();
        List<Bed> equalBed = new LinkedList<>();
        List<Bed> greaterBed = new LinkedList<>();
        int bedPivotIndex = beds.size() / 2;
        int rightIndex = partition(bears, beds.get(bedPivotIndex), lessBear, equalBear, greaterBear);
        partition(beds, bears.get(rightIndex), lessBed, equalBed, greaterBed);
        lessBear = bearQuickSort(lessBear, lessBed);
        greaterBear = bearQuickSort(greaterBear, greaterBed);
        return catenate(catenate(lessBear, equalBear), greaterBear);
    }
    // Now write a version for bedQuickSort or generic QuickSort.
    private static <T extends HiddenComparable, S extends HiddenComparable> List<T> quickSort(List<T> tList, List<S> sList) {
        if (tList.size() <= 1) {
            return tList;
        }
        List<T> lessT = new LinkedList<>();
        List<T> equalT = new LinkedList<>();
        List<T> greaterT = new LinkedList<>();
        List<S> lessS = new LinkedList<>();
        List<S> equalS = new LinkedList<>();
        List<S> greaterS = new LinkedList<>();
        int SPivotIndex = sList.size() / 2;
        int rightIndex = partition(tList, sList.get(SPivotIndex), lessT, equalT, greaterT);
        partition(sList, tList.get(rightIndex), lessS, equalS, greaterS);
        lessT = quickSort(lessT, lessS);
        greaterT = quickSort(greaterT, greaterS);
        return catenate(catenate(lessT, equalT), greaterT);
    }

    private List<Bear> bears;
    private List<Bed> beds;
}
