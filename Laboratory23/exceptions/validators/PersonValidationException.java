package exceptions.validators;

public class PersonValidationException extends UserValidationException {

    public PersonValidationException(String message) {
        super(message);
    }

    public PersonValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

