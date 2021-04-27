import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by hug.
 */
public class Experiments {
    public static void experiment1() {
        BST tree = new BST();
        List<Double> averageDepth = new ArrayList<>();
        List<Double> optimalAvgDepth = new ArrayList<>();
        List<Integer> xItemNumbers = new ArrayList<>();
        int x = 1;

        List<Integer> source = IntStream.rangeClosed(1, 5000).boxed().collect(Collectors.toList());
        Collections.shuffle(source);
        for (Integer key :
                source) {
            tree.add(key);
            averageDepth.add(tree.averageDepth());
            optimalAvgDepth.add(ExperimentHelper.optimalAverageDepth(x));
            xItemNumbers.add(x);
            x++;
        }

        XYChart chart = new XYChartBuilder().width(1000).height(800).xAxisTitle("number of items")
                .yAxisTitle("average depth").build();
        chart.addSeries("actual random", xItemNumbers, averageDepth);
        chart.addSeries("optimal", xItemNumbers, optimalAvgDepth);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        BST tree = new BST();
        List<Integer> xOperationNumbers = new ArrayList<>();
        List<Double> averageDepth = new ArrayList<>();
        int x = 1;
        for (int i = 0; i < RandomGenerator.getRandomInt(500); i++) {
            ExperimentHelper.ranInsert(tree);
            averageDepth.add(tree.averageDepth());
            xOperationNumbers.add(x);
            x++;
        }

        for (int i = 0; i < 4500; i++) {
            if (tree.size() == 0 || RandomGenerator.getRandomInt(2) >= 1) {
                ExperimentHelper.ranInsert(tree);
            } else {
                ExperimentHelper.ranDelSuc(tree);
            }
            averageDepth.add(tree.averageDepth());
            xOperationNumbers.add(x);
            x++;
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operations")
                .yAxisTitle("average depth").build();
        chart.addSeries("depth by asymmetric operation", xOperationNumbers, averageDepth);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
        BST tree = new BST();
        List<Integer> xOperationNumbers = new ArrayList<>();
        List<Double> averageDepth = new ArrayList<>();
        int x = 1;
        for (int i = 0; i < RandomGenerator.getRandomInt(500); i++) {
            ExperimentHelper.ranInsert(tree);
            averageDepth.add(tree.averageDepth());
            xOperationNumbers.add(x);
            x++;
        }

        for (int i = 0; i < 4500; i++) {
            if (tree.size() == 0 || RandomGenerator.getRandomInt(2) >= 1) {
                ExperimentHelper.ranInsert(tree);
            } else {
                ExperimentHelper.ranDelRan(tree);
            }
            averageDepth.add(tree.averageDepth());
            xOperationNumbers.add(x);
            x++;
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operations")
                .yAxisTitle("average depth").build();
        chart.addSeries("depth by symmetric operation", xOperationNumbers, averageDepth);

        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        experiment1();
        experiment2();
        experiment3();
    }
}
