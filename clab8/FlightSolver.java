import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    private PriorityQueue<Flight> toTakeOffPQ;
    private PriorityQueue<Flight> toLandPQ;

    public FlightSolver(ArrayList<Flight> flights) {
        /* FIX ME */

        Comparator<Flight> minStartComp = (f1, f2) -> (f1.startTime() - f2.startTime());
        Comparator<Flight> minEndComp = (f1, f2) -> (f1.endTime() - f2.endTime());
        toTakeOffPQ = new PriorityQueue<>(flights.size(), minStartComp);
        toLandPQ = new PriorityQueue<>(flights.size(), minEndComp);
        for (Flight flight
                :
                flights) {
            toTakeOffPQ.add(flight);
            toLandPQ.add(flight);
        }
    }


    public int solve() {
        /* FIX ME */
            int maxPsgrAO = 0;
            int curPsgr = 0;

        while (toTakeOffPQ.size() > 0 && toLandPQ.size() > 0) {
            int startTime = toTakeOffPQ.peek().startTime();
            int endTime = toLandPQ.peek().endTime();

            if (startTime <= endTime) {
                Flight f = toTakeOffPQ.remove();
                curPsgr += f.passengers();
            } else {
                Flight f = toLandPQ.remove();
                curPsgr -= f.passengers();
            }

            maxPsgrAO = (curPsgr > maxPsgrAO) ? curPsgr: maxPsgrAO;
        }
        return maxPsgrAO;
    }

}
