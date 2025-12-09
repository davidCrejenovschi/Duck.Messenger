package entities.dtos.users.interfaces;

import java.util.Optional;

public interface UserDTOI {

    Optional<Long> id();

    Optional<String> username();

    Optional<String> password();

    Optional<String> email();
}
