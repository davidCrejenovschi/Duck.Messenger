package validators;
import entities.dtos.DuckDTO;
import entities.users.Duck;
import entities.users.DuckType;
import entities.users.FlyingDuck;
import entities.users.SwimmingDuck;
import exceptions.ValidationException;


public class DuckValidator extends AbstractUserValidator{

    public DuckType validateDuckType(String duckTypeString) throws ValidationException {

        if (duckTypeString == null || duckTypeString.trim().isEmpty()) {
            throw new ValidationException("Duck type string cannot be null or empty.");
        }
        try {
            return DuckType.valueOf(duckTypeString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            String validValues = "FLYER or SWIMMER";
            throw new ValidationException("Invalid duck type: " + duckTypeString + ". Must be one of: " + validValues);
        }
    }

    public double validateSpeed(double speed) throws ValidationException {

        if (speed <= 0) {
            throw new ValidationException("Speed must be greater than 0.");
        }
        if (speed > 200) {
            throw new ValidationException("Speed cannot exceed 200.");
        }
        return speed;
    }

    public double validateStamina(double stamina) throws ValidationException {

        if (stamina <= 0) {
            throw new ValidationException("Stamina must be greater than 0.");
        }
        if (stamina > 100) {
            throw new ValidationException("Stamina cannot exceed 100.");
        }
        return stamina;
    }

    public Duck validateDuck(DuckDTO duckDTO) throws ValidationException {

        if (duckDTO == null) {
            throw new ValidationException("DuckDTO is null");
        }

        String username = validateUsername(duckDTO.username().orElse(null));
        String email = validateEmail(duckDTO.email().orElse(null));
        String password = validatePassword(duckDTO.password().orElse(null));
        double stamina = validateStamina(duckDTO.stamina().orElse(-1.0));
        double speed = validateSpeed(duckDTO.speed().orElse(-1.0));
        DuckType duckType =  validateDuckType(duckDTO.duckType().orElse(null));

        if(duckType == DuckType.FLYER){
            FlyingDuck.Builder builder = new FlyingDuck.Builder();
            builder.username(username);
            builder.password(password);
            builder.stamina(stamina);
            builder.speed(speed);
            builder.email(email);
            return builder.build();
        } else {
            SwimmingDuck.Builder builder = new SwimmingDuck.Builder();
            builder.username(username);
            builder.password(password);
            builder.stamina(stamina);
            builder.speed(speed);
            builder.email(email);
            return builder.build();
        }

    }

}
