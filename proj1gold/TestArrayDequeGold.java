import static org.junit.Assert.*;
import org.junit.Test;
import java.util.LinkedList;

public class TestArrayDequeGold {
    public class FailureSequence extends LinkedList<String> {
        public FailureSequence() {
            super();
        }
        public void addSeq(String s) {
            addLast(s);
            if (size() > 4) {
                removeFirst();
            }
        }
        public void printLast() {
            System.out.print(peekLast());
        }

        public String printSeq() {
            StringBuilder a = new StringBuilder();
            for (String i: this) {
                a.append(i);
            }
            return a.toString();
        }
    }
    @Test
    public void testStudentAndSolutionDeque() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        FailureSequence msgSeq = new FailureSequence();
        Integer actual = 0;
        Integer expected = 0;
        for (Integer i = StdRandom.uniform(10); expected.equals(actual);
             i = StdRandom.uniform(10)) {
            double ranNumberBetween0and1 = StdRandom.uniform();
            if (sad.isEmpty() || ads.isEmpty()) {
                if (ranNumberBetween0and1 < 0.5) {
                    msgSeq.addSeq("addFirst(" + i + ")\n");
                    sad.addFirst(i);
                    ads.addFirst(i);
                } else {
                    msgSeq.addSeq("addLast(" + i + ")\n");
                    sad.addLast(i);
                    ads.addLast(i);
                }

            } else {
                if (ranNumberBetween0and1 < 0.25) {
                    msgSeq.addSeq("addFirst(" + i + ")\n");
                    sad.addFirst(i);
                    ads.addFirst(i);
                } else if (ranNumberBetween0and1 < 0.50) {
                    msgSeq.addSeq("addLast(" + i + ")\n");
                    sad.addLast(i);
                    ads.addLast(i);
                } else if (ranNumberBetween0and1 < 0.75) {
                    msgSeq.addSeq("removeFirst()\n");
                    actual = sad.removeFirst();
                    expected = ads.removeFirst();
                } else {
                    msgSeq.addSeq("removeLast()\n");
                    actual = sad.removeLast();
                    expected = ads.removeLast();
                }
            }
        }
        assertEquals(msgSeq.printSeq(), expected, actual);
    }
}
