package repository;

import entities.events.Event;

public abstract class EventRepository<T extends Event> extends AbstractFileRepository<T> {

    @Override
    protected long getId(T item) {
        return item.getId();
    }

    @Override
    public abstract void readFromFile();

    @Override
    public abstract void writeToFile();
}
