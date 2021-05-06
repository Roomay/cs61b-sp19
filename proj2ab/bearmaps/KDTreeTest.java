package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class KDTreeTest {
    @Test
    public void testKDTByNPS() {
        Random rd = new Random(1);

        for (int i = 0; i < 100; i++) {
            ArrayList<Point> list = new ArrayList<>();
            for (int j = 0; j < 100000; j++) {
                list.add(new Point(rd.nextDouble(), rd.nextDouble()));
            }

            KDTree kdTree = new KDTree(list);
            NaivePointSet npSet = new NaivePointSet(list);

            double testX = rd.nextDouble();
            double textY = rd.nextDouble();

            Point retKDT = kdTree.nearest(testX, textY);
            Point retNPSet = npSet.nearest(testX, textY);

            assertTrue(retNPSet.equals(retKDT));
        }
    }

    @Test
    public void testKDTTime() {
        Random rd = new Random(1);

        long startKDT = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            ArrayList<Point> list = new ArrayList<>();
            for (int j = 0; j < 100000; j++) {
                list.add(new Point(rd.nextDouble(), rd.nextDouble()));
            }

            KDTree kdTree = new KDTree(list);

            for (int j = 0; j < 10000; j++) {
                double testX = rd.nextDouble();
                double textY = rd.nextDouble();
                Point retKDT = kdTree.nearest(testX, textY);
            }

        }
        long endKDT = System.currentTimeMillis();
        System.out.println("Total KDT time elapsed: " + (endKDT - startKDT) / 1000.0 + " seconds.");
    }

    @Test
    public void testNPSTime() {
        Random rd = new Random(1);

        long startNPS = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            ArrayList<Point> list = new ArrayList<>();
            for (int j = 0; j < 100000; j++) {
                list.add(new Point(rd.nextDouble(), rd.nextDouble()));
            }

            NaivePointSet npSet = new NaivePointSet(list);

            for (int j = 0; j < 10000; j++) {
                double testX = rd.nextDouble();
                double textY = rd.nextDouble();
                Point retNPSet = npSet.nearest(testX, textY);
            }
        }
        long endNPS = System.currentTimeMillis();
        System.out.println("Total NaivePointSet time elapsed: " + (endNPS - startNPS) / 1000.0 + " seconds.");
    }
}
