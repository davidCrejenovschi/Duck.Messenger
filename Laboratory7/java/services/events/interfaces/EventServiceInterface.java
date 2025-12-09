package services.events.interfaces;
import entities.events.abstracts.Event;
import entities.dtos.events.abstracts.EventDTO;
import exceptions.validators.EventValidationException;


public interface EventServiceInterface<T extends Event, D extends EventDTO> {

    void addEvent(D eventDTO) throws EventValidationException;

    void deleteEvent(long id) throws EventValidationException;

    void addUserToEvent(long id_user, long id_event) throws EventValidationException;

    void deleteUserFromEvent(long id_user, long id_event) throws EventValidationException;

    T getEventById(long id) throws EventValidationException;

}
