package services;
import entities.ducks.*;
import exceptions.validators.UserValidationException;
import repository.DuckRepository;
import validators.users.DuckValidator;


public class DuckService extends UserServiceAbstract<Duck, DuckDTO, DuckValidator> {

    public DuckService ( DuckRepository repository, DuckValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addUser(DuckDTO dto) throws UserValidationException {

        Duck duck = createDuckFromDTO(dto);

        validator.validateUser(duck);
        repository.add(duck);
        repository.writeToFile();
    }
    private Duck createDuckFromDTO(DuckDTO dto) {
        return switch (dto.duckType()) {
            case SWIMMER -> new SwimmingDuck(
                    dto.id(), dto.username(), dto.password(),
                    dto.email(), dto.speed(), dto.stamina()
            );
            case FLYER -> new FlyingDuck(
                    dto.id(), dto.username(), dto.password(),
                    dto.email(), dto.speed(), dto.stamina()
            );
        };
    }


    @Override
    public void updateUser(long id, DuckDTO dto) throws UserValidationException {

        if (!(dto instanceof DuckDTO duckDTO)) {
            throw new UserValidationException("UserDTO is not a DuckDTO");
        }

        Duck existingDuck = repository.getById(id);

        String username = duckDTO.username();
        if (username != null) {
            validator.validateUsername(username);
            existingDuck.setUsername(username);
        }

        String password = duckDTO.password();
        if (password != null) {
            validator.validatePassword(password);
            existingDuck.setPassword(password);
        }

        String email = duckDTO.email();
        if (email != null) {
            validator.validateEmail(email);
            existingDuck.setEmail(email);
        }

        DuckType duckType = duckDTO.duckType();
        if (duckType != null) {
            validator.validateDuckType(duckType);
            existingDuck.setDuckType(duckType);
        }

        Double speed = duckDTO.speed();
        if (speed != null) {
            validator.validateSpeed(speed);
            existingDuck.setSpeed(speed);
        }

        Double stamina = duckDTO.stamina();
        if (stamina != null) {
            validator.validateStamina(stamina);
            existingDuck.setStamina(stamina);
        }

        repository.writeToFile();

    }


}
