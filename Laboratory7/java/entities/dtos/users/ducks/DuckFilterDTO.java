package entities.dtos.users.ducks;
import entities.users.ducks.enums.DuckType;
import java.util.Optional;

public class DuckFilterDTO {

    public enum Comparison {
        EQ,
        GT,
        LT
    }

    private Long id;
    private String username;
    private String password;
    private String email;
    private DuckType duckType;
    private Double speed;
    private Double stamina;

    private Comparison speedComparison;
    private Comparison staminaComparison;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<DuckType> getDuckType() {
        return Optional.ofNullable(duckType);
    }

    public void setDuckType(DuckType duckType) {
        this.duckType = duckType;
    }

    public Optional<Double> getSpeed() {
        return Optional.ofNullable(speed);
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Optional<Double> getStamina() {
        return Optional.ofNullable(stamina);
    }

    public void setStamina(Double stamina) {
        this.stamina = stamina;
    }

    public Optional<Comparison> getSpeedComparison() {
        return Optional.ofNullable(speedComparison);
    }

    public void setSpeedComparison(Comparison speedComparison) {
        this.speedComparison = speedComparison;
    }

    public Optional<Comparison> getStaminaComparison() {
        return Optional.ofNullable(staminaComparison);
    }

    public void setStaminaComparison(Comparison staminaComparison) {
        this.staminaComparison = staminaComparison;
    }
}
