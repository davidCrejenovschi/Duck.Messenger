package entities.flocks.abstracts;
import entities.flocks.enums.FlockInterest;
import entities.flocks.interfaces.FlockInterface;

import java.util.HashSet;
import java.util.Set;

public abstract class FlockAbstract implements FlockInterface {

    protected final long id;
    protected final FlockInterest interest;
    protected final String name;
    protected final Set<Long> members = new HashSet<>();

    protected FlockAbstract(long id, FlockInterest interest, String name) {
        this.id = id;
        this.interest = interest;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FlockInterest getInterest() {
        return interest;
    }

    @Override
    public Set<Long> getMembers() {
        return members;
    }

    @Override
    public void addMember(long id) {
        members.add(id);
    }

    @Override
    public void removeMember(long id) {
        members.remove(id);
    }

}
