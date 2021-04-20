package creatures;

import edu.princeton.cs.algs4.StdRandom;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a motile pacifist photosynthesizer.
 *
 * @author Josh Hug
 */
public class Plip extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates plip with energy equal to E.
     */
    public Plip(double e) {
        super("plip");
        r = 99;
        g = 76;
        b = 0;
        energy = e;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Plip() {
        this(1);
    }


    /**
     * Should return a color with red = 99, blue = 76, and green that varies
     * linearly based on the energy of the Plip. If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255. The green value should vary with energy
     * linearly in between these two extremes. It's not absolutely vital
     * that you get this exactly correct.
     */
    public Color color() {
        return color(r, (int) (63 + 96 * energy), b);
    }

    /**
     * Do nothing with C, Plips are pacifists.
     */
    public void attack(Creature c) {
        // do nothing.
    }

    /**
     * Plips should lose 0.15 units of energy when moving. If you want to
     * to avoid the magic number warning, you'll need to make a
     * private static final variable. This is not required for this lab.
     */
    public void move() {
        // TODO
        energy -= 0.15;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * Plips gain 0.2 energy when staying due to photosynthesis.
     */
    public void stay() {
        // TODO
        energy += 0.2;
        if (energy > 2) {
            energy = 2;
        }
    }

    /**
     * Plips and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Plip.
     */
    public Plip replicate() {
        energy *= 0.5;
        return new Plip(energy);
    }

    /**
     * Plips take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability,
     * towards an empty direction chosen at random.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        boolean anyClorus = false;
        Action action = new Action(Action.ActionType.STAY);

        // TODO
        // (Google: Enhanced for-loop over keys of NEIGHBORS?)
        for (Map.Entry<Direction, Occupant> entry: neighbors.entrySet()) {
            if (entry.getValue().name().equals("empty")) {
                emptyNeighbors.addLast(entry.getKey());
            } else if (entry.getValue().name().equals("clorus")) {
                anyClorus = true;
            }
        }
        // Rule 1 end
        // HINT: randomEntry(emptyNeighbors)
        if (!emptyNeighbors.isEmpty()) { // get direction
            int ranIndex = StdRandom.uniform(6);
            Direction dir;
            int choices = emptyNeighbors.size();
            if (choices == 3) {
                if (ranIndex < 2) {
                    dir = emptyNeighbors.getFirst();
                } else if (ranIndex < 4) {
                    emptyNeighbors.removeFirst();
                    dir = emptyNeighbors.getFirst();
                } else {
                    dir = emptyNeighbors.getLast();
                }
            } else {
                if (ranIndex < 3) {
                    dir = emptyNeighbors.getFirst();
                } else {
                    dir = emptyNeighbors.getLast();
                }
            }
            // Rule 2
            if (energy() >= 1.0) {
                action = new Action(Action.ActionType.REPLICATE, dir);
            } else if (anyClorus) { // Rule 3
                if (StdRandom.uniform(2) == 0) {
                    action = new Action(Action.ActionType.MOVE, dir);
                }
            }
        }

        // Rule 4
        return action;
    }
}
