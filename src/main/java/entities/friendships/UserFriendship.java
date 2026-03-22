package entities.friendships;
import entities.users.AbstractUser;


public class UserFriendship extends AbstractFiendship<AbstractUser> {

    private Long id;
    FriendshipStatus status;

    public UserFriendship(AbstractUser sender, AbstractUser receiver) {
        this.sender = sender;
        this.receiver = receiver;
        status = FriendshipStatus.PENDING;

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public FriendshipStatus getStatus() {
        return status;
    }
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}