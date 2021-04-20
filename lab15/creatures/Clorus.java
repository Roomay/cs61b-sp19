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
public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c) {
        energy += c.energy();
    }

    public void move() {
        energy -= 0.03;
        if (energy < 0) {
            energy = 0;
        }
    }

    public void stay() {
        energy -= 0.01;
        if (energy < 0) {
            energy = 0;
        }
    }
    public Clorus replicate() {
        energy *= 0.5;
        return new Clorus(energy);
    }
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();
        boolean anyPlip = false;
        Action action = new Action(Action.ActionType.STAY);
        for (Map.Entry<Direction, Occupant> entry : neighbors.entrySet()) {
            if (entry.getValue().name().equals("empty")) {
                emptyNeighbors.addLast(entry.getKey());
            } else if (entry.getValue().name().equals("plip")) {
                anyPlip = true;
                plipNeighbors.addLast(entry.getKey());
            }
        }

        if (!emptyNeighbors.isEmpty()) { // Rule 2
            Direction dir;
            Direction[] dirDrawer;
            if (anyPlip) {
                int ranPlipIndex = StdRandom.uniform(plipNeighbors.size());
                dirDrawer = plipNeighbors.toArray(new Direction[plipNeighbors.size()]);
                dir = dirDrawer[ranPlipIndex];
                action = new Action(Action.ActionType.ATTACK, dir);
            } else {
                int ranEmptyIndex = StdRandom.uniform(emptyNeighbors.size());
                dirDrawer = emptyNeighbors.toArray(new Direction[emptyNeighbors.size()]);
                dir = dirDrawer[ranEmptyIndex];
                // Rule 3
                if (energy() >= 1.0) {
                    action = new Action(Action.ActionType.REPLICATE, dir);
                } /** Rule 4 */ else {
                    action = new Action(Action.ActionType.MOVE, dir);
                }
            }
        }
        return action;
    }
}

