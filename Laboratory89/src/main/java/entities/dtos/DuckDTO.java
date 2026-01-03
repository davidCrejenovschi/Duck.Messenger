package entities.dtos;
import java.util.Optional;

public class DuckDTO extends UserDTO {

    private String duckType;
    private Double speed;
    private Double stamina;

    public Optional<String> duckType() {
        return Optional.ofNullable(duckType);
    }
    public void setDuckType(String duckType) {
        this.duckType = duckType;
    }

    public Optional<Double> speed() {
        return Optional.ofNullable(speed);
    }
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Optional<Double> stamina() {
        return Optional.ofNullable(stamina);
    }
    public void setStamina(Double stamina) {
        this.stamina = stamina;
    }

}
