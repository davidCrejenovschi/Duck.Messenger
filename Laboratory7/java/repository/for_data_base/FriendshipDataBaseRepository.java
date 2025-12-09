package repository.for_data_base;
import configs.DBConnection;
import entities.pairs.Friendship;
import exceptions.repositories.RepositoryException;
import repository.abstracts.AbstractDataBaseRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendshipDataBaseRepository extends AbstractDataBaseRepository<Friendship> {

    @Override
    protected long getId(Friendship item) {
       return item.getId();
    }

    @Override
    public void saveChangesToExternMemory() {

    }

    @Override
    protected Collection<Friendship> readAllFromDatabase() {
        String sql = "SELECT user_left, user_right FROM friendships";
        Collection<Friendship> friendships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long left = rs.getLong("user_left");
                Long right = rs.getLong("user_right");

                Friendship friendship = new Friendship(left, right);
                friendships.add(friendship);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return friendships;
    }

    @Override
    protected Friendship readOneFromDatabase(long id) {
        String sql = "SELECT user_left, user_right FROM friendships WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long left = rs.getLong("user_left");
                    Long right = rs.getLong("user_right");

                    return new Friendship(left, right);
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

        return null;
    }

    @Override
    protected Collection<Friendship> getByIdsFromDatabase(Set<Long> ids) {

        String placeholders = ids.stream()
                .map(_ -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT user_left, user_right FROM friendships WHERE id IN (" + placeholders + ")";

        List<Friendship> friendships = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (Long id : ids) {
                ps.setLong(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long left = rs.getLong("user_left");
                    Long right = rs.getLong("user_right");
                    friendships.add(new Friendship(left, right));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading friendships by IDs: " + e.getMessage(), e);
        }

        return friendships;
    }

    @Override
    protected void saveOneToDatabase(Friendship friendship) {
        String sql = "INSERT INTO friendships (id, user_left, user_right) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, friendship.getId());
            ps.setLong(2, Math.min(friendship.getLeft(), friendship.getRight()));
            ps.setLong(3, Math.max(friendship.getLeft(), friendship.getRight()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    protected void existInDataBase(long id) {
        String sql = "SELECT 1 FROM friendships WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RepositoryException("Friendship with id=" + id + " does not exist in the database.");
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error checking existence: " + e.getMessage(), e);
        }
    }

}
