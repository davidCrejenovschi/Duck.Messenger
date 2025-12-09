package exceptions.validators;

public class FriendshipValidationException extends ValidationException {

    public FriendshipValidationException(String message) {
        super(message);
    }

    public FriendshipValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
