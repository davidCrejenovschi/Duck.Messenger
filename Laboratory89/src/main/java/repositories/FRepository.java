package repositories;
import entities.friendships.FriendshipStatus;
import entities.friendships.UserFriendship;
import java.util.Set;

public interface FRepository extends Repository<UserFriendship> {
    Set<UserFriendship> getNonApprovedFriendshipsForUser(Long userId);
    void updateFriendshipStatus(long id, FriendshipStatus status);
}
