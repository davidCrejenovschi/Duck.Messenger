package entities.ducks;
import entities.users.UserDTO;


public class DuckDTO extends UserDTO {

    private final DuckType duckType;
    private final Double speed;
    private final Double stamina;

    public DuckDTO(long id, String username, String password, String email, DuckType duckType, Double speed, Double stamina) {
        super(id, username, password, email);
        this.duckType = duckType;
        this.speed = speed;
        this.stamina = stamina;
    }

    public DuckDTO(UserDTO dto, DuckType duckType, Double speed, Double stamina){
        super(dto.id(), dto.username(), dto.password(), dto.email());
        this.duckType = duckType;
        this.speed = speed;
        this.stamina = stamina;
    }

    public DuckType duckType(){
        return duckType;
    }
    public Double speed(){
        return speed;
    }
    public Double stamina(){
        return stamina;
    }
}