package validators;
import entities.dtos.FriendshipDTO;
import entities.friendships.UserFriendship;
import entities.users.AbstractUser;
import exceptions.ValidationException;

public class FriendshipValidator {

    public UserFriendship validate(FriendshipDTO friendshipDTO) throws ValidationException {
        AbstractUser sender = validateUserNotNull(friendshipDTO.sender());
        AbstractUser receiver = validateUserNotNull(friendshipDTO.receiver());
        return new UserFriendship(sender, receiver);
    }

    private AbstractUser validateUserNotNull(AbstractUser user) throws ValidationException {

        if (user == null) {
            throw new ValidationException("The sender or the receiver user cannot be null.");
        }
        return user;
    }

}