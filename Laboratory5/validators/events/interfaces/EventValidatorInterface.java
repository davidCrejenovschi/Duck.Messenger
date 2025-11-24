package validators.events.interfaces;
import entities.events.abstracts.Event;
import exceptions.validators.EventValidationException;

public interface EventValidatorInterface<T extends Event> {

    void validate(T entity) throws EventValidationException;

    void validateId(long id) throws EventValidationException;

    void validateName(String name) throws EventValidationException;
}
