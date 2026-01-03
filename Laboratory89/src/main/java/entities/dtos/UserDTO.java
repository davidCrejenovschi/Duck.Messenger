package entities.dtos;
import java.util.Optional;

public abstract class UserDTO {

    private Long id;
    private  String username;
    private String password;
    private String email;

    public Optional<Long> id() {
        return Optional.ofNullable(id);
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Optional<String> username() {
        return Optional.ofNullable(username);
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<String> password() {
        return Optional.ofNullable(password);
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<String> email() {
        return Optional.ofNullable(email);
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

