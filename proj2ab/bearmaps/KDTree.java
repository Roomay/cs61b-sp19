package bearmaps;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class KDTree implements PointSet {
    private kdtNode root;
    private static final int COORDINATE_X = 0;
    private static final int COORDINATE_Y = 1;

    private kdtNode put(Point point, kdtNode node, int coordinate) {
        if (node == null) {
            return new kdtNode(point, coordinate);
        } else {
            double pointKey = point.getX();
            double nodeKey = node.getX();
            if (coordinate == COORDINATE_Y) {
                pointKey = point.getY();
                nodeKey = node.getY();
            }

            /* Left Child is bound with a left / down point, right child a right / up one.*/
            if (Double.compare(pointKey, nodeKey) < 0) {
                node.leftChild = put(point, node.leftChild, 1 - coordinate);
            } else {
                node.rightChild = put(point, node.rightChild, 1 - coordinate);
            }
        }
        return node;
    }

    private class kdtNode {
        Point key;
        int splitDirection;
        kdtNode leftChild;
        kdtNode rightChild;

        public kdtNode(Point point, int splitDirection) {
            key = point;
            this.splitDirection = splitDirection;
            leftChild = null;
            rightChild = null;
        }

        public double getX() {
            return key.getX();
        }

        public double getY() {
            return key.getY();
        }

        public int getSplitDirection() {
            return splitDirection;
        }
    }

    public KDTree(List<Point> points) {
        for (Point point :
                points) {
            root = put(point, root, COORDINATE_X);
        }
    }

    public Point nearest(double x, double y) { //Flawed solution by simple traversal.
        double minSquareDistance = Double.MAX_VALUE;
        kdtNode nearestPoint = root;
        Stack<kdtNode> traversalHelper = new Stack<>();
        HashSet<kdtNode> visitedRecord = new HashSet<>();
        kdtNode cur;
        traversalHelper.push(root);
        while (!traversalHelper.isEmpty()) {
            cur = traversalHelper.peek();
            if (cur == null) {
                traversalHelper.pop();
            } else {
                if (visitedRecord.contains(cur)) { // 2 Second visit, bad side.
                    traversalHelper.pop();

                    double curX = cur.getX();
                    double curY = cur.getY();

                    double badSidePotentialSqDis = cur.getSplitDirection() == COORDINATE_X ?
                            (curX - x) * (curX - x) : (curY - y) * (curY - y); // 2
                    if (Double.compare(badSidePotentialSqDis, minSquareDistance) < 0) {
                        double goodSideFlagDistance = cur.getSplitDirection() == COORDINATE_X ?
                                x - curX : y - curY; // 1, 2
                        kdtNode badSide = Double.compare(goodSideFlagDistance, 0) < 0 ?
                                cur.rightChild : cur.leftChild; // 2
                        if (badSide != null) { // 2
                            traversalHelper.push(badSide);
                        }
                    }
                } else { // 1 First visit, good side.
                    visitedRecord.add(cur);

                    double curX = cur.getX();
                    double curY = cur.getY();

                    double newSquareDistance =
                            (curX - x) * (curX - x) + (curY - y) * (curY - y); // 1
                    if (Double.compare(newSquareDistance, minSquareDistance) < 0) { // 1
                        minSquareDistance = newSquareDistance;
                        nearestPoint = cur;
                    }

                    double goodSideFlagDistance = cur.getSplitDirection() == COORDINATE_X ?
                            x - curX : y - curY; // 1, 2
                    kdtNode goodSide = Double.compare(goodSideFlagDistance, 0) < 0 ?
                            cur.leftChild : cur.rightChild; // 1
                    if (goodSide != null) { // 1
                        traversalHelper.push(goodSide);
                    }
                }
            }
        }
        return nearestPoint.key;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        KDTree nn = new KDTree(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX() + " " +  ret.getY());
    }
}
