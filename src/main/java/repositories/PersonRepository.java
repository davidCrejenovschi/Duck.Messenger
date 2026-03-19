package repositories;
import connections.DBConnection;
import entities.users.Person;
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
import entities.users.AbstractUser;
import java.sql.*;
import java.util.*;

public class PersonRepository extends AbstractUserDBRepository<Person> {

    @Override
    protected long getId(Person item) {
        return item.getId();
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        java.sql.Date sqlBirthDate = rs.getDate("birth_date");
        java.time.LocalDate birthDate = (sqlBirthDate != null) ? sqlBirthDate.toLocalDate() : null;

        Person person = new Person.Builder()
                .username(rs.getString("username"))
                .password(encryptor.decrypt(rs.getString("password")))
                .email(rs.getString("email"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .occupation(rs.getString("occupation"))
                .empathyLevel(rs.getInt("empathy_level"))
                .birthDate(birthDate)
                .build();

        person.setId(id);

        Set<AbstractUser> friends = getUserFriends(id);
        for (AbstractUser friend : friends) {
            person.addFriend(friend);
        }

        return person;
    }

    @Override
    protected Collection<Person> readAllFromDatabase() {
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date " +
                "FROM users u " +
                "JOIN persons p ON u.id = p.user_id";

        List<Person> persons = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                persons.add(mapResultSetToPerson(rs));
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading all persons: " + e.getMessage(), e);
        }

        return persons;
    }

    @Override
    protected Person readOneFromDatabase(long id) {
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date " +
                "FROM users u " +
                "JOIN persons p ON u.id = p.user_id " +
                "WHERE u.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPerson(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading person by ID: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<Person> getByIdsFromDatabase(Set<Long> ids) {
        if (ids.isEmpty()) return new ArrayList<>();

        String placeholders = ids.stream().map(a -> "?").collect(Collectors.joining(","));
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date " +
                "FROM users u " +
                "JOIN persons p ON u.id = p.user_id " +
                "WHERE u.id IN (" + placeholders + ")";

        Collection<Person> persons = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            for (Long id : ids) {
                ps.setLong(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    persons.add(mapResultSetToPerson(rs));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error reading persons by IDs: " + e.getMessage(), e);
        }

        return persons;
    }

    @Override
    protected void saveSpecificDetails(Connection conn, Person person) throws SQLException {
        String insertPersonSql = "INSERT INTO persons (user_id, first_name, last_name, occupation, empathy_level, birth_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psPerson = conn.prepareStatement(insertPersonSql)) {
            psPerson.setLong(1, person.getId());
            psPerson.setString(2, person.getFirstName());
            psPerson.setString(3, person.getLastName());
            psPerson.setString(4, person.getOccupation());
            psPerson.setInt(5, person.getEmpathyLevel());
            psPerson.setDate(6, person.getBirthDate() != null ? java.sql.Date.valueOf(person.getBirthDate()) : null);
            psPerson.executeUpdate();
        }
    }

    @Override
    public Person findUserByUsername(String username) {

        Person person = findInCache(username);
        if (person != null) {
            return person;
        }
        person = findInDatabase(username);
        return person;
    }

    private Person findInDatabase(String username) {
        String sql = "SELECT u.id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date " +
                "FROM users u " +
                "JOIN persons p ON u.id = p.user_id " +
                "WHERE u.username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    return mapResultSetToPerson(resultSet);
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException("Database error while searching for user: " + username, e);
        }

        return null;
    }

    private Person findInCache(String username) {
        for (Person person : items.values()) {
            if (person.getUsername().equals(username)) {
                return person;
            }
        }
        return null;
    }
}