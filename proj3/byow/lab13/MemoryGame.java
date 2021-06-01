package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.rand = new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();


    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[rand.nextInt(26)]);
        }

        return sb.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        if (s != null) {
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
            StdDraw.text(width / 2, height / 2, s);
        }
        if (!gameOver && playerTurn) {
            StdDraw.setFont(new Font("Monaco", Font.HANGING_BASELINE, 28));
            StdDraw.textLeft(0, height - 1, " " + "Round: " + round);
            StdDraw.text(width / 2, height - 1, "Watch!");
            StdDraw.textRight(width, height - 1, ENCOURAGEMENT[rand.nextInt(6)] + " ");
            StdDraw.line(0, height - 2, width, height - 2);
        }

        StdDraw.show();
        //TODO: If game is not over, display relevant game information at the top of the screen
    }

    public void flashSequence(String letters) {
        char[] s = letters.toCharArray();
        for (Character c
                :
             s) {
            drawFrame(c.toString());
            StdDraw.pause(1000);
            drawFrame(null);
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        playerTurn = true;
        StringBuilder sb = new StringBuilder();
        drawFrame(null);
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                sb.append(StdDraw.nextKeyTyped());
                drawFrame(sb.toString());
                n--;
            }
        }
        playerTurn = false;
        return sb.toString();
    }

    public void startGame() {
        gameOver = false;
        round = 0;
        String target;
        String input;

        do {
            drawFrame("Round: " + ++round);
            StdDraw.pause(1500);
            target = generateRandomString(round);
            flashSequence(target);
            input = solicitNCharsInput(round);
        } while (target.equals(input));

        drawFrame("Game Over! You made it to round:" + round);

    }

}
