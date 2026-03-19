package repositories;

import connections.DBConnection;
import entities.friendships.FriendshipStatus;
import entities.friendships.UserFriendship;
import exceptions.RepositoryException;
import entities.users.AbstractUser;
import utils.UserMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class FriendshipRepository extends AbstractDBRepository<UserFriendship> implements FRepository {

    private static final String USER_QUERY_BASE =
            "SELECT u.id, u.username, u.password, u.email, " +
                    "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date, " +
                    "d.duck_type, d.speed, d.stamina " +
                    "FROM users u " +
                    "LEFT JOIN persons p ON u.id = p.user_id " +
                    "LEFT JOIN ducks d ON u.id = d.user_id " +
                    "WHERE u.id = ?";

    private AbstractUser extractUserFromResultSet(ResultSet rs) throws SQLException {
        return UserMapper.extractUserFromResultSet(rs);
    }

    private AbstractUser findUserByIdFromDb(Long id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(USER_QUERY_BASE)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    protected long getId(UserFriendship item) {
        return item.getId();
    }

    private UserFriendship mapResultSetToFriendship(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long senderId = rs.getLong("sender_id");
        Long receiverId = rs.getLong("receiver_id");
        String statusString = rs.getString("status");

        AbstractUser sender = findUserByIdFromDb(senderId);
        AbstractUser receiver = findUserByIdFromDb(receiverId);

        if (sender == null || receiver == null) {
            throw new SQLException("Sender or Receiver not found for friendship ID: " + id);
        }

        UserFriendship friendship = new UserFriendship(sender, receiver);
        friendship.setId(id);
        friendship.setStatus(FriendshipStatus.valueOf(statusString));

        return friendship;
    }

    @Override
    protected Collection<UserFriendship> readAllFromDatabase() {
        String sql = "SELECT id, sender_id, receiver_id, status FROM friendships";
        Collection<UserFriendship> friendships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                friendships.add(mapResultSetToFriendship(rs));
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading all friendships: " + e.getMessage(), e);
        }

        return friendships;
    }

    @Override
    protected UserFriendship readOneFromDatabase(long id) {
        String sql = "SELECT id, sender_id, receiver_id, status FROM friendships WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFriendship(rs);
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading friendship by ID: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected Collection<UserFriendship> getByIdsFromDatabase(Set<Long> ids) {
        String placeholders = ids.stream()
                .map(a -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT id, sender_id, receiver_id, status FROM friendships WHERE id IN (" + placeholders + ")";

        List<UserFriendship> friendships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (Long id : ids) {
                ps.setLong(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friendships.add(mapResultSetToFriendship(rs));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading friendships by IDs: " + e.getMessage(), e);
        }

        return friendships;
    }

    @Override
    protected void saveOneToDatabase(UserFriendship friendship) {
        String sql = "INSERT INTO friendships (sender_id, receiver_id, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, friendship.getSender().getId());
            ps.setLong(2, friendship.getReceiver().getId());
            ps.setString(3, friendship.getStatus().name());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    friendship.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving friendship: " + e.getMessage(), e);
        }
    }

    @Override
    protected void deleteOneFromDatabase(long id) {
        String sql = "DELETE FROM friendships WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("NO FRIENDSHIP WITH ID : " + id);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting friendship: " + e.getMessage(), e);
        }
    }

    public void updateFriendshipStatus(long id, FriendshipStatus status) {
        String sql = "UPDATE friendships SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("Could not update friendship status. ID not found: " + id);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error updating friendship status: " + e.getMessage(), e);
        }
    }

    public Set<UserFriendship> getNonApprovedFriendshipsForUser(Long userId) {
        String sql = "SELECT id, sender_id, receiver_id, status FROM friendships WHERE (sender_id = ? OR receiver_id = ?) AND status <> 'APPROVED'";
        Set<UserFriendship> friendships = new HashSet<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friendships.add(mapResultSetToFriendship(rs));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage(), e);
        }

        return friendships;
    }
}