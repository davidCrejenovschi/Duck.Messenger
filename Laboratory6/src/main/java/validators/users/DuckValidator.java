package validators.users;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import exceptions.validators.DuckValidationException;
import exceptions.validators.UserValidationException;
import validators.users.abstracts.UserValidatorAbstract;

import java.util.ArrayList;
import java.util.List;

public class DuckValidator extends UserValidatorAbstract<Duck> {

    @Override
    public void validateUser(Duck user) throws UserValidationException {


        List<String> errors = new ArrayList<>();

        try {
            validateId(user.getId());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateUsername(user.getUsername());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validatePassword(user.getPassword());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateEmail(user.getEmail());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateDuckType(user.getDuckType());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateSpeed(user.getSpeed());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateStamina(user.getStamina());
        } catch (DuckValidationException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new DuckValidationException(String.join(" ", errors));
        }
    }

    @Override
    public void validateId(long id) throws UserValidationException {

        if (id <= 0) {
            throw new DuckValidationException("Id must be a positive number.");
        }
        String idString = Long.toString(id);
        if (!idString.endsWith("00")) {
            throw new DuckValidationException("Duck Id must end with '00' (examples: 100, 200, 1300).");
        }
    }

    public void validateDuckType(DuckType duckType) throws DuckValidationException {
        if (duckType == null) {
            throw new DuckValidationException("Duck type cannot be null.");
        }
    }

    public void validateSpeed(double speed) throws UserValidationException {
        if (speed <= 0) {
            throw new DuckValidationException("Speed must be greater than 0.");
        }
        if (speed > 200) {
            throw new DuckValidationException("Speed cannot exceed 200.");
        }
    }

    public void validateStamina(double stamina) throws UserValidationException {
        if (stamina <= 0) {
            throw new DuckValidationException("Stamina must be greater than 0.");
        }
        if (stamina > 100) {
            throw new DuckValidationException("Stamina cannot exceed 100.");
        }
    }
}
