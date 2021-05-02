public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        if (input.length() < pattern.length()) {
            return -1;
        }
        RollingString rPattern = new RollingString(pattern, pattern.length());
        RollingString rInput = new RollingString(input.substring(0, pattern.length()), pattern.length());

        if (rInput.equals(rPattern)) {
            if (rInput.toString().equals(pattern)) {
                return 0;
            }
        }

        for (int i = pattern.length(); i < input.length(); i++) {
            rInput.addChar(input.charAt(i));
            if (rInput.equals(rPattern)) {
                if (rInput.toString().equals(pattern)) {
                    return i - pattern.length() + 1;
                }
            }
        }

        return -1;
    }
}
