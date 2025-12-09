package entities.dtos.users.ducks;
import entities.dtos.users.abstracts.UserDTO;
import entities.users.ducks.enums.DuckType;
import java.util.Optional;

public class DuckDTO extends UserDTO {

    private final DuckType duckType;
    private final Double speed;
    private final Double stamina;

    public DuckDTO(
            Long id,
            String username,
            String password,
            String email,
            DuckType duckType,
            Double speed,
            Double stamina
    ) {
        super(id, username, password, email);
        this.duckType = duckType;
        this.speed = speed;
        this.stamina = stamina;
    }

    public Optional<DuckType> duckType() {
        return Optional.ofNullable(duckType);
    }

    public Optional<Double> speed() {
        return Optional.ofNullable(speed);
    }

    public Optional<Double> stamina() {
        return Optional.ofNullable(stamina);
    }
}
