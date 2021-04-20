public class PlanetExtreme{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	
	public PlanetExtreme(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public PlanetExtreme(PlanetExtreme b){
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}

	private static final double G = 6.67e-11;
	private static final double RoughRho = 3e3;
	private static final double Pi = 3.14;

	public double calcDistance(PlanetExtreme b){
		double distance = Math.sqrt(Math.pow((this.xxPos - b.xxPos),2)+Math.pow((this.yyPos - b.yyPos),2));
		return distance;
	}

	public double calcForceExertedBy(PlanetExtreme b){
		double force = G * this.mass * b.mass / Math.pow(this.calcDistance(b),2);
		return force;
	}

	public double calcForceExertedByX(PlanetExtreme b){
		double distance = calcDistance(b);
		double forceX = (b.xxPos - xxPos)/distance * calcForceExertedBy(b);
		return forceX;
	}

	public double calcForceExertedByY(PlanetExtreme b){
		double distance = calcDistance(b);
		double forceY = (b.yyPos - yyPos)/distance * calcForceExertedBy(b);
		return forceY;
	}

	public double calcNetForceExertedByX(PlanetExtreme[] ba){
		double netfX = 0;
		for ( PlanetExtreme element : ba) {
			if(this.equals(element)){
				continue;
			}
			netfX += calcForceExertedByX(element);
		}
		return netfX;
	}
	
	public double calcNetForceExertedByY(PlanetExtreme[] ba){
		double netfY = 0;
		for ( int i=0; i<ba.length; i+=1) {
			if(this.equals(ba[i])){
				continue;
			}
			netfY += calcForceExertedByY(ba[i]);
		}
		return netfY;
	}

	public double calcPlanetRadius(){
		double radius = Math.pow(mass/Pi*3/4/RoughRho, 1.00/3);
		return radius;
	}

	public boolean isTooNear(PlanetExtreme p){
		if(calcDistance(p) <calcPlanetRadius()+p.calcPlanetRadius() && !this.equals(p)){
			return true;
		}
		return false;
	}

	public static void elasticCollision(PlanetExtreme p1, PlanetExtreme p2){
		double oldv1x = p1.xxVel;
		double oldv2x = p2.xxVel;
		double oldv1y = p1.yyVel;
		double oldv2y = p2.yyVel;
		p1.xxVel = oldv1x - 2*p2.mass/(p1.mass+p2.mass)*((oldv1x-oldv2x)*(p1.xxPos-p2.xxPos)+(oldv1y-oldv2y)*(p1.yyPos-p2.yyPos))/(Math.pow(p1.xxPos-p2.xxPos,2)+Math.pow(p1.yyPos-p2.yyPos,2))*(p1.xxPos-p2.xxPos);
		p1.yyVel = oldv1y - 2*p2.mass/(p1.mass+p2.mass)*((oldv1x-oldv2x)*(p1.xxPos-p2.xxPos)+(oldv1y-oldv2y)*(p1.yyPos-p2.yyPos))/(Math.pow(p1.xxPos-p2.xxPos,2)+Math.pow(p1.yyPos-p2.yyPos,2))*(p1.yyPos-p2.yyPos);
		p2.xxVel = oldv2x - 2*p1.mass/(p1.mass+p2.mass)*((oldv2x-oldv1x)*(p2.xxPos-p1.xxPos)+(oldv2y-oldv1y)*(p2.yyPos-p1.yyPos))/(Math.pow(p1.xxPos-p2.xxPos,2)+Math.pow(p1.yyPos-p2.yyPos,2))*(p2.xxPos-p1.xxPos);
		p2.yyVel = oldv2y - 2*p1.mass/(p1.mass+p2.mass)*((oldv2x-oldv1x)*(p2.xxPos-p1.xxPos)+(oldv2y-oldv1y)*(p2.yyPos-p1.yyPos))/(Math.pow(p1.xxPos-p2.xxPos,2)+Math.pow(p1.yyPos-p2.yyPos,2))*(p2.yyPos-p1.yyPos);

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
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}