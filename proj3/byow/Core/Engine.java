package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.Main;
import com.sun.jdi.Value;
import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.TarjanSCC;
import edu.princeton.cs.introcs.StdDraw;


import java.awt.*;
import java.io.*;
import java.util.*;

public class Engine {

    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int RECTANGLE_BASE_SIZE = 4;

    /* Tables and Sets storing the rooms and their connection details. */
    private long seed;
    private HashMap<Position, RectangleSize> roomsSize;
    private HashMap<Position, Integer> roomsNo;
    private QuickUnionUF roomsUnionSet;
    private Set<Position> vertices; // store each corner of each room.
    private KDTree<Position> rooms; // store lowerleft corner of each room.

    /* Avatar Details. */
    private Position avatarPos;
    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_DOWN = 2;
    private static final int DIRECTION_LEFT = 3;
    private static final int[][] DIRECTION = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    /* String Process. */
    private static final boolean INPUT_BY_KEY_BOARD = true;
    private static final boolean INPUT_BY_STRING = false;

    /* Draw Parameter. */
    private static final int DRAW_MENU = 0;
    private static final int DRAW_SEED = 1;
    private static final int DRAW_INFO = 2;
    private static final int DRAW_SAVE = 3;
    private static final int DRAW_QUIT = 4;

    /* Save and Load */


    class Automaton { // An automaton to process the input string
        TETile[][] world = null;
        private boolean isKeyBoard;
        private String state = "start";
        private StringBuilder seedBuilder = new StringBuilder();
        private Map<String, String[]> table = new HashMap<>() {{
            put("start", new String[]{"seed", "play", "end", "start", "start", "start", "start", "start"});
            put("seed", new String[]{"seed", "seed", "seed", "seed", "play", "seed", "seed", "seed"});
            put("play", new String[]{"play", "play", "play", "play", "play", "play", "save", "play"});
            put("end", new String[]{"end", "end", "end", "end", "end", "end", "end", "end"});
            put("save", new String[]{"play", "play", "end", "play", "play", "play", "play", "play"});
        }};
        private Thread mouseCatcher;

        Automaton(boolean isKeyBoard) {
            this.isKeyBoard = isKeyBoard;
            if (isKeyBoard) {
                drawFrame(DRAW_MENU, null);
                /*
                Runnable mouseCatcherHandler = () -> {
                    try {
                        while (true) {
                            if (state.equals("play")) {
                                int x = (int) StdDraw.mouseX();
                                int y = (int) StdDraw.mouseY();
                                StdDraw.text(5, HEIGHT, world[x][y].description());
                            }
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {}
                };
                mouseCatcher = new Thread(mouseCatcherHandler);
                mouseCatcher.start();
                 */
            }
        }

        void process(char c) {
            switch (state) {
                case "start":
                    if (c == 'L' || c == 'l') {
                        Main.SavedInfo entry = Main.loadGame();
                        seed = entry.getSeed();
                        avatarPos = new Position(entry.getX(), entry.getY());
                        world = generateWorldBySeed(seed, avatarPos);

                        if (isKeyBoard) {
                            ter.initialize(WIDTH, HEIGHT);
                            ter.renderFrame(world);
                        }
                    } else if (c == 'N' || c == 'n') {
                        if (isKeyBoard) {
                            drawFrame(DRAW_SEED, "");
                        }
                    }

                    break;
                case "seed":
                    if (Character.isDigit(c)) {
                        seedBuilder.append(c); // Should check overflow if needed.
                        drawFrame(DRAW_SEED, seedBuilder.toString());
                    } else if (c == 'S' || c == 's') {
                        seed = Long.parseLong(seedBuilder.toString());
                        world = generateWorldBySeed(seed, null);
                        if (isKeyBoard) {
                            ter.initialize(WIDTH, HEIGHT);
                            ter.renderFrame(world);
                        }
                    }
                    break;
                case "play":
                    if (c == 'W' || c == 'S' || c == 'A' || c == 'D'
                            || c == 'w' || c == 's' || c == 'a' || c == 'd') {
                        move(world, c);
                        if (isKeyBoard) {
                            ter.renderFrame(world);
                            drawFrame(DRAW_INFO, null);
                        }
                    }
                    break;
                case "save":
                    if (c == 'Q' || c == 'q') {
                        drawFrame(DRAW_SAVE, null);
                        StdDraw.pause(1500);
                        Main.SavedInfo save = new Main.SavedInfo(seed, avatarPos.x, avatarPos.y);
                        Main.saveGame(save);

                    }
                default:
                    drawFrame(DRAW_QUIT, null);
                    StdDraw.pause(1500);
            }

            state = table.get(state)[get_col(c)]; // state transition
            if (state.equals("end")) { // end check
                drawFrame(DRAW_QUIT, null);
                StdDraw.pause(1500);
                System.exit(0);
            }
        }

        private void drawFrame(int mode, String s) {
            Font header = new Font("Monaco", Font.BOLD, 30);
            Font option = new Font("Monaco", Font.PLAIN, 20);
            Font info = new Font("Monaco", Font.BOLD, 12);
            switch (mode) {
                case DRAW_MENU:
                    StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
                    StdDraw.setFont(header);
                    StdDraw.setXscale(0, WIDTH);
                    StdDraw.setYscale(0, HEIGHT);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.text(WIDTH / 2, HEIGHT - 5, "CS61B: THE GAME");
                    StdDraw.setFont(option);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "New Game (N)");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit (Q)");
                    break;
                case DRAW_SEED:
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "Please enter a Seed (end up with an key S): ");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, s);
                    break;
                case DRAW_INFO:
                    Font preFont = StdDraw.getFont();
                    Color preColor = StdDraw.getPenColor();

                    StdDraw.setFont(info);
                    StdDraw.setPenColor(Color.WHITE);
                    int x = (int) StdDraw.mouseX();
                    int y = (int) StdDraw.mouseY();
                    StdDraw.text(3, HEIGHT - 1, world[x][y].description());

                    StdDraw.setFont(preFont);
                    StdDraw.setPenColor(preColor);
                    break;
                case DRAW_SAVE:
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(header);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "Your game is being saved.");
                    break;
                default:
                    StdDraw.setFont(header);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "The game is now quitting...");
            }
            StdDraw.show();
        }

        private int get_col(char c) {
            if (c == 'N' || c == 'n') {
                return 0;
            }
            if (c == 'L' || c == 'l') {
                return 1;
            }
            if (c == 'Q' || c == 'q') {
                return 2;
            }
            if (Character.isDigit(c)) {
                return 3;
            }
            if (c == 'S' || c == 's') {
                return 4;
            }
            if (c == 'W' || c == 'A' || c == 'D' || c == 'w' || c == 'a' || c == 'd') {
                return 5;
            }
            if (c == ':') {
                return 6;
            }
            return 7;
        }
    }

    class Position {
        int x;
        int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != Position.class) {
                return false;
            }
            return (this.x == ((Position) obj).x && this.y == ((Position) obj).y);
        }
        @Override
        public int hashCode() {
            return x * HEIGHT + y;
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
        Automaton automaton = new Automaton(INPUT_BY_KEY_BOARD);

        while (true) {  // Process input
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }

            automaton.process(StdDraw.nextKeyTyped());
        }

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
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        InputSource str = new StringInputDevice(input);
        Automaton automaton = new Automaton(INPUT_BY_STRING);

        while (str.possibleNextInput()) {
            automaton.process(str.getNextKey());
        }

        return automaton.world;
    }

    private void move(TETile[][] tiles, char direct) {
        int direction;
        switch (direct) {
            case 'W':
            case 'w':
                direction = DIRECTION_UP;
                break;
            case 'S':
            case 's':
                direction = DIRECTION_DOWN;
                break;
            case 'A':
            case 'a':
                direction = DIRECTION_LEFT;
                break;
            default:
                direction = DIRECTION_RIGHT;
        }
        int oldX = avatarPos.x, oldY = avatarPos.y;
        int x = oldX + DIRECTION[direction][0], y = oldY + DIRECTION[direction][1];
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && tiles[x][y].character() != '#') {
            avatarPos.x = x;
            avatarPos.y = y;
            tiles[oldX][oldY] = Tileset.FLOOR;
            tiles[x][y] = Tileset.AVATAR;
        }
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

    private TETile[][] generateWorldBySeed(long seed, Position avatar) {
        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        tilesInit(tiles);
        Random generator = new Random(seed);
        int numOfRooms = 20 + generator.nextInt(20);
        addRooms(tiles, numOfRooms, generator);
        addHallways(tiles, generator);
        addLockedDoor(tiles, generator);
        addAvatar(tiles, generator, avatar);
        return tiles;
    }

    private void addRooms(TETile[][] tiles, int nums, Random generator) {
        roomsSize = new HashMap<>(nums);
        roomsNo = new HashMap<>(nums);
        roomsUnionSet = new QuickUnionUF(nums + 1);
        vertices = new HashSet<>();
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
        vertices.add(p);
        vertices.add(new Position(leftBar, topBar));
        vertices.add(new Position(rightBar, topBar));
        vertices.add(new Position(rightBar, bottomBar));
        /* Update Room's size for hallway generation.*/
        roomsSize.put(p, new RectangleSize(height, width)); // Put the final size into the HashMap.
        //System.out.println("base position: " + p.x + ", " + p.y + "-- size : width " + width + ", height " + height); // test
    }

    private boolean isCollided(Position p, int height, int width) {
        if (p.x + width + 1 >= WIDTH || p.y + height + 1 >= HEIGHT) {
            return true;
        }

        int rightBar = p.x + width + 2;
        int topBar = p.y + height + 2;

        for (int i = p.x; i < rightBar; i++) {
            for (int j = 0; j < topBar; j++) {
                if (vertices.contains(new Position(i, j))) {
                    return true;
                }
            }
        }
        return false;
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
                int xOut = x1 > x2 ?  xGenLowerBound + generator.nextInt(Integer.max(x1 + width1 + 1 - xGenLowerBound, 1)) :
                        x1 + 1 + generator.nextInt(Integer.max(xGenUpperBound - x1 - 1, 1));
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
                int yOut = y1 > y2 ?  yGenLowerBound + generator.nextInt(Integer.max(y1 + height1 + 1 - yGenLowerBound, 1)) :
                        y1 + 1 + generator.nextInt(Integer.max(yGenUpperBound - y1 - 1, 1));
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
        int width = tiles.length;
        int height = tiles[0].length;
        int ptr, x, y;
        do {
            ptr = generator.nextInt(height * width);
            x = ptr % width;
            y = ptr / width;
        } while (!isOpenWall(tiles, x, y));

        tiles[x][y] = Tileset.LOCKED_DOOR;
    }

    private void addAvatar(TETile[][] tiles, Random generator, Position avatar) {
        int x, y;
        if (avatar == null) {
            int width = tiles.length;
            int height = tiles[0].length;
            int ptr;
            do {
                ptr = generator.nextInt(height * width);
                x = ptr % width;
                y = ptr / width;
            } while (tiles[x][y].character() != '·');

            avatarPos = new Position(x, y);
        } else {
            x = avatar.x;
            y = avatar.y;
            avatarPos = avatar;
        }

        tiles[x][y] = Tileset.AVATAR;
    }

    private boolean isOpenWall(TETile[][] tiles, int x, int y) {
        int width = tiles.length;
        int height = tiles[0].length;
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



    /*
    public static void main(String[] args) {

        Engine e = new Engine();
        e.interactWithKeyboard();

        //e.ter.initialize(WIDTH, HEIGHT);

        //TETile[][] testWorld = e.interactWithInputString("n3412SDD");

        //e.ter.renderFrame(testWorld);
    }
    */
}
