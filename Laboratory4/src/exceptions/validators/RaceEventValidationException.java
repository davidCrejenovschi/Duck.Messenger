package exceptions.validators;

public class RaceEventValidationException extends EventValidationException {

    public RaceEventValidationException(String message) {
        super(message);
    }

    public RaceEventValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
