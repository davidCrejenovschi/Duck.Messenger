package exceptions.repositories;

public class WriteToFileException extends RepositoryException {
    public WriteToFileException(String message) {
        super(message);
    }
    public WriteToFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
