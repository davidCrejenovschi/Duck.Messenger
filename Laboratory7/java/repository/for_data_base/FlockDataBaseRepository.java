package repository.for_data_base;
import configs.DBConnection;
import entities.flocks.Flock;
import entities.flocks.enums.FlockInterest;
import exceptions.repositories.RepositoryException;
import repository.abstracts.AbstractDataBaseRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FlockDataBaseRepository extends AbstractDataBaseRepository<Flock> {

    @Override
    protected long getId(Flock item) {
        return item.getId();
    }

    private void executeMemberBatchUpdate(Connection conn, String sql, long flockId, Collection<Long> userIds) throws SQLException {

        if (userIds.isEmpty()) return;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Long userId : userIds) {
                ps.setLong(1, flockId);
                ps.setLong(2, userId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    @Override
    public void saveChangesToExternMemory() {

        String selectSql = "SELECT user_id FROM members WHERE flock_id = ?";
        String insertSql = "INSERT INTO members (flock_id, user_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM members WHERE flock_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            for (Flock flock : items.values()) {
                long flockId = flock.getId();

                Set<Long> dbMembers = new HashSet<>();
                try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                    ps.setLong(1, flockId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            dbMembers.add(rs.getLong("user_id"));
                        }
                    }
                }

                Set<Long> ramMembers = flock.getMembers();

                Set<Long> toInsert = new HashSet<>(ramMembers);
                toInsert.removeAll(dbMembers);

                Set<Long> toDelete = new HashSet<>(dbMembers);
                toDelete.removeAll(ramMembers);

                executeMemberBatchUpdate(conn, insertSql, flockId, toInsert);
                executeMemberBatchUpdate(conn, deleteSql, flockId, toDelete);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving flock member changes: " + e.getMessage(), e);
        }
    }

    private Flock mapResultSetToFlock(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String interestStr = rs.getString("interest");
        System.out.println("____");

        FlockInterest interest;
        try {
            interest = FlockInterest.valueOf(interestStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RepositoryException("Invalid FlockInterest value in database: " + interestStr, e);
        }

        return new Flock(id, interest, name);
    }

    @Override
    protected Collection<Flock> readAllFromDatabase() {

        String flockSql = "SELECT id, name, interest FROM flocks";
        String membersSql = "SELECT user_id, flock_id FROM members";

        List<Flock> flocks = new ArrayList<>();
        Map<Long, Flock> flockMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(flockSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Flock flock = mapResultSetToFlock(rs);
                    flocks.add(flock);
                    flockMap.put(flock.getId(), flock);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(membersSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    long flockId = rs.getLong("flock_id");
                    long userId = rs.getLong("user_id");

                    Flock flock = flockMap.get(flockId);
                    if (flock != null) {
                        flock.getMembers().add(userId);
                    }
                }
            }

            return flocks;

        } catch (SQLException e) {
            throw new RepositoryException("Error reading all flocks from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Flock readOneFromDatabase(long id) {

        String flockSql = "SELECT id, name, interest FROM flocks WHERE id = ?";
        String membersSql = "SELECT user_id FROM members WHERE flock_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            Flock flock;
            try (PreparedStatement ps = conn.prepareStatement(flockSql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        flock = mapResultSetToFlock(rs);
                    } else {
                        throw new RepositoryException("Flock with id=" + id + " does not exist.");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(membersSql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    Set<Long> members = new HashSet<>();
                    while (rs.next()) {
                        members.add(rs.getLong("user_id"));
                    }
                    flock.getMembers().addAll(members);
                }
            }

            return flock;

        } catch (SQLException e) {
            throw new RepositoryException("Error reading flock from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<Flock> getByIdsFromDatabase(Set<Long> ids) {

        String placeholders = ids.stream().map(_ -> "?").collect(Collectors.joining(","));
        String flockSql = "SELECT id, name, interest FROM flocks WHERE id IN (" + placeholders + ")";
        String membersSql = "SELECT user_id, flock_id FROM members WHERE flock_id IN (" + placeholders + ")";

        List<Flock> flocks = new ArrayList<>();
        Map<Long, Flock> flockMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(flockSql)) {
                int index = 1;
                for (Long id : ids) {
                    ps.setLong(index++, id);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Flock flock = mapResultSetToFlock(rs);
                        flocks.add(flock);
                        flockMap.put(flock.getId(), flock);
                    }
                }
            }

            if (flocks.isEmpty()) {
                return flocks;
            }

            try (PreparedStatement ps = conn.prepareStatement(membersSql)) {
                int index = 1;
                for (Long id : ids) {
                    ps.setLong(index++, id);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long flockId = rs.getLong("flock_id");
                        long userId = rs.getLong("user_id");

                        Flock flock = flockMap.get(flockId);
                        if (flock != null) {
                            flock.getMembers().add(userId);
                        }
                    }
                }
            }

            return flocks;

        } catch (SQLException e) {
            throw new RepositoryException("Error reading flocks by IDs from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void saveOneToDatabase(Flock flock) {

        String flockSql = "INSERT INTO flocks (id, name, interest) VALUES (?, ?, ?)";
        String membersSql = "INSERT INTO members (flock_id, user_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(flockSql)) {
                ps.setLong(1, flock.getId());
                ps.setString(2, flock.getName());
                ps.setString(3, flock.getInterest().name());
                ps.executeUpdate();
            }

            if (!flock.getMembers().isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(membersSql)) {
                    for (Long userId : flock.getMembers()) {
                        ps.setLong(1, flock.getId());
                        ps.setLong(2, userId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving flock to database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void deleteOneFromDatabase(long id) {
        String deleteFlockSql = "DELETE FROM flocks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteFlockSql)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("Flock with id=" + id + " does not exist.");
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting flock from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void existInDataBase(long id) {
        String sql = "SELECT 1 FROM flocks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RepositoryException("Flock with id=" + id + " does not exist in the database.");
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error checking existence: " + e.getMessage(), e);
        }
    }

}
