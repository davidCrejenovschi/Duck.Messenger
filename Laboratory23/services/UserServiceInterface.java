package services;
import entities.users.UserDTO;
import exceptions.validators.UserValidationException;

import java.util.Collection;

public interface UserServiceInterface<T, D extends UserDTO> {
    void addUser(D dto) throws UserValidationException;
    void deleteUser(long id) throws UserValidationException;
    void updateUser(long id, D dto) throws UserValidationException;
    Collection<T> getAllUsers();
    T getUser(long id);
}

