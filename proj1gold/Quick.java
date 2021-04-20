public class Quick {
    public static void main(String[] args) {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(6);
        sad.addLast(1);
        System.out.println("removeFirst()" + sad.removeFirst());
        System.out.println("removeLast()" + sad.removeLast());
        return;
    }
}
