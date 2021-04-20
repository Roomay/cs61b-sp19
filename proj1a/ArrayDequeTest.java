public class ArrayDequeTest {
    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkElement(Object expected, Object actual) {
        if (expected != actual) {
            System.out.println("get(index) returns " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed.
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<String> ad1 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        passed = checkSize(1, ad1.size()) && passed;
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.addLast("middle");
        passed = checkSize(2, ad1.size()) && passed;

        ad1.addLast("back");
        passed = checkSize(3, ad1.size()) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);

    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(0);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        //should be 0
        passed = checkElement(0, ad1.removeFirst()) && passed;

        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        ad1.addFirst(2);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        printTestStatus(passed);

    }

    public static void largeAddRemoveTest() {

        System.out.println("Running largeAdd/remove test.");

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        for (int i = 0; i < 50; i++) {
            ad1.addFirst(i);
        }
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;
        // should be 49
        passed = checkElement(49, ad1.get(0)) && passed;
        // should be 0
        passed = checkElement(0, ad1.get(49)) && passed;

        for (int i = 0; i < 25; i++) {
            ad1.removeFirst();
        }
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        // should be 24
        passed = checkElement(24, ad1.get(0)) && passed;

        // should be 0
        passed = checkElement(0, ad1.get(24)) && passed;

        // should be null
        passed = checkElement(null, ad1.get(25)) && passed;

        for (int i = 0; i < 25; i++) {
            ad1.addLast(24 - i);
        }

        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;
        // should be 24
        passed = checkElement(ad1.get(0), ad1.get(25)) && passed;
        // should be 0
        passed = checkElement(ad1.get(24), ad1.get(49)) && passed;

        for (int i = 0; i < 25; i++) {
            ad1.removeLast();
        }

        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;
        // should be 24
        passed = checkElement(24, ad1.get(0)) && passed;
        // should be 0
        passed = checkElement(0, ad1.get(24)) && passed;
        // should be null
        passed = checkElement(null, ad1.get(49)) && passed;



        printTestStatus(passed);

    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        largeAddRemoveTest();
    }
}
