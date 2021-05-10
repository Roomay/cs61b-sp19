package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        distHPQ = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        distHPQ.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        edgeTo.put(start, null);

        Vertex p = null;
        numStatesExplored = 0;
        while (distHPQ.size() > 0 && !end.equals(distHPQ.getSmallest())
                && Double.compare(sw.elapsedTime(), timeout) < 0) {
            p = distHPQ.removeSmallest();
            numStatesExplored += 1;
            for (WeightedEdge<Vertex> e
                    :
                    input.neighbors(p)) {
                relax(e, input, end);
            }
        }

        solution = List.of();
        solutionWeight = 0;
        if (sw.elapsedTime() >= timeout) {
            timeSpent = timeout;
            outcome = SolverOutcome.TIMEOUT;
            return;
        }

        if (distHPQ.size() > 0) {
            p = distHPQ.getSmallest();
        }

        if (end.equals(p)) {
            Vertex thread = end;
            WeightedEdge<Vertex> edge = null;
            solution = new LinkedList<>();
            solution.add(0, thread);
            while (!thread.equals(start)) {
                edge = edgeTo.get(thread);
                solution.add(0, edge.from());
                solutionWeight += edge.weight();
                thread = edge.from();
            }
            outcome = SolverOutcome.SOLVED;
            timeSpent = sw.elapsedTime();
            return;
        }
        outcome = SolverOutcome.UNSOLVABLE;
        timeSpent = sw.elapsedTime();
    }

    private void relax(WeightedEdge<Vertex> e, AStarGraph<Vertex> heuristic, Vertex goal) {
        Vertex f = e.from();
        Vertex t = e.to();
        double w = e.weight();
        double distToF = distTo.getOrDefault(f, Double.POSITIVE_INFINITY);
        double distToT = distTo.getOrDefault(t, Double.POSITIVE_INFINITY);
        double updateDistToT = distToF + w;
        if (Double.compare(updateDistToT, distToT) < 0) {
            distTo.put(t, updateDistToT);
            edgeTo.put(t, e);
            double updatePriority = updateDistToT + heuristic.estimatedDistanceToGoal(t, goal);
            if (distHPQ.contains(t)) {
                distHPQ.changePriority(t, updatePriority);
            } else {
                distHPQ.add(t, updatePriority);
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }

    // Outputs

    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private int numStatesExplored;
    private double timeSpent;

    // Tools
    private ExtrinsicMinPQ<Vertex> distHPQ;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, WeightedEdge<Vertex>> edgeTo;


}
