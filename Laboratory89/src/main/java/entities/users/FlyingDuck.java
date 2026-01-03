package entities.users;

public class FlyingDuck extends Duck{

    public FlyingDuck(Builder builder) {
        super(builder, DuckType.FLYER);
    }

    public static class Builder extends Duck.Builder<Builder>{

        @Override
        protected Builder self() {
            return this;
        }

        public FlyingDuck build() {
            return new FlyingDuck(this);
        }
    }

    public void fly(){

    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

}
