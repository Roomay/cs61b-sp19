public class NBody{
	public static double readRadius(String fname){
		In in = new In(fname);
		in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readBodies(String fname){
		In in = new In(fname);
		int range = in.readInt();
		Planet[] barr = new Planet[range];
		in.readDouble();
		for ( int i=0; i<range; i++) {
			barr[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
		}

		return barr;
	}

	public static void main(String[] args) {
		/**Reading*/
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet[] bodies = readBodies(filename);

		/**Drawing*/
		StdDraw.setScale(-radius, radius);
		String background = "images/starfield.jpg";
		StdDraw.picture(0,0, background);
		for (Planet element : bodies) {
			element.draw();
		}

		/**Animation*/
		StdDraw.enableDoubleBuffering();
		double[] xForces = new double[bodies.length];
		double[] yForces = new double[bodies.length];
		for ( double interval = 0; interval <= T; interval+=dt) {
			/**Calculation and Updates*/
			for ( int i=0; i<bodies.length;	i++) {
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
			}
			for ( int i=0; i<bodies.length; i++) {
				bodies[i].update(dt, xForces[i], yForces[i]);
			}

			/**Draw and Show*/
			StdDraw.picture(0,0,background);
			for (Planet element : bodies) {
				element.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
		}

		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (Planet element : bodies) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", element.xxPos, element.yyPos, element.xxVel, element.yyVel, element.mass, element.imgFileName);
		}
	}
}