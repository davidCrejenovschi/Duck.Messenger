package strategies.forrace;
import entities.Duck;
import entities.Lane;

public class GreedyBinarySearchSolver implements RaceSolverStrategy {

    @Override
    public double solve(Duck[] ducks, Lane[] lanes, Duck[] chosenDucks) {
        double st = 0.0;
        double dr = calcUpperBound(ducks, lanes);
        double ans = dr;

        while (dr - st > 0.01) {
            double mid = (st + dr) / 2;
            if (canFinish(mid, ducks, lanes, chosenDucks)) {
                ans = mid;
                dr = mid;
            } else {
                st = mid;
            }
        }
        return ans;
    }

    private double calcUpperBound(Duck[] ducks, Lane[] lanes) {

        double maxDistance = 0;
        for (Lane lane : lanes) {
            maxDistance = Math.max(maxDistance, lane.distanceBuoys());
        }

        int minSpeed = Integer.MAX_VALUE;
        for (Duck d : ducks) {
            minSpeed = Math.min(minSpeed, d.speed());
        }

        return 2.0 * maxDistance / minSpeed;
    }

    private boolean canFinish(double time, Duck[] ducks, Lane[] lanes, Duck[] chosenDucks) {
        int currLane = 0;

        for (int i = 0; i < ducks.length && currLane < lanes.length; i++) {
            double speedNeeded = 2.0 * lanes[currLane].distanceBuoys() / time;

            if (ducks[i].speed() >= speedNeeded) {
                chosenDucks[currLane] = ducks[i];
                currLane++;
            }
        }

        return currLane == lanes.length;
    }
}
