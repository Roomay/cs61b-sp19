package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Test the clorus class
 * @author roomay
 */

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.001);
        c.move();
        assertEquals(1.94, c.energy(), 0.001);
        c.stay();
        assertEquals(1.95, c.energy(), 0.001);
        c.stay();
        assertEquals(1.96, c.energy(), 0.001);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus rec = c.replicate();
        assertEquals(c.energy(), 1, 0.01);
        assertEquals(rec.energy(), 1, 0.01);
        assertNotSame(c, rec);
    }

    @Test
    public void testChoose() {
        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // both empty adjacent spaces and adjacent plips accessible; attack a plip nearby.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> topEmptyLeftPlip = new HashMap<Direction, Occupant>();
        topEmptyLeftPlip.put(Direction.TOP, new Empty());
        topEmptyLeftPlip.put(Direction.BOTTOM, new Impassible());
        topEmptyLeftPlip.put(Direction.LEFT, new Plip());
        topEmptyLeftPlip.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmptyLeftPlip);
        expected = new Action(Action.ActionType.ATTACK, Direction.LEFT);

        assertEquals(expected, actual);

        // Energy >= 1 and no plips nearby; replicate towards an empty space.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> rightEmpty = new HashMap<Direction, Occupant>();
        rightEmpty.put(Direction.TOP, new Impassible());
        rightEmpty.put(Direction.BOTTOM, new Impassible());
        rightEmpty.put(Direction.LEFT, new Impassible());
        rightEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(rightEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(expected, actual);


        // Energy < 1 and no plips nearby; move.
        c = new Clorus(.99);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.TOP);

        assertEquals(expected, actual);
    }

    @Test
    public void testAttack() {
        // The attacker (the clorus) gains the victim's energy.
        Clorus c1 = new Clorus(1.2);
        Clorus c2 = new Clorus(1.2);
        Plip p = new Plip(1.2);

        c2.attack(p);

        assertEquals(2.4, c2.energy(), 0.01);

        c1.attack(c2);

        assertEquals(3.6, c1.energy(), 0.01);
    }
}
