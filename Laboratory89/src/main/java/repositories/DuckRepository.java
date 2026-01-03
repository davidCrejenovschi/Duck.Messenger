package repositories;
import connections.DBConnection;
import entities.users.Duck;
import entities.users.FlyingDuck;
import entities.users.SwimmingDuck;
import exceptions.RepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import entities.users.*;
import java.sql.*;
import java.util.*;

public class DuckRepository extends AbstractUserDBRepository<Duck> {

    @Override
    protected long getId(Duck item) {
        return item.getId();
    }

    private Duck mapResultSetToDuck(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = encryptor.decrypt(rs.getString("password"));
        String email = rs.getString("email");
        String duckType = rs.getString("duck_type");
        double speed = rs.getDouble("speed");
        double stamina = rs.getDouble("stamina");
        long id = rs.getLong("id");

        Duck duck;
        if ("SWIMMER".equalsIgnoreCase(duckType)) {
            duck = new SwimmingDuck.Builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .speed(speed)
                    .stamina(stamina)
                    .build();
        } else {
            duck = new FlyingDuck.Builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .speed(speed)
                    .stamina(stamina)
                    .build();
        }

        duck.setId(id);

        Set<AbstractUser> friends = getUserFriends(id);
        for (AbstractUser friend : friends) {
            duck.addFriend(friend);
        }

        return duck;
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
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading duck from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<Duck> getByIdsFromDatabase(Set<Long> ids) {
        if (ids.isEmpty()) return new ArrayList<>();

        String placeholders = ids.stream().map(a -> "?").collect(Collectors.joining(","));
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
    protected void saveSpecificDetails(Connection conn, Duck duck) throws SQLException {
        String insertDuckSql = "INSERT INTO ducks (user_id, duck_type, speed, stamina) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psDuck = conn.prepareStatement(insertDuckSql)) {
            psDuck.setLong(1, duck.getId());
            psDuck.setString(2, duck.getDuckType().toString());
            psDuck.setDouble(3, duck.getSpeed());
            psDuck.setDouble(4, duck.getStamina());
            psDuck.executeUpdate();
        }
    }

    @Override
    public Duck findUserByUsername(String username) {

        Duck duck = findInCache(username);
        if (duck != null) {
            return duck;
        }

        duck = findInDatabase(username);
        return duck;
    }

    private Duck findInCache(String username) {
        for (Duck duck : items.values()) {
            if (duck.getUsername().equals(username)) {
                return duck;
            }
        }
        return null;
    }

    private Duck findInDatabase(String username) {

        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM users u " +
                "JOIN ducks d ON u.id = d.user_id " +
                "WHERE u.username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDuck(resultSet);
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException("Database error while searching for duck: " + username, e);
        }

        return null;
    }


}