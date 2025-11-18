package services.users.abstracts;
import entities.users.abstracts.User;
import entities.dtos.users.abstracts.UserDTO;
import exceptions.validators.UserValidationException;
import repository.abstracts.AbstractRepository;
import services.users.interfaces.UserServiceInterface;
import validators.users.abstracts.UserValidatorAbstract;
import java.util.Collection;
import java.util.List;


public abstract class UserServiceAbstract<T extends User, D extends UserDTO, V extends UserValidatorAbstract<T>> implements UserServiceInterface<T, D> {

    protected final AbstractRepository<T> repository;
    protected final V validator;

    public UserServiceAbstract(AbstractRepository<T> repository, V validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public abstract void addUser(D dto) throws UserValidationException;

    @Override
    public void deleteUser(long id) throws UserValidationException {
        validator.validateId(id);
        repository.delete(id);
    }

    @Override
    public Collection<T> getAllUsers() {
        return repository.getAll();
    }

    public Collection<T> getSpecificUsers(List<Long> ducksIds) throws UserValidationException {
        for(Long id: ducksIds){
            validator.validateId(id);
        }
        return repository.getByIds(ducksIds);
    }

    @Override
    public T getUser(long id) throws UserValidationException {
        validator.validateId(id);
        return repository.getById(id);
    }

    @Override
    public void existsUser(long id) throws UserValidationException {
        validator.validateId(id);
        repository.exists(id);
    }
}
