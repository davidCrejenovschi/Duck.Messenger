package entities.users;

public class SwimmingDuck extends Duck{

    public SwimmingDuck(Builder builder) {
        super(builder, DuckType.SWIMMER);
    }

    public static class Builder extends Duck.Builder<Builder>{

        @Override
        protected Builder self() {
            return this;
        }

        public SwimmingDuck build() {
            return new SwimmingDuck(this);
        }
    }

    @Override
    public void login() {}

    @Override
    public void logout() {

    }

}
