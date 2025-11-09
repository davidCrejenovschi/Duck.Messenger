package entities.ducks;

import entities.flocks.Flock;
import entities.users.User;

public abstract class Duck extends User {

    private DuckType duckType;
    private double speed;
    private double stamina;
    private Flock flock = null;

    public Duck(long id_init,
                  String username_init,
                  String password_init,
                  String email_init,
                  DuckType duckType_init,
                  double speed_init,
                  double stamina_init) {
        super(id_init, username_init, password_init, email_init);
        duckType = duckType_init;
        speed = speed_init;
        stamina = stamina_init;
    }

    public void setFlock(Flock flock) {
        this.flock = flock;
    }
    public Flock getFlock() {
        return flock;
    }

    @Override
    public void login(){

    }

    @Override
    public void logout(){

    }

    @Override
    public void sendMessage(){

    }

    @Override
    public void receiveMessage(){
    }

    public double getSpeed(){
        return speed;
    }
    public DuckType getDuckType(){
        return duckType;
    }
    public double getStamina(){
        return stamina;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void setDuckType(DuckType duckType) {
        this.duckType = duckType;
    }
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public void onEventStarted() {

        String flockName = (flock != null) ? flock.getName() : "no flock";
        System.out.println("Hi, I am a " + duckType +
                " duck with speed " + speed +
                " and stamina " + stamina +
                ", from " + flockName + ".");
    }

}
