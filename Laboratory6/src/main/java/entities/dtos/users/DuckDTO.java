package entities.dtos.users;
import entities.dtos.users.abstracts.UserDTO;
import entities.users.ducks.enums.DuckType;


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