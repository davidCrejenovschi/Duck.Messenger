package services;

import entities.dtos.FriendshipDTO;
import entities.friendships.FriendshipStatus;
import entities.friendships.UserFriendship;
import exceptions.ValidationException;
import repositories.FRepository;
import repositories.FriendshipRepository;
import validators.FriendshipValidator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FriendshipService {

    private final FRepository friendshipRepository;
    private final FriendshipValidator friendshipValidator;
    private final ObjectProperty<UserFriendship> friendshipChangeProperty = new SimpleObjectProperty<>();

    public FriendshipService(FriendshipRepository friendshipRepository, FriendshipValidator friendshipValidator) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipValidator = friendshipValidator;
    }

    public ObjectProperty<UserFriendship> getFriendshipChangeProperty() {
        return friendshipChangeProperty;
    }

    public void addFriendship(FriendshipDTO friendshipDTO) throws ValidationException {
        UserFriendship userFriendship = friendshipValidator.validate(friendshipDTO);
        friendshipRepository.add(userFriendship);
        friendshipChangeProperty.set(userFriendship);
    }

    public void updateFriendshipStatus(UserFriendship userFriendship){

        friendshipRepository.updateFriendshipStatus(userFriendship.getId(), userFriendship.getStatus());
        friendshipChangeProperty.set(userFriendship);

    }

    public void deleteFriendship(long id) {
        UserFriendship deleted = friendshipRepository.getById(id);
        friendshipRepository.delete(id);
        if (deleted != null) {
            friendshipChangeProperty.set(deleted);
        }
    }

    public UserFriendship getFriendshipById(long id) {
        return friendshipRepository.getById(id);
    }

    public Collection<UserFriendship> getAllFriendships() {
        return friendshipRepository.getAll();
    }

    public Collection<UserFriendship> getFriendshipsByIds(List<Long> ids) {
        return friendshipRepository.getByIds(ids);
    }

    public Set<UserFriendship> getNonApprovedFriendships(long userId) {
        return friendshipRepository.getNonApprovedFriendshipsForUser(userId);
    }
}