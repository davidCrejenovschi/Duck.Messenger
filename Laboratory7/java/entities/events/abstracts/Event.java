package entities.events.abstracts;
import entities.events.interfaces.Observable;
import entities.users.ducks.abstracts.Duck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Event implements Observable {

    long id;
    private final String name;
    private final List<Long> subscribers = new ArrayList<>();

    public Event(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void subscribe(long user_id) {
        subscribers.add(user_id);
    }

    public void unsubscribe(long user_id) {
        subscribers.remove(user_id);
    }

    @Override
    public abstract double startEvent(Collection<Duck> subscribes);

    public List<Long> getSubscribersIds() {
        return subscribers;
    }
    public void setSubscribersIds(List<Long> subscribers) {
        this.subscribers.clear();
        this.subscribers.addAll(subscribers);
    }
    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
}
