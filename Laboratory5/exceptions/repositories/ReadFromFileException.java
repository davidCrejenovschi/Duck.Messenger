package exceptions.repositories;

public class ReadFromFileException extends RepositoryException {

    public ReadFromFileException(String message) {
        super(message);
    }
    public ReadFromFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
