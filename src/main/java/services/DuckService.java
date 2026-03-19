package services;
import entities.dtos.DuckDTO;
import entities.users.Duck;
import exceptions.ValidationException;
import repositories.DuckRepository;
import repositories.URepository;
import validators.DuckValidator;
import java.util.Collection;
import java.util.List;

public class DuckService {

    private final URepository<Duck> duckRepository;
    private final DuckValidator duckValidator;

    public DuckService(DuckRepository duckRepository, DuckValidator duckValidator) {
        this.duckRepository = duckRepository;
        this.duckValidator = duckValidator;
    }

    public void addDuck(DuckDTO duckDTO) throws ValidationException {

        Duck newDuck = duckValidator.validateDuck(duckDTO);
        duckRepository.add(newDuck);
    }

    public void deleteDuck(long id) {
        duckRepository.delete(id);
    }

    public Duck getDuckById(long id) {
        return duckRepository.getById(id);
    }

    public Collection<Duck> getAllDucks() {
        return duckRepository.getAll();
    }

    public Collection<Duck> getDucksByIds(List<Long> ids) {
        return duckRepository.getByIds(ids);
    }

    public Duck findDuckByUsername(String username) {
        return duckRepository.findUserByUsername(username);
    }

}
