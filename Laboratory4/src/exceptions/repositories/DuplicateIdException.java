package exceptions.repositories;

public class DuplicateIdException extends RepositoryException {

    public DuplicateIdException(String message) {
        super(message);
    }
    public DuplicateIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
