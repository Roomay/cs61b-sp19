package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.TarjanSCC;


import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int RECTANGLE_BASE_SIZE = 4;

    /* Tables and Sets storing the rooms and their connection details*/
    private HashMap<Position, RectangleSize> roomsSize;
    private HashMap<Position, Integer> roomsNo;
    private QuickUnionUF roomsUnionSet;
    private KDTree<Position> vertices; // store each corner of each room.
    private KDTree<Position> rooms; // store lowerleft corner of each room.


    class Position {
        int x;
        int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class RectangleSize {
        int height;
        int width;
        RectangleSize(int h, int w) {
            height = h;
            width = w;
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.


        InputSource str = new StringInputDevice(input);
        char c;
        StringBuilder buffer = new StringBuilder();
        while (str.possibleNextInput()) {
            c = str.getNextKey();
            if (c == 'N' || c == 'n') {
                break; // break and stage to seed input.
            } else if (c == 'Q' || c == 'q') {
                System.exit(0); // quit.
            } else if (c == 'L' || c == 'l') {
                break; // load a game from a file.
            }
        }
        //
        while (str.possibleNextInput()) {
            c = str.getNextKey();
            if (c >= '0' && c <= '9') {
                buffer.append(c);
            } else if (c == 'S' || c == 's'){
                break;
            }
        }

        long seed = Long.parseLong(buffer.toString());

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        tilesInit(finalWorldFrame);
        generateWorldBySeed(finalWorldFrame, seed);

        // Codes below process the "wsad" operations.

        return finalWorldFrame;
    }

    private void tilesInit(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void generateWorldBySeed(TETile[][] tiles, long seed) {
        Random generator = new Random(seed);
        int numOfRooms = 20 + generator.nextInt(20);
        addRooms(tiles, numOfRooms, generator);
        addHallways(tiles, generator);
        addLockedDoor(tiles, generator);
    }

    private void addRooms(TETile[][] tiles, int nums, Random generator) {
        roomsSize = new HashMap<>(nums);
        roomsNo = new HashMap<>(nums);
        roomsUnionSet = new QuickUnionUF(nums + 1);
        vertices = new KDTree<>();
        rooms = new KDTree<>();
        // draw rectangles;
        for (int i = 1; i <= nums; i++) {
            Position leftLowerCorner;
            do {
                int posX = generator.nextInt(WIDTH - 2);
                int posY = generator.nextInt(HEIGHT - 2);
                leftLowerCorner = new Position(posX, posY);
            }while (isCollided(leftLowerCorner, 1, 1));

            int rectWidth = 1 + generator.nextInt(RECTANGLE_BASE_SIZE + (nums - i) / 10);
            int rectHeight = 1 + generator.nextInt(RECTANGLE_BASE_SIZE + (nums - i) / 10);
            addSingleRoom(tiles, leftLowerCorner, rectHeight, rectWidth, generator);
            roomsNo.put(leftLowerCorner, i);
            rooms.put(leftLowerCorner, leftLowerCorner.x, leftLowerCorner.y);
        }
    }

    private void addSingleRoom(TETile[][] tiles, Position p, int height, int width, Random generator) {
        while (isCollided(p, height, width) && !(height == 1 && width == 1)) { // Shrink until not collided.
            if (height > 1 && width > 1) {
                if (generator.nextBoolean()) {
                    height--;
                } else {
                    width--;
                }
            } else if (height == 1 && width > 1) {
                width--;
            } else if (height > 1 && width == 1) {
                height--;
            }
        }

        /* Wall tiles*/
        int wallTopHeight = p.y + height + 2; // Excluded
        int wallRightWidth = p.x + width + 2; // Excluded
        int leftBar = p.x;
        int rightBar = wallRightWidth - 1; //
        int bottomBar = p.y;
        int topBar = wallTopHeight - 1; //
        for (int i = p.y; i < wallTopHeight; i++) {
            tiles[leftBar][i] = Tileset.WALL;
            tiles[rightBar][i] = Tileset.WALL;
        }
        for (int i = p.x; i < wallRightWidth; i++) {
            tiles[i][bottomBar] = Tileset.WALL;
            tiles[i][topBar] = Tileset.WALL;
        }

        /* Floor tiles*/
        for (int i = bottomBar + 1; i < topBar; i++) {
            for (int j = leftBar + 1; j < rightBar; j++) {
                tiles[j][i] = Tileset.FLOOR;
            }
        }

        /* Update KDTree for collision check.*/
        vertices.put(p, leftBar, bottomBar);
        vertices.put(new Position(leftBar, topBar), leftBar, topBar);
        vertices.put(new Position(rightBar, topBar), rightBar, topBar);
        vertices.put(new Position(rightBar, bottomBar), rightBar, bottomBar);
        /* Update Room's size for hallway generation.*/
        roomsSize.put(p, new RectangleSize(height, width)); // Put the final size into the HashMap.
        //System.out.println("base position: " + p.x + ", " + p.y + "-- size : width " + width + ", height " + height); // test
    }

    private boolean isCollided(Position p, int height, int width) {
        return (p.x + width + 1 >= WIDTH || p.y + height + 1 >= HEIGHT || vertices.barrierInRange(p.x, p.y, height, width, roomsSize));
    }

    private void addHallways(TETile[][] tiles, Random generator) {
        if (rooms.getRoot() == null) {
            return;
        }
        Stack<KDTree<Position>.kdtNode<Position>> traversalHelper = new Stack<>();
        KDTree<Position>.kdtNode<Position> cur = rooms.getRoot();
        while (!traversalHelper.isEmpty() || cur != null) {
            if (cur != null) {
                if (cur.leftChild != null) {
                    Position p1 = cur.key;
                    Position p2 = cur.leftChild.key;
                    if (!roomsUnionSet.connected(roomsNo.get(p1), roomsNo.get(p2))) {
                        connect(tiles, p1, p2, generator);
                    }
                }
                traversalHelper.push(cur);
                cur = cur.leftChild;
            } else {
                cur = traversalHelper.pop();
                if (cur.rightChild != null) {
                    Position p1 = cur.key;
                    Position p2 = cur.rightChild.key;
                    if (!roomsUnionSet.connected(roomsNo.get(p1), roomsNo.get(p2))) {
                        connect(tiles, p1, p2, generator);
                    }
                }
                cur = cur.rightChild;
            }
        }
    }
    private void connect(TETile[][] tiles, Position p1, Position p2, Random generator) {
        /* Mark the rooms as connected*/
        roomsUnionSet.union(roomsNo.get(p1), roomsNo.get(p2));
        int x1 = p1.x;
        int x2 = p2.x;
        int y1 = p1.y;
        int y2 = p2.y;
        int width1 = roomsSize.get(p1).width;
        int width2 = roomsSize.get(p2).width;
        int height1 = roomsSize.get(p1).height;
        int height2 = roomsSize.get(p2).height;
        if (x1 + width1 > x2 && x1 < x2 + width2) { // x overlap
            int xStart = Integer.max(x1 + 1, x2 + 1); // Random lower bound.
            int xEnd = Integer.min(x1 + width1, x2 + width2); // Random upper bound.
            int xWay = xStart + generator.nextInt(xEnd - xStart + 1);
            int yStart = Integer.min(y1 + height1 + 1, y2 + height2 + 1);
            int yEnd = Integer.max(y1, y2);
            for (int i = yStart; i <= yEnd; i++) {
                tiles[xWay][i] = Tileset.FLOOR;
                if (tiles[xWay - 1][i].character() == ' ') {
                    tiles[xWay - 1][i] = Tileset.WALL;
                }
                if (tiles[xWay + 1][i].character() == ' ') {
                    tiles[xWay + 1][i] = Tileset.WALL;
                }
            }
        } else if (y1 + height1 > y2 && y1 < y2 + height2) { // y overlap
            int yStart = Integer.max(y1 +1, y2 + 1); // Random lower bound.
            int yEnd = Integer.min(y1 + height1, y2 + height2); // Random upper bound.
            int yWay = yStart + generator.nextInt(yEnd - yStart + 1);
            int xStart = Integer.min(x1 + width1 + 1, x2 + width2 + 1);
            int xEnd = Integer.max(x1, x2);
            for (int i = xStart; i <= xEnd; i++) {
                tiles[i][yWay] = Tileset.FLOOR;
                if (tiles[i][yWay - 1].character() == ' ') {
                    tiles[i][yWay - 1] = Tileset.WALL;
                }
                if (tiles[i][yWay + 1].character() == ' ') {
                    tiles[i][yWay + 1] = Tileset.WALL;
                }
            }
        } else { // non overlap
            // determine in which direction (horizontal-span or vertical-span) first to breakout
            if (generator.nextBoolean()) { // vertical-span first
                int xGenLowerBound = Integer.max(x1 + 1, x2 + width2 + 2);
                int xGenUpperBound = Integer.min(x1 + width1 + 1, x2);
                int xOut = x1 > x2 ?  xGenLowerBound + generator.nextInt(x1 + width1 + 1 - xGenLowerBound) :
                        x1 + 1 + generator.nextInt(xGenUpperBound - x1 - 1);
                int yCorner = y2 + 1 + generator.nextInt(height2);

                // pre-draw the corner.
                for (int i = xOut - 1; i <= xOut + 1; i++) {
                    for (int j = yCorner - 1; j <= yCorner + 1; j++) {
                        if (tiles[i][j].character() == ' ') {
                            tiles[i][j] = Tileset.WALL;
                        }
                    }
                }

                // hallway before the corner.
                for (int i = y1 + 1; (y1 < y2 && i <= yCorner) || (y1 > y2 && i >= yCorner) ; i += (y1 < y2) ? 1 : -1) {
                    tiles[xOut][i] = Tileset.FLOOR;
                    if (tiles[xOut - 1][i].character() == ' ') {
                        tiles[xOut - 1][i] = Tileset.WALL;
                    }
                    if (tiles[xOut + 1][i].character() == ' ') {
                        tiles[xOut + 1][i] = Tileset.WALL;
                    }
                }

                // hallway after the corner.
                for (int i = xOut; (x1 < x2 && i <= x2) || (x1 > x2 && i >= x2 + width2 + 1) ; i += (x1 < x2) ? 1 : -1) {
                    tiles[i][yCorner] = Tileset.FLOOR;
                    if (tiles[i][yCorner - 1].character() == ' ') {
                        tiles[i][yCorner - 1] = Tileset.WALL;
                    }
                    if (tiles[i][yCorner + 1].character() == ' ') {
                        tiles[i][yCorner + 1] = Tileset.WALL;
                    }
                }
            } else { // horizontal-span first
                int yGenLowerBound = Integer.max(y1 + 1, y2 + height2 + 2);
                int yGenUpperBound = Integer.min(y1 + height1 + 1, y2);
                int yOut = y1 > y2 ?  yGenLowerBound + generator.nextInt(y1 + height1 + 1 - yGenLowerBound) :
                        y1 + 1 + generator.nextInt(yGenUpperBound - y1 - 1);
                int xCorner = x2 + 1 + generator.nextInt(width2);

                // pre-draw the corner.
                for (int i = yOut - 1; i <= yOut + 1; i++) {
                    for (int j = xCorner - 1; j <= xCorner + 1; j++) {
                        if (tiles[j][i].character() == ' ') {
                            tiles[j][i] = Tileset.WALL;
                        }
                    }
                }

                // hallway before the corner.
                for (int i = x1 + 1; (x1 < x2 && i <= xCorner) || (x1 > x2 && i >= xCorner) ; i += (x1 < x2) ? 1 : -1) {
                    tiles[i][yOut] = Tileset.FLOOR;
                    if (tiles[i][yOut - 1].character() == ' ') {
                        tiles[i][yOut - 1] = Tileset.WALL;
                    }
                    if (tiles[i][yOut + 1].character() == ' ') {
                        tiles[i][yOut + 1] = Tileset.WALL;
                    }
                }

                // hallway after the corner.
                for (int i = yOut; (y1 < y2 && i <= y2) || (y1 > y2 && i >= y2 + height2 + 1) ; i += (y1 < y2) ? 1 : -1) {
                    tiles[xCorner][i] = Tileset.FLOOR;
                    if (tiles[xCorner - 1][i].character() == ' ') {
                        tiles[xCorner - 1][i] = Tileset.WALL;
                    }
                    if (tiles[xCorner + 1][i].character() == ' ') {
                        tiles[xCorner + 1][i] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void addLockedDoor(TETile[][] tiles, Random generator) {
        int width = tiles[0].length;
        int height = tiles.length;
        int ptr, x, y;
        do {
            ptr = generator.nextInt(height * width);
            x = ptr % width;
            y = ptr / height;
        } while (!isOpenWall(tiles, x, y));

        tiles[x][y] = Tileset.LOCKED_DOOR;
    }

    private boolean isOpenWall(TETile[][] tiles, int x, int y) {
        int width = tiles[0].length;
        int height = tiles.length;
        if (!(tiles[x][y].character() == '#')) {
            return false;
        }
        if ((x < width - 1 && tiles[x + 1][y].character() == '·')
                || (x > 0 && tiles[x - 1][y].character() == '·')
                || (y < height - 1 && tiles[x][y + 1].character() == '·')
                || (y > 0 && tiles[x][y - 1].character() == '·')) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] testWorld = e.interactWithInputString("n3412S");

        ter.renderFrame(testWorld);
    }
}
