package repository.for_data_base;
import configs.DBConnection;
import entities.users.ducks.FlyingDuck;
import entities.users.ducks.SwimmingDuck;
import exceptions.repositories.RepositoryException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import entities.users.ducks.abstracts.Duck;
import java.sql.Connection;
import java.util.stream.Collectors;

public class DuckDataBaseRepository extends UserAbstractDataBaseRepository<Duck> {

    @Override
    protected long getId(Duck item) {
        return item.getId();
    }

    private Duck mapResultSetToDuck(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String duckType = rs.getString("duck_type");
        double speed = rs.getDouble("speed");
        double stamina = rs.getDouble("stamina");

        if ("SWIMMER".equalsIgnoreCase(duckType)) {
            return new SwimmingDuck(id, username, password, email, speed, stamina);
        } else {
            return new FlyingDuck(id, username, password, email, speed, stamina);
        }
    }
    @Override
    protected Collection<Duck> readAllFromDatabase() {

        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM users u " +
                "JOIN ducks d ON u.id = d.user_id";

        List<Duck> ducks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ducks.add(mapResultSetToDuck(rs));
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading all ducks from database: " + e.getMessage(), e);
        }

        return ducks;
    }
    @Override
    protected Duck readOneFromDatabase(long id) {
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM users u " +
                "JOIN ducks d ON u.id = d.user_id " +
                "WHERE u.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDuck(rs);
                } else {
                    throw new RepositoryException("Duck with user_id=" + id + " does not exist.");
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading duck from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<Duck> getByIdsFromDatabase(Set<Long> ids) {

        String placeholders = ids.stream().map(_ -> "?").collect(Collectors.joining(","));
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM users u " +
                "JOIN ducks d ON u.id = d.user_id " +
                "WHERE u.id IN (" + placeholders + ")";

        List<Duck> ducks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (Long id : ids) {
                ps.setLong(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ducks.add(mapResultSetToDuck(rs));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading ducks by IDs: " + e.getMessage(), e);
        }

        return ducks;
    }

    @Override
    protected void saveOneToDatabase(Duck duck) {
        String insertUserSql = "INSERT INTO users (id, username, password, email) VALUES (?, ?, ?, ?)";
        String insertDuckSql = "INSERT INTO ducks (user_id, duck_type, speed, stamina) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement psUser = conn.prepareStatement(insertUserSql)) {
                psUser.setLong(1, duck.getId());
                psUser.setString(2, duck.getUsername());
                psUser.setString(3, duck.getPassword());
                psUser.setString(4, duck.getEmail());
                psUser.executeUpdate();
            }

            try (PreparedStatement psDuck = conn.prepareStatement(insertDuckSql)) {
                psDuck.setLong(1, duck.getId());
                psDuck.setString(2, duck.getDuckType().toString());
                psDuck.setDouble(3, duck.getSpeed());
                psDuck.setDouble(4, duck.getStamina());
                psDuck.executeUpdate();
            }

        } catch (SQLException e) {
           throw new RepositoryException("ERROR ON ADDING : "+e.getMessage());
        }
    }


}
