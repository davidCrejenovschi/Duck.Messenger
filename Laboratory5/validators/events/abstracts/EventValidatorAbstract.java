package validators.events.abstracts;
import entities.events.abstracts.Event;
import exceptions.validators.EventValidationException;
import validators.events.interfaces.EventValidatorInterface;

public abstract class EventValidatorAbstract<T extends Event> implements EventValidatorInterface<T> {

    @Override
    public abstract void validate(T entity) throws EventValidationException;

    @Override
    public abstract void validateId(long id) throws EventValidationException;

    @Override
    public void validateName(String name) throws EventValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new EventValidationException("Event name cannot be null or empty");
        }
    }

}
