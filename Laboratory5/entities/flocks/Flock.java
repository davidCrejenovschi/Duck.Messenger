package entities.flocks;
import entities.flocks.abstracts.FlockAbstract;
import entities.flocks.enums.FlockInterest;
import entities.pairs.DoublePair;
import entities.users.ducks.abstracts.Duck;

import java.util.Collection;

public class Flock extends FlockAbstract {

    public Flock(long id, FlockInterest interest, String name) {
        super(id, interest, name);
    }

    public DoublePair getFlockPerformance(Collection<Duck> members) {
        if (members.isEmpty()) return new DoublePair();
        DoublePair pair = new DoublePair();
        pair.left = getFlockSpeed(members);
        pair.right = getFlockStamina(members);
        return pair;
    }

     private double getFlockStamina(Collection<Duck> members) {
        double sum = 0.0;
        for (Duck duck : members) {
            sum += duck.getStamina();
        }
        return sum / members.size();
    }

    private double getFlockSpeed(Collection<Duck> members) {
        double sum = 0.0;
        for (Duck duck : members) {
            sum += duck.getSpeed();
        }
        return sum / members.size();
    }
}
