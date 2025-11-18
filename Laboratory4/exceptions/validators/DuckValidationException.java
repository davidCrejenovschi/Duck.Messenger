package exceptions.validators;

public class DuckValidationException extends UserValidationException {

    public DuckValidationException(String message) {
        super(message);
    }

    public DuckValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
