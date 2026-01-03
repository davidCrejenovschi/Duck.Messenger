package repositories;
import connections.DBConnection;
import entities.users.AbstractUser;
import exceptions.RepositoryException;
import utils.StringEncryptor;
import utils.UserMapper;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractUserDBRepository<T extends AbstractUser> extends AbstractDBRepository<T> implements URepository<T> {

    @Override
    protected abstract long getId(T item);
    @Override
    protected abstract Collection<T> readAllFromDatabase();
    @Override
    protected abstract T readOneFromDatabase(long id);
    @Override
    protected abstract Collection<T> getByIdsFromDatabase(Set<Long> ids);

    protected abstract void saveSpecificDetails(Connection conn, T entity) throws SQLException;

    @Override
    protected void saveOneToDatabase(T entity) {
        String insertUserSql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUser = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, entity.getUsername());
                psUser.setString(2, encryptPassword(entity.getPassword()));
                psUser.setString(3, entity.getEmail());
                psUser.executeUpdate();

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                saveSpecificDetails(conn, entity);
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new RepositoryException("ERROR ON ADDING : " + e.getMessage(), e);
            }

        } catch (SQLException e) {

            throw new RepositoryException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void deleteOneFromDatabase(long id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("NO USER WITH ID : " + id);
            }

        } catch (SQLException e) {
            throw new RepositoryException("ERROR ON DELETE : " + e.getMessage(), e);
        }
    }

    protected Set<AbstractUser> getUserFriends(long userId) {

        Set<AbstractUser> friends = new HashSet<>();

        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM friendships f " +
                "JOIN users u ON (f.sender_id = u.id AND f.receiver_id = ?) OR (f.receiver_id = u.id AND f.sender_id = ?) " +
                "LEFT JOIN persons p ON u.id = p.user_id " +
                "LEFT JOIN ducks d ON u.id = d.user_id " +
                "WHERE f.status = 'APPROVED' AND u.id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setLong(3, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    friends.add(UserMapper.extractUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error reading friends for user ID: " + userId + " - " + e.getMessage(), e);
        }

        return friends;
    }

    @Override
    public abstract T findUserByUsername(String username);

}