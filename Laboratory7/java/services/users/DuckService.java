package services.users;
import entities.dtos.users.ducks.DuckFilterDTO;
import entities.users.ducks.abstracts.Duck;
import entities.dtos.users.ducks.DuckDTO;
import entities.users.ducks.FlyingDuck;
import entities.users.ducks.SwimmingDuck;
import entities.utils.Page;
import entities.utils.Pageable;
import exceptions.validators.UserValidationException;
import repository.abstracts.AbstractMemoryBridgeRepository;
import repository.for_data_base.paginig.DuckDBPagingRepository;
import services.users.abstracts.UserServiceAbstract;
import validators.users.DuckValidator;


public class DuckService extends UserServiceAbstract<Duck, DuckDTO, DuckValidator> {

    public DuckService (AbstractMemoryBridgeRepository<Duck> repository, DuckValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addUser(DuckDTO dto) throws UserValidationException {

        Duck duck = createDuckFromDTO(dto);

        validator.validateUser(duck);
        repository.add(duck);
        repository.saveChangesToExternMemory();

    }

    private Duck createDuckFromDTO(DuckDTO dto) {
        return switch (dto.duckType().orElseThrow(() ->
                new IllegalArgumentException("Duck type is missing"))) {
            case SWIMMER -> new SwimmingDuck(
                    dto.id().orElseThrow(() -> new IllegalArgumentException("ID is missing")),
                    dto.username().orElseThrow(() -> new IllegalArgumentException("Username is missing")),
                    dto.password().orElseThrow(() -> new IllegalArgumentException("Password is missing")),
                    dto.email().orElseThrow(() -> new IllegalArgumentException("Email is missing")),
                    dto.speed().orElseThrow(() -> new IllegalArgumentException("Speed is missing")),
                    dto.stamina().orElseThrow(() -> new IllegalArgumentException("Stamina is missing"))
            );
            case FLYER -> new FlyingDuck(
                    dto.id().orElseThrow(() -> new IllegalArgumentException("ID is missing")),
                    dto.username().orElseThrow(() -> new IllegalArgumentException("Username is missing")),
                    dto.password().orElseThrow(() -> new IllegalArgumentException("Password is missing")),
                    dto.email().orElseThrow(() -> new IllegalArgumentException("Email is missing")),
                    dto.speed().orElseThrow(() -> new IllegalArgumentException("Speed is missing")),
                    dto.stamina().orElseThrow(() -> new IllegalArgumentException("Stamina is missing"))
            );
        };
    }

    public Page<Duck> getDucksOnPage(Pageable pageable, DuckFilterDTO filter) {

        if (!(repository instanceof DuckDBPagingRepository pagingRepo)) {
            throw new UnsupportedOperationException( "Repository does not support pagination" );}

        return pagingRepo.findAllOnPage(pageable, filter); }

}
