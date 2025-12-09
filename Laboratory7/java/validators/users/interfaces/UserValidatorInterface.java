package validators.users.interfaces;
import exceptions.validators.UserValidationException;

public interface UserValidatorInterface<T> {

    void validateUser(T user) throws UserValidationException;

}
