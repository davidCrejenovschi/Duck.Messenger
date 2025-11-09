package services;
import entities.users.User;
import entities.users.UserDTO;
import exceptions.validators.UserValidationException;
import repository.AbstractFileRepository;
import validators.users.UserValidatorAbstract;
import java.util.Collection;


public abstract class UserServiceAbstract<T extends User, D extends UserDTO, V extends UserValidatorAbstract<T>> implements UserServiceInterface<T, D> {

    protected final AbstractFileRepository<T> repository;
    protected final V validator;

    public UserServiceAbstract(AbstractFileRepository<T> repository, V validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public abstract void addUser(D dto) throws UserValidationException;

    @Override
    public void deleteUser(long id) throws UserValidationException {
        validator.validateId(id);
        repository.delete(id);
        repository.writeToFile();
    }

    @Override
    public abstract void updateUser(long id, D dto) throws UserValidationException;

    @Override
    public Collection<T> getAllUsers() {
        return repository.getAll();
    }

    @Override
    public T getUser(long id) {
        return repository.getById(id);
    }
}
