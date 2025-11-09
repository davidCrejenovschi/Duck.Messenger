package entities.flocks;

import entities.ducks.Duck;
import entities.pairs.DoublePair;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Flock {

    private final long id;
    private final FlockInterest interest;
    private final String name;
    private final Set<Long> members = new HashSet<>();


    public Flock(long id, FlockInterest interest, String name) {
        this.id = id;
        this.interest = interest;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Long> getMembers() {
        return members;
    }

    public FlockInterest getInterest() {
        return interest;
    }

    public DoublePair getFlockPerformance(Collection<Duck> members) {
        if (members.isEmpty()) return new DoublePair();
        DoublePair longPair = new DoublePair();
        longPair.left = getFlockSpeed(members);
        longPair.right = getFlockStamina(members);
        return longPair;
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

    public void addMember(long id) {
        members.add(id);
    }

    public void removeMember(long id) {
        members.remove(id);
    }

}
