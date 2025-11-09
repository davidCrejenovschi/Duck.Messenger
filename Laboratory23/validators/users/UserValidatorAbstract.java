package validators.users;

import exceptions.validators.UserValidationException;

public abstract class UserValidatorAbstract<T> implements UserValidatorInterface<T> {

    @Override
    public abstract void validateUser(T user) throws UserValidationException;

    public abstract void validateId(long id) throws UserValidationException;

    public void validateUsername(String username) throws UserValidationException {
        if (username == null) {
            throw new UserValidationException("Username cannot be null.");
        }
        if (username.isBlank()) {
            throw new UserValidationException("Username cannot be empty.");
        }
        if (username.length() < 3) {
            throw new UserValidationException("Username must have at least 3 characters.");
        }
    }

    public void validatePassword(String password) throws UserValidationException {
        if (password == null) {
            throw new UserValidationException("Password cannot be null.");
        }
        if (password.isBlank()) {
            throw new UserValidationException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new UserValidationException("Password must have at least 6 characters.");
        }
    }

    public void validateEmail(String email) throws UserValidationException {
        if (email == null) {
            throw new UserValidationException("Email cannot be null.");
        }
        if (email.isBlank()) {
            throw new UserValidationException("Email cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new UserValidationException("Email must be in a valid format (example: duck@mail.com).");
        }
    }

}
