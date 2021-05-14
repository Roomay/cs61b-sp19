package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.QuickUnionUF;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int RECTANGLE_BASE_SIZE = 4;

    /* Tables and Sets storing the rooms and their connection details*/
    private HashMap<Position, RectangleSize> roomsSize;
    private HashMap<Integer, Position> roomsNo;
    private QuickUnionUF roomsUnionSet;
    private KDTree<Position> vertices;


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
        if (input.charAt(0) != 'N' && input.charAt(0) != 'n') {
            throw new InputMismatchException();
        }

        InputSource str = new StringInputDevice(input);
        str.getNextKey();
        char c;
        StringBuilder buffer = new StringBuilder();
        while (str.possibleNextInput()) {
            c = str.getNextKey();
            if (c >= '0' && c <= '9') {
                buffer.append(c);
            } else {
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
        int height = tiles[0].length;
        int width = tiles.length;
        Random generator = new Random(seed);
        int numOfRooms = 20 + generator.nextInt(20);
        addRooms(tiles, numOfRooms, generator);
        //addHallways(tiles, generator);
    }

    private void addRooms(TETile[][] tiles, int nums, Random generator) {
        int width = tiles[0].length;
        int height = tiles.length;
        roomsSize = new HashMap<>(nums);
        roomsNo = new HashMap<>(nums);
        roomsUnionSet = new QuickUnionUF(nums + 1);
        vertices = new KDTree<>();
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
            roomsNo.put(nums, leftLowerCorner);
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

    public static void main(String[] args) {
        Engine e = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] testWorld = e.interactWithInputString("n123");

        ter.renderFrame(testWorld);
    }
}
