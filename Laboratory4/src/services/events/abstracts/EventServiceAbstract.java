package services.events.abstracts;
import entities.dtos.events.abstracts.EventDTO;
import entities.events.abstracts.Event;
import exceptions.validators.EventValidationException;
import repository.abstracts.AbstractFileRepository;
import services.events.interfaces.EventServiceInterface;
import validators.events.abstracts.EventValidatorAbstract;


public abstract class EventServiceAbstract<T extends Event, D extends EventDTO, VE extends EventValidatorAbstract<T>> implements EventServiceInterface<T, D> {

    protected final AbstractFileRepository<T> repository;
    protected final VE validatorEvent;

    public EventServiceAbstract(AbstractFileRepository<T> repository, VE validatorEvent) {
        this.repository = repository;
        this.validatorEvent = validatorEvent;
    }

    @Override
    public abstract void addEvent(D dto) throws EventValidationException;

    @Override
    public void deleteEvent(long id) throws EventValidationException {

        validatorEvent.validateId(id);
        repository.delete(id);
        repository.writeToFile();
    }

    @Override
    public void addUserToEvent(long id_user, long id_event) throws EventValidationException {
        validatorEvent.validateId(id_event);
        repository.getById(id_event).subscribe(id_user);
        repository.writeToFile();
    }

    @Override
    public void deleteUserFromEvent(long id_user, long id_event) throws EventValidationException {
        validatorEvent.validateId(id_event);
        repository.getById(id_event).unsubscribe(id_user);
        repository.writeToFile();
    }

    @Override
    public T getEventById(long id) throws EventValidationException {

        validatorEvent.validateId(id);
        return repository.getById(id);
    }

}
