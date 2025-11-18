package entities.flocks.interfaces;
import entities.flocks.enums.FlockInterest;
import java.util.Set;

public interface FlockInterface {

    long getId();
    String getName();
    FlockInterest getInterest();
    Set<Long> getMembers();
    void addMember(long id);
    void removeMember(long id);

}
