package entities.users.ducks.abstracts;
import entities.users.ducks.enums.DuckType;
import entities.users.abstracts.User;

public abstract class Duck extends User {

    protected DuckType duckType;
    protected double speed;
    protected double stamina;
    protected long flock = -1;

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

    public void setFlock(long flock) {
        this.flock = flock;
    }
    public long getFlock() {
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

    @Override
    public String toString(){
        return "Stamina: "+getStamina()+" Speed:"+getSpeed();
    }

}
