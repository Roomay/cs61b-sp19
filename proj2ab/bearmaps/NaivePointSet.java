package bearmaps;

import java.util.HashSet;
import java.util.List;

public class NaivePointSet implements PointSet {

    HashSet<Point> pointSet;

    public NaivePointSet(List<Point> points) {
        pointSet = new HashSet<>();
        pointSet.addAll(points);
    }

    public Point nearest(double x, double y) {
        double minSquareDistance = Double.MAX_VALUE;
        Point nearestPoint = null;
        for (Point point
                :
                pointSet) {
            double newSquareDistance = (point.getX() - x) * (point.getX() - x) + (point.getY() - y) * (point.getY() - y);
            if (Double.compare(newSquareDistance, minSquareDistance) < 0) {
                minSquareDistance = newSquareDistance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
}
