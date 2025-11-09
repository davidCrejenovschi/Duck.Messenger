package validators.users;
import exceptions.validators.UserValidationException;

public interface UserValidatorInterface<T> {

    public void validateUser(T user) throws UserValidationException;

    public void validateId(long id) throws UserValidationException;

    public void validateUsername(String username) throws UserValidationException;

    public void validatePassword(String password) throws UserValidationException;

    public void validateEmail(String email) throws UserValidationException;

}
