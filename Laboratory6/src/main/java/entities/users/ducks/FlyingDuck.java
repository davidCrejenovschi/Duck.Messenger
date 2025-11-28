package entities.users.ducks;

import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import entities.users.ducks.interfaces.Flyer;

public class FlyingDuck extends Duck implements Flyer {

    public FlyingDuck(long id_init,
                      String username_init,
                      String password_init,
                      String email_init,
                      double speed_init,
                      double stamina_init) {
        super(id_init, username_init, password_init, email_init, DuckType.FLYER, speed_init, stamina_init);
    }

    @Override
    public void fly() {

    }
}
