package entities.ducks;

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
    public void swim() {}
}
