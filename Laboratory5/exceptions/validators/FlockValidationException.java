package exceptions.validators;

public class FlockValidationException extends ValidationException {

    public FlockValidationException(String message) {
        super(message);
    }

    public FlockValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
