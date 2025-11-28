package entities.users.ducks;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import entities.users.ducks.interfaces.Swimmer;

public class SwimmingDuck extends Duck implements Swimmer {

    public SwimmingDuck(long id_init,
                        String username_init,
                        String password_init,
                        String email_init,
                        double speed_init,
                        double stamina_init) {
        super(id_init, username_init, password_init, email_init, DuckType.SWIMMER, speed_init, stamina_init);
    }

    @Override
    public double swim(int laneLength) {
        return 2*laneLength/speed;
    }
}
