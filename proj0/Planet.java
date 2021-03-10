public class Planet{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Planet(Planet b){
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}

	public static final double G = 6.67e-11;

	public double calcDistance(Planet b){
		double distance = Math.sqrt(Math.pow((this.xxPos - b.xxPos),2)+Math.pow((this.yyPos - b.yyPos),2));
		return distance;
	}

	public double calcForceExertedBy(Planet b){
		double force = G * this.mass * b.mass / Math.pow(this.calcDistance(b),2);
		return force;
	}

	public double calcForceExertedByX(Planet b){
		double distance = calcDistance(b);
		double forceX = (b.xxPos - xxPos)/distance * calcForceExertedBy(b);
		return forceX;
	}

	public double calcForceExertedByY(Planet b){
		double distance = calcDistance(b);
		double forceY = (b.yyPos - yyPos)/distance * calcForceExertedBy(b);
		return forceY;
	}

	public double calcNetForceExertedByX(Planet[] ba){
		double netfX = 0;
		for ( Planet element : ba) {
			if(this.equals(element)){
				continue;
			}
			netfX += calcForceExertedByX(element);
		}
		return netfX;
	}
	
	public double calcNetForceExertedByY(Planet[] ba){
		double netfY = 0;
		for ( int i=0; i<ba.length; i+=1) {
			if(this.equals(ba[i])){
				continue;
			}
			netfY += calcForceExertedByY(ba[i]);
		}
		return netfY;
	}

	public void update(double dt, double fx, double fy){
		double ax = fx / mass;
		double ay = fy / mass;
		xxVel += dt * ax;
		yyVel += dt * ay;
		xxPos += dt * xxVel;
		yyPos += dt * yyVel;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images\\" + imgFileName);
	}
}