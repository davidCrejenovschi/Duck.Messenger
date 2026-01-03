package validators;
import exceptions.ValidationException;


public abstract class AbstractUserValidator {

    public String validateUsername(String username) throws ValidationException {
        if (username == null) {
            throw new ValidationException("Username cannot be null.");
        }
        if (username.isBlank()) {
            throw new ValidationException("Username cannot be empty.");
        }
        if (username.length() < 3) {
            throw new ValidationException("Username must have at least 3 characters.");
        }
        return username;
    }

    public String validatePassword(String password) throws ValidationException {
        if (password == null) {
            throw new ValidationException("Password cannot be null.");
        }
        if (password.isBlank()) {
            throw new ValidationException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new ValidationException("Password must have at least 6 characters.");
        }
        return password;
    }

    public String validateEmail(String email) throws ValidationException {
        if (email == null) {
            throw new ValidationException("Email cannot be null.");
        }
        if (email.isBlank()) {
            throw new ValidationException("Email cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new ValidationException("Email must be in a valid format (example: duck@mail.com).");
        }
        return email;
    }
}
