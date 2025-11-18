package repository.for_data_base;
import configs.DBConnection;
import entities.users.persons.Person;
import exceptions.repositories.RepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonDataBaseRepository extends UserAbstractDataBaseRepository<Person> {

    @Override
    protected long getId(Person item) {
        return item.getId();
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


        String placeholders = ids.stream().map(_ -> "?").collect(Collectors.joining(","));
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
    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        return new Person(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("occupation"),
                rs.getInt("empathy_level"),
                rs.getDate("birth_date").toLocalDate()
        );
    }

    @Override
    protected void saveOneToDatabase(Person person) {
        String insertUserSql = "INSERT INTO users (id, username, password, email) VALUES (?, ?, ?, ?)";
        String insertPersonSql = "INSERT INTO persons (user_id, first_name, last_name, occupation, empathy_level, birth_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement psUser = conn.prepareStatement(insertUserSql)) {
                psUser.setLong(1, person.getId());
                psUser.setString(2, person.getUsername());
                psUser.setString(3, person.getPassword());
                psUser.setString(4, person.getEmail());
                psUser.executeUpdate();
            }

            try (PreparedStatement psPerson = conn.prepareStatement(insertPersonSql)) {
                psPerson.setLong(1, person.getId());
                psPerson.setString(2, person.getFirst_name());
                psPerson.setString(3, person.getLast_name());
                psPerson.setString(4, person.getOccupation());
                psPerson.setInt(5, person.getEmpathyLevel());
                psPerson.setDate(6, java.sql.Date.valueOf(person.getBirth_date()));
                psPerson.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RepositoryException("ERROR ON ADDING : "+e.getMessage());
        }

    }

}
