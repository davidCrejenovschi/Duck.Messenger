package repository.for_data_base;
import configs.DBConnection;
import entities.events.RaceEvent;
import exceptions.repositories.RepositoryException;
import repository.abstracts.AbstractDataBaseRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RaceEventDataBaseRepository extends AbstractDataBaseRepository<RaceEvent> {

    @Override
    protected long getId(RaceEvent item) {
        return item.getId();
    }

    private void executeBatchUpdate(Connection conn, String sql, long eventId, Set<Long> userIds) throws SQLException {
        if (userIds.isEmpty()) {
            return;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Long userId : userIds) {
                ps.setLong(1, eventId);
                ps.setLong(2, userId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    @Override
    public void saveChangesToExternMemory() {
        String selectSql = "SELECT user_id FROM subscribers WHERE event_id = ?";
        String insertSql = "INSERT INTO subscribers (event_id, user_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM subscribers WHERE event_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            for (RaceEvent raceEvent : items.values()) {
                long eventId = raceEvent.getId();

                Set<Long> dbSubscribers = new HashSet<>();
                try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                    ps.setLong(1, eventId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            dbSubscribers.add(rs.getLong("user_id"));
                        }
                    }
                }

                Set<Long> inMemory = new HashSet<>(raceEvent.getSubscribersIds());
                Set<Long> toInsert = new HashSet<>(inMemory);
                toInsert.removeAll(dbSubscribers);

                Set<Long> toDelete = new HashSet<>(dbSubscribers);
                toDelete.removeAll(inMemory);

                executeBatchUpdate(conn, insertSql, eventId, toInsert);
                executeBatchUpdate(conn, deleteSql, eventId, toDelete);

            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving RaceEvent subscribers to database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<RaceEvent> readAllFromDatabase() {

        String raceSql = "SELECT e.id, e.name, r.M " +
                "FROM events e " +
                "JOIN race_events r ON e.id = r.id";
        String subscribersSql = "SELECT event_id, user_id FROM subscribers";

        Map<Long, RaceEvent> raceEventMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(raceSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    int M = rs.getInt("M");

                    raceEventMap.put(id, new RaceEvent(id, name, M));
                }
            }

            if (raceEventMap.isEmpty()) {
                throw new RepositoryException("No RaceEvents found in the database.");
            }

            try (PreparedStatement ps = conn.prepareStatement(subscribersSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    long eventId = rs.getLong("event_id");
                    long userId = rs.getLong("user_id");

                    RaceEvent raceEvent = raceEventMap.get(eventId);
                    if (raceEvent != null) {
                        raceEvent.subscribe(userId);
                    }
                }
            }

            return raceEventMap.values();

        } catch (SQLException e) {
            throw new RepositoryException("Error reading all RaceEvents from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected RaceEvent readOneFromDatabase(long id) {

        String raceSql = "SELECT e.name, r.M " +
                "FROM events e " +
                "JOIN race_events r ON e.id = r.id " +
                "WHERE e.id = ?";

        String subscribersSql = "SELECT user_id FROM subscribers WHERE event_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            RaceEvent raceEvent;

            try (PreparedStatement ps = conn.prepareStatement(raceSql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        int M = rs.getInt("M");
                        raceEvent = new RaceEvent(id, name, M);
                    } else {
                        throw new RepositoryException("RaceEvent with id=" + id + " does not exist.");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(subscribersSql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long userId = rs.getLong("user_id");
                        raceEvent.subscribe(userId);
                    }
                }
            }

            return raceEvent;

        } catch (SQLException e) {
            throw new RepositoryException("Error reading RaceEvent from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<RaceEvent> getByIdsFromDatabase(Set<Long> ids) {

        String placeholders = ids.stream().map(_ -> "?").collect(Collectors.joining(","));
        String raceSql = "SELECT e.id, e.name, r.M " +
                "FROM events e " +
                "JOIN race_events r ON e.id = r.id " +
                "WHERE e.id IN (" + placeholders + ")";

        String subscribersSql = "SELECT event_id, user_id FROM subscribers WHERE event_id IN (" + placeholders + ")";

        Map<Long, RaceEvent> raceEventMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(raceSql)) {
                int index = 1;
                for (Long id : ids) {
                    ps.setLong(index++, id);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        int M = rs.getInt("M");

                        raceEventMap.put(id, new RaceEvent(id, name, M));
                    }
                }
            }

            if (raceEventMap.isEmpty()) {
                throw new RepositoryException("No RaceEvents found for the given IDs.");
            }

            try (PreparedStatement ps = conn.prepareStatement(subscribersSql)) {
                int index = 1;
                for (Long id : ids) {
                    ps.setLong(index++, id);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long eventId = rs.getLong("event_id");
                        long userId = rs.getLong("user_id");

                        RaceEvent raceEvent = raceEventMap.get(eventId);
                        if (raceEvent != null) {
                            raceEvent.subscribe(userId);
                        }
                    }
                }
            }

            return raceEventMap.values();

        } catch (SQLException e) {
            throw new RepositoryException("Error reading RaceEvents by IDs from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void saveOneToDatabase(RaceEvent raceEvent) {

        String eventSql = "INSERT INTO events (id, name) VALUES (?, ?)";
        String raceSql = "INSERT INTO race_events (id, M) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement psEvent = conn.prepareStatement(eventSql)) {
                psEvent.setLong(1, raceEvent.getId());
                psEvent.setString(2, raceEvent.getName());
                psEvent.executeUpdate();
            }

            try (PreparedStatement psRace = conn.prepareStatement(raceSql)) {
                psRace.setLong(1, raceEvent.getId());
                psRace.setInt(2, raceEvent.getM());
                psRace.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving RaceEvent to database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void deleteOneFromDatabase(long id) {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new RepositoryException("Event with id=" + id + " does not exist.");
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting Event from database: " + e.getMessage(), e);
        }
    }

    @Override
    protected void existInDataBase(long id) {
        String sql = "SELECT 1 FROM race_events WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new RepositoryException("RaceEvent with id=" + id + " does not exist in the database.");
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error checking existence of RaceEvent: " + e.getMessage(), e);
        }
    }

}
