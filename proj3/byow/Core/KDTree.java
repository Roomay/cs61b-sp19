package byow.Core;


import java.util.HashSet;

import java.util.Map;
import java.util.Stack;

class KDTree<K> {
    private kdtNode<K> root;
    private static final int COORDINATE_X = 0;
    private static final int COORDINATE_Y = 1;

    private kdtNode<K> put(K point, int x, int y, kdtNode<K> node, int coordinate) {
        if (node == null) {
            return new kdtNode<>(point, x, y, coordinate);
        } else {
            int pointKey = x;
            int nodeKey = node.getX();
            if (coordinate == COORDINATE_Y) {
                pointKey = y;
                nodeKey = node.getY();
            }

            /* Left Child is bound with a left / down point, right child a right / up one.*/
            if (pointKey - nodeKey < 0) {
                node.leftChild = put(point, x, y, node.leftChild, 1 - coordinate);
            } else {
                node.rightChild = put(point, x, y, node.rightChild, 1 - coordinate);
            }
        }
        return node;
    }

    public class kdtNode<K> {
        K key;
        int posX;
        int posY;
        int splitDirection;
        kdtNode<K> leftChild;
        kdtNode<K> rightChild;

        kdtNode(K point, int x, int y, int splitDirection) {
            key = point;
            this.splitDirection = splitDirection;
            posX = x;
            posY = y;
            leftChild = null;
            rightChild = null;
        }

        int getX() {
            return posX;
        }

        int getY() {
            return posY;
        }

        int getSplitDirection() {
            return splitDirection;
        }
    }

    KDTree() {}

    public void put(K point, int x, int y) {
        root = put(point, x, y, root, COORDINATE_X);
    }
    public kdtNode<K> getRoot() {
        return root;
    }

    public boolean barrierInRange(int x, int y, int height, int width, Map<K, Engine.RectangleSize> roomMap) {
        Stack<kdtNode<K>> traversalHelper = new Stack<>();
        HashSet<kdtNode<K>> visitedRecord = new HashSet<>();
        kdtNode<K> cur;
        traversalHelper.push(root);
        while (!traversalHelper.isEmpty()) {
            cur = traversalHelper.peek();
            if (cur == null) {
                traversalHelper.pop();
            } else {
                int curX = cur.getX();
                 int curY = cur.getY();
                int offsetX = curX - x;
                int offsetY = curY - y;

                if (offsetX >= 0 && offsetX < width + 2 && offsetY >= 0 && offsetY < height + 2) { // current p drops within range.
                    return true;
                } else if (roomMap.containsKey(cur.key)) {
                    int curWidth = roomMap.get(cur.key).width;
                    int curHeight = roomMap.get(cur.key).height;
                    int curOffsetX = -offsetX;
                    int curOffsetY = -offsetY;
                    if (curOffsetX >= 0 && curOffsetX < curWidth + 2 && curOffsetY >= 0 && curOffsetY < curHeight + 2) {
                        return true;
                    }
                }
                int goodSideFlagOffset = cur.getSplitDirection() == COORDINATE_X ?
                        x - curX : y - curY; // 1, 2
                int heightOrWidth = cur.getSplitDirection() == COORDINATE_X ?
                        width : height;

                if (visitedRecord.contains(cur)) { // 2 Second visit, bad side.
                    traversalHelper.pop();
                    kdtNode<K> badSide = goodSideFlagOffset + heightOrWidth + 2 <= 0 ? // bad side?
                            cur.rightChild : cur.leftChild; // 2
                    badSide = goodSideFlagOffset > 0 ? cur.leftChild : badSide;
                    traversalHelper.push(badSide);
                } else { // 1 First visit, good side.
                    visitedRecord.add(cur);
                    kdtNode<K> goodSide = goodSideFlagOffset + heightOrWidth + 2 <= 0 ? // good side ?
                            cur.leftChild : cur.rightChild; // 1
                    goodSide = goodSideFlagOffset > 0 ? cur.rightChild : goodSide;
                    traversalHelper.push(goodSide);
                }
            }
        }
        return false;
    }
}
