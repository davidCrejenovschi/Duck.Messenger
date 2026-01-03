package entities.friendships;

public abstract class AbstractFiendship <U> implements Friendship<U> {

    public  U sender;
    public  U receiver;

    @Override
    public U getSender() { return sender; }

    @Override
    public U getReceiver() { return receiver; }
}

