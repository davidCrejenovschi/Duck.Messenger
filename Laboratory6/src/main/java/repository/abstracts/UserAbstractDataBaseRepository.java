package repository.abstracts;
import configs.DBConnection;
import exceptions.repositories.RepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

public abstract class UserAbstractDataBaseRepository<T> extends AbstractDataBaseRepository<T> {

    @Override
    protected abstract long getId(T item);
    @Override
    protected abstract Collection<T> readAllFromDatabase();
    @Override
    protected abstract T readOneFromDatabase(long id);
    @Override
    protected abstract void saveOneToDatabase(T object);
    @Override
    protected abstract Collection<T> getByIdsFromDatabase(Set<Long> ids);
    @Override
    public abstract void saveChangesToExternMemory();

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
    @Override
    protected void existInDataBase(long id) {
        String sql = "SELECT 1 FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RepositoryException("User with id=" + id + " does not exist in the database.");
                }

            }

        } catch (SQLException e) {
            throw new RepositoryException("Error checking existence: " + e.getMessage(), e);
        }
    }

}
