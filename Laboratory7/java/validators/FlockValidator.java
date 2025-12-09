package validators;
import entities.flocks.Flock;
import exceptions.validators.FlockValidationException;
import java.util.ArrayList;
import java.util.List;

public class FlockValidator {

    public void validateFlock(Flock flock) throws FlockValidationException {
        List<String> errors = new ArrayList<>();

        try {
            validateId(flock.getId());
        } catch (FlockValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateName(flock.getName());
        } catch (FlockValidationException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new FlockValidationException(String.join(" ", errors));
        }
    }

    public void validateId(long id) throws FlockValidationException {
        if (id <= 0) {
            throw new FlockValidationException("Flock ID must be a positive number.");
        }
        String idStr = Long.toString(id);
        if (!idStr.endsWith("02")) {
            throw new FlockValidationException("Flock ID must end with '02' (examples: 102, 502, 1202).");
        }
    }

    public void validateName(String name) throws FlockValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new FlockValidationException("Flock name cannot be null or empty.");
        }
    }

}
