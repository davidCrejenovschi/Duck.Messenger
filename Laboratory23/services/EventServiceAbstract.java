package services;

import entities.events.EventDTO;
import entities.events.Event;
import exceptions.validators.EventValidationException;
import repository.AbstractFileRepository;
import validators.events.EventValidatorAbstract;


public abstract class EventServiceAbstract<T extends Event, D extends EventDTO, V extends EventValidatorAbstract<T>> implements EventServiceInterface<T, D> {

    protected final AbstractFileRepository<T> repository;
    protected final V validator;

    public EventServiceAbstract(AbstractFileRepository<T> repository, V validator) {
        this.repository = repository;
        this.validator = validator;
    }


    @Override
    public abstract void addEvent(D dto) throws EventValidationException;


    @Override
    public void deleteEvent(long id) throws EventValidationException {

        validator.validateId(id);
        repository.delete(id);
        repository.writeToFile();
    }


    @Override
    public T getEventById(long id) throws EventValidationException {

        validator.validateId(id);
        return repository.getById(id);
    }

    @Override
    public void notifySubscribers(long eventId) {
        T event = repository.getById(eventId);
        event.notifySubscribers();
    }
}
