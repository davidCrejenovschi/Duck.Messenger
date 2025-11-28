package services.users.interfaces;
import entities.dtos.users.abstracts.UserDTO;
import exceptions.validators.UserValidationException;

import java.util.Collection;

public interface UserServiceInterface<T, D extends UserDTO> {
    void addUser(D dto) throws UserValidationException;
    void deleteUser(long id) throws UserValidationException;
    Collection<T> getAllUsers();
    T getUser(long id) throws UserValidationException;
    void existsUser(long id) throws UserValidationException;
}

