package entities.events;
import entities.events.abstracts.Event;
import entities.users.ducks.SwimmingDuck;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import java.util.*;

public class RaceEvent extends Event {

    private final int M;
    private int[] lanes = new int[0];
    private SwimmingDuck[] chosenParticipants = new SwimmingDuck[0];
    private double raceTime = -1;

    public RaceEvent(long id, String name, int M) {
        super(id, name);
        this.M = M;
    }

    public int getM() {
        return M;
    }

    public void generateLanes() {
        lanes = new int[M];
        Random rand = new Random();
        int current = 2;
        if (M > 0) lanes[0] = current;
        for (int i = 1; i < M; i++) {
            int delta = rand.nextInt(4);
            current += delta;
            lanes[i] = current;
        }
    }

    public void assignParticipants(Duck[] allSubscribes) {
        List<SwimmingDuck> swimmers = new ArrayList<>();
        if (allSubscribes != null) {
            for (Duck d : allSubscribes) {
                if (d != null && d.getDuckType() == DuckType.SWIMMER) swimmers.add((SwimmingDuck) d);
            }
        }
        if (swimmers.size() < M) throw new RuntimeException("Not enough swimmers");
        swimmers.sort(Comparator.comparingDouble(SwimmingDuck::getStamina)
                .thenComparing((SwimmingDuck a, SwimmingDuck b) -> Double.compare(b.getSpeed(), a.getSpeed())));
        SwimmingDuck[] sorted = swimmers.toArray(new SwimmingDuck[0]);
        double maxDist = 0.0;
        for (int dist : lanes) if (dist > maxDist) maxDist = dist;
        double minSpeed = Double.POSITIVE_INFINITY;
        for (SwimmingDuck s : sorted) if (s.getSpeed() < minSpeed) minSpeed = s.getSpeed();
        double low = 0.0;
        double high = 2.0 * maxDist / minSpeed;
        double eps = 1e-4;
        SwimmingDuck[] temp = new SwimmingDuck[M];
        while (high - low > eps) {
            double mid = (low + high) / 2.0;
            if (canFinish(mid, sorted, lanes, temp)) high = mid; else low = mid;
        }
        SwimmingDuck[] finalChosen = new SwimmingDuck[M];
        if (!canFinish(high, sorted, lanes, finalChosen)) throw new RuntimeException("No feasible assignment found");
        this.chosenParticipants = finalChosen;
    }

    private boolean canFinish(double time, SwimmingDuck[] sortedByStamina, int[] laneDistances, SwimmingDuck[] outChosen) {
        if (time <= 0.0) return false;
        int currLane = 0;
        SwimmingDuck[] temp = new SwimmingDuck[laneDistances.length];
        for (int i = 0; i < sortedByStamina.length && currLane < laneDistances.length; i++) {
            SwimmingDuck d = sortedByStamina[i];
            int dist = laneDistances[currLane];
            double needed = 2.0 * dist / time;
            if (d.getSpeed() >= needed) {
                temp[currLane] = d;
                currLane++;
            }
        }
        if (currLane == laneDistances.length) {
            if (outChosen != null) System.arraycopy(temp, 0, outChosen, 0, Math.min(outChosen.length, temp.length));
            return true;
        }
        return false;
    }

    @Override
    public double startEvent(Collection<Duck> subscribes) {

        generateLanes();
        assignParticipants(subscribes.toArray(new Duck[0]));

        boolean insufficient = false;
        for (int i = 0; i < M; i++) {
            if (chosenParticipants == null || i >= chosenParticipants.length || chosenParticipants[i] == null) {
                insufficient = true;
                break;
            }
        }

        if (insufficient) {

            throw new RuntimeException("There is no enough swimming ducks subscribed to start the race");
        }


        for(int i=0; i<M; i++){ raceTime = Math.max(chosenParticipants[i].swim(lanes[i]),raceTime); }
        return raceTime;
    }

    public SwimmingDuck[] getChosenParticipantsFromLastRace() {
        return chosenParticipants;
    }
    public int[] getLanesFromLastRance() {
        return lanes;
    }
    public double getLastRaceTime() {
        return raceTime;
    }

}
