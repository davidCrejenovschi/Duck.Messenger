package entities.events.interfaces;

import entities.users.ducks.abstracts.Duck;

import java.util.Collection;

public interface Observable {

    double startEvent(Collection<Duck> subscribes);
}
