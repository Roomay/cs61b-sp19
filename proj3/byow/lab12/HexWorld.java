package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void tesselationOfHexagon(TETile[][] world, int size) {
        List<Position> lowerLeftSet = positionOfTesselation(world, size);
        for (Position p
                :
                lowerLeftSet) {
            addHexagon(world, p, size, randomTile());
        }
    }

    private static TETile randomTile() {
        int tileNum = new Random().nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.WATER;
        }
    }

    private static List<Position> positionOfTesselation(TETile[][] world, int size) {
        List<Position> ret = new LinkedList<>();
        int maxHorizontalSpan = size * 3 - 2;
        int avrHorizontalSpan = size * 2 - 1;
        int edgeTileNum = Integer.min(
                (world.length - maxHorizontalSpan) / avrHorizontalSpan / 2 + 1,
                (world[0].length / (size * 2) + 1) / 2);
        int tileMaxNumXY = edgeTileNum * 2 - 1;
        int middlePosX = world.length / 2 - maxHorizontalSpan / 2;
        for (int i = 0; i < edgeTileNum; i++) {
            int tileNumY = edgeTileNum + i;
            int posX = middlePosX - avrHorizontalSpan * (edgeTileNum - 1 - i);
            int startPosY =
                    (edgeTileNum * 2 - 1 - tileNumY - tileMaxNumXY) * size + world[0].length / 2;
            for (int j = 0; j < tileNumY; j++) {
                int posY = startPosY + j * size * 2;
                ret.add(new Position(posX, posY));
            }
        }
        for (int i = tileMaxNumXY - 1; i >= edgeTileNum; i--) { // Here
            int tileNumY = edgeTileNum + tileMaxNumXY - 1 - i;
            int posX = middlePosX - avrHorizontalSpan * (edgeTileNum - 1 - i);
            int startPosY =
                    (edgeTileNum * 2 - 1 - tileNumY - tileMaxNumXY) * size + world[0].length / 2;
            for (int j = 0; j < tileNumY; j++) {
                int posY = startPosY + j * size * 2;
                ret.add(new Position(posX, posY));
            }
        }
        return ret;
    }

    public static void addHexagon(TETile[][] world, Position lowerLeft, int size, TETile teTile) {
        int[][] xInterval = xIntervalOfDraw(lowerLeft, size);
        int bottom = lowerLeft.y;
        int middle = lowerLeft.y + size;
        int top = lowerLeft.y + size * 2;
        int middleBound = Integer.min(world[0].length, middle);
        int heightBound = Integer.min(world[0].length, top);
        for (int row = bottom; row < middleBound; row++) {
            int widthBound = Integer.min(xInterval[row - bottom][1], world.length);
            for (int col = xInterval[row - bottom][0]; col < widthBound; col++) {
                world[col][row] = TETile.colorVariant(teTile, 255, 255, 255, new Random());
            }
        }
        for (int row = middleBound; row < heightBound; row++) {
            int widthBound = Integer.min(xInterval[row - bottom][1], world.length);
            for (int col = xInterval[row - bottom][0]; col < widthBound; col++) {
                world[col][row] = TETile.colorVariant(teTile, 255, 255, 255, new Random());
            }
        }

    }
    /*
     return an 2-D array ret[][].
     ret[i][0] indicates where the start of the i-th row (bottom is the 0-th row)
      and ret[i][1] the end (+1).
     */
    private static int[][] xIntervalOfDraw(Position lowerLeft, int size) {
        int[][] ret = new int[size * 2][2];
        for (int i = 0; i < size; i++) {
            ret[i][0] = lowerLeft.x + size - 1 - i;
            ret[i][1] = lowerLeft.x + size * 2 - 1 + i;
        }
        for (int i = size * 2 - 1; i >= size; i--) {
            ret[i][0] = lowerLeft.x - size + i;
            ret[i][1] = lowerLeft.x + size * 4 - 2 - i;
        }
        return ret;
    }

    static class Position {
        int x;
        int y;
        Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(60, 30);

        TETile[][] world = new TETile[60][30];
        for (int x = 0; x < 60; x += 1) {
            for (int y = 0; y < 30; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        //addHexagon(world, new Position(50, 20), 5, Tileset.FLOWER);
        tesselationOfHexagon(world, 4);

        ter.renderFrame(world);
    }
}
