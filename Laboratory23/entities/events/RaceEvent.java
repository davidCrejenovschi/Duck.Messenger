package entities.events;
import entities.ducks.Duck;
import entities.ducks.SwimmingDuck;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RaceEvent extends Event {

    private List<SwimmingDuck> participants = new ArrayList<>();
    private final int M;

    public RaceEvent(long id, String name, int M) {
        super(id, name);
        this.M = M;
    }

    public void selectPotentialParticipants(Collection<Duck> subscribes) {

        participants.clear();
        participants = subscribes.stream()
                .filter(u -> u instanceof SwimmingDuck)
                .map(u -> (SwimmingDuck) u)
                .collect(Collectors.toList());
    }

    private List<SwimmingDuck> getEligibleParticipants() {

        return participants.stream()
                .sorted(Comparator.comparingDouble(SwimmingDuck::getSpeed).reversed())
                .limit(M)
                .sorted(Comparator.comparingDouble(SwimmingDuck::getStamina))
                .collect(Collectors.toList());
    }

    @Override
    public void notifySubscribers() {

        participants = getEligibleParticipants();
        if(participants.size() == M){
            System.out.println("The race can start!");
            for(Duck duck : participants){
                duck.onEventStarted();
            }
        } else {
            System.out.println("The race cannot begin!");
        }
    }

    public int getM() {
        return M;
    }
}
