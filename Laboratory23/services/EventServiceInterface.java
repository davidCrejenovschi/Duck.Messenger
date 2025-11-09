package services;
import entities.events.Event;
import entities.events.EventDTO;
import exceptions.validators.EventValidationException;

public interface EventServiceInterface<T extends Event, D extends EventDTO> {

    void addEvent(D eventDTO) throws EventValidationException;

    void deleteEvent(long id) throws EventValidationException;

    T getEventById(long id) throws EventValidationException;

    void notifySubscribers(long eventId);
}
