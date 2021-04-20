public class TestBody{
	public static void main(String[] args) {
		checkBody();
	}

	private static void checkEquals(double expected, double actual, String label, double eps){
		if(!Double.isNaN(actual) && !Double.isInfinite(actual) && Math.abs(expected - actual) <= Math.max(expected, actual) * eps){
			System.out.println("PASS: " + label + "Expected " + expected + "and you gave " + actual);
		}
		else {
			System.out.println("FAIL: " + label + "Expected " + expected + "and you gave " + actual);
		}
	}

	private static void checkBody(){
		System.out.println("Checking Body...");

		Body earth = new Body(1.0, 1.0, 3.0, 4.0, 5.0, "earth.gif");
		Body sun = new Body(7, 9, 3.0, 4.0, 500, "sun.gif");

		checkEquals(1.67e-9, earth.calcForceExertedBy(sun), "force to earth ", 0.01);
		checkEquals(1.67e-9, sun.calcForceExertedBy(earth), "force to sun ", 0.01);
	}

}