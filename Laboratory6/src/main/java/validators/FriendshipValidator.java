package validators;
import entities.pairs.Friendship;
import exceptions.validators.FriendshipValidationException;
import java.util.ArrayList;
import java.util.List;

public class FriendshipValidator {

    public void validateFriendship(Friendship pair) throws FriendshipValidationException {

        if (pair == null) {
            throw new FriendshipValidationException("Friendship pair is null.");
        }

        List<String> errors = new ArrayList<>();

        try {
            validateId(pair.getId());
        } catch (FriendshipValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateLeftId(pair.getLeft());
        } catch (FriendshipValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateRightId(pair.getRight());
        } catch (FriendshipValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateDistinctIds(pair.getLeft(), pair.getRight());
        } catch (FriendshipValidationException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new FriendshipValidationException(String.join(" ", errors));
        }
    }

    public void validateId(long id) throws FriendshipValidationException {
        if (id <= 0) {
            throw new FriendshipValidationException("Friendship id must be a positive number.");
        }
    }

    public void validateLeftId(Long leftId) throws FriendshipValidationException {
        if (leftId == null) {
            throw new FriendshipValidationException("Left id cannot be null.");
        }
        if (leftId <= 0) {
            throw new FriendshipValidationException("Left id must be a positive number.");
        }
    }

    public void validateRightId(Long rightId) throws FriendshipValidationException {
        if (rightId == null) {
            throw new FriendshipValidationException("Right id cannot be null.");
        }
        if (rightId <= 0) {
            throw new FriendshipValidationException("Right id must be a positive number.");
        }
    }

    public void validateDistinctIds(Long leftId, Long rightId) throws FriendshipValidationException {
        if (leftId != null && leftId.equals(rightId)) {
            throw new FriendshipValidationException("An entity cannot be friends with itself.");
        }
    }
}
