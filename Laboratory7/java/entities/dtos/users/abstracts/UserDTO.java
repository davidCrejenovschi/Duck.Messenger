package entities.dtos.users.abstracts;
import entities.dtos.users.interfaces.UserDTOI;
import java.util.Optional;

public abstract class UserDTO implements UserDTOI {

    private final Long id;
    private final String username;
    private final String password;
    private final String email;

    public UserDTO(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public Optional<Long> id() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<String> username() {
        return Optional.ofNullable(username);
    }

    @Override
    public Optional<String> password() {
        return Optional.ofNullable(password);
    }

    @Override
    public Optional<String> email() {
        return Optional.ofNullable(email);
    }
}

