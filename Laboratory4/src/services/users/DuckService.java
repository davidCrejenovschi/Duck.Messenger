package services.users;
import entities.users.ducks.abstracts.Duck;
import entities.dtos.users.DuckDTO;
import entities.users.ducks.FlyingDuck;
import entities.users.ducks.SwimmingDuck;
import exceptions.validators.UserValidationException;
import repository.abstracts.AbstractRepository;
import services.users.abstracts.UserServiceAbstract;
import validators.users.DuckValidator;


public class DuckService extends UserServiceAbstract<Duck, DuckDTO, DuckValidator> {

    public DuckService (AbstractRepository<Duck> repository, DuckValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addUser(DuckDTO dto) throws UserValidationException {

        Duck duck = createDuckFromDTO(dto);

        validator.validateUser(duck);
        repository.add(duck);

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

}
