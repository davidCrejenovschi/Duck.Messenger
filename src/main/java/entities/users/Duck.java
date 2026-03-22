package entities.users;

public abstract class Duck extends AbstractUser {

    protected DuckType duck_type;
    protected double speed;
    protected double stamina;

    protected Duck(Builder<?> builder, DuckType duck_type) {
        super(builder);
        this.duck_type = duck_type;
        speed = builder.speed;
        stamina = builder.stamina;
    }

    public void setDuckType(DuckType duckType) {
        this.duck_type = duckType;
    }

    public DuckType getDuckType() {
        return duck_type;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getStamina() {
        return stamina;
    }

    protected abstract static class Builder<T extends Builder<T>> extends AbstractUser.Builder<T> {

        private double speed;
        private double stamina;

        public T speed(double speed) {
            this.speed = speed;
            return self();
        }

        public T stamina(double stamina) {
            this.stamina = stamina;
            return self();
        }

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", type=" + duck_type +
                ", speed=" + speed +
                ", stamina=" + stamina +
                '}';
    }

}
