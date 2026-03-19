package repositories;

import connections.DBConnection;
import entities.messages.Message;
import entities.messages.ReplyMessage;
import entities.users.AbstractUser;
import exceptions.RepositoryException;
import utils.UserMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MessageRepository extends AbstractDBRepository<Message> implements MRepository {

    private final String BASE_QUERY =
            "SELECT DISTINCT m.id, m.content, m.sent_at, m.reply_to_id, " +
                    "u.id AS user_id, u.username, u.password, u.email, " +
                    "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date, " +
                    "d.duck_type, d.speed, d.stamina " +
                    "FROM messages m " +
                    "JOIN users u ON m.sender_id = u.id " +
                    "LEFT JOIN persons p ON u.id = p.user_id " +
                    "LEFT JOIN ducks d ON u.id = d.user_id ";

    @Override
    protected long getId(Message item) {
        return item.getId();
    }

    private AbstractUser extractSenderFromResultSet(ResultSet rs) throws SQLException {
        return UserMapper.extractUserFromResultSet(rs);
    }

    private Message mapResultSetToMessage(ResultSet rs, Connection conn) throws SQLException {
        long id = rs.getLong("id");
        String content = rs.getString("content");
        Timestamp sentAt = rs.getTimestamp("sent_at");
        long replyToId = rs.getLong("reply_to_id");

        AbstractUser sender = extractSenderFromResultSet(rs);
        Message message;

        if (replyToId != 0) {
            Message quotedMessage = readOneInternal(conn, replyToId);
            if (quotedMessage == null) {
                message = new Message(id, sender, content, sentAt.toLocalDateTime());
            } else {
                message = new ReplyMessage(id, sender, content, sentAt.toLocalDateTime(), quotedMessage);
            }
        } else {
            message = new Message(id, sender, content, sentAt.toLocalDateTime());
        }

        loadRecipients(conn, message);
        return message;
    }

    private void loadRecipients(Connection conn, Message message) throws SQLException {
        String sql = "SELECT u.id AS id, u.username, u.password, u.email, " +
                "p.first_name, p.last_name, p.occupation, p.empathy_level, p.birth_date, " +
                "d.duck_type, d.speed, d.stamina " +
                "FROM message_recipients mr " +
                "JOIN users u ON mr.recipient_id = u.id " +
                "LEFT JOIN persons p ON u.id = p.user_id " +
                "LEFT JOIN ducks d ON u.id = d.user_id " +
                "WHERE mr.message_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, message.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    message.addReceiver(extractSenderFromResultSet(rs));
                }
            }
        }
    }

    private Message readOneInternal(Connection conn, long id) throws SQLException {
        String sql = BASE_QUERY + " WHERE m.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMessage(rs, conn);
                }
            }
        }
        return null;
    }

    @Override
    public List<Message> getConversationBefore(long userId, long friendId, LocalDateTime before, int pageSize) {
        List<Message> messages = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(BASE_QUERY);
        sqlBuilder.append(" JOIN message_recipients mr ON m.id = mr.message_id ");
        sqlBuilder.append(" WHERE ((m.sender_id = ? AND mr.recipient_id = ?) ");
        sqlBuilder.append(" OR (m.sender_id = ? AND mr.recipient_id = ?)) ");

        if (before != null) {
            sqlBuilder.append(" AND m.sent_at < ? ");
        }

        sqlBuilder.append(" ORDER BY m.sent_at DESC LIMIT ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            int idx = 1;
            ps.setLong(idx++, userId);
            ps.setLong(idx++, friendId);
            ps.setLong(idx++, friendId);
            ps.setLong(idx++, userId);

            if (before != null) {
                ps.setTimestamp(idx++, Timestamp.valueOf(before));
            }

            ps.setInt(idx, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapResultSetToMessage(rs, conn));
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error loading conversation: " + e.getMessage(), e);
        }

        return messages;
    }

    @Override
    protected Collection<Message> readAllFromDatabase() {
        List<Message> messages = new ArrayList<>();
        String sql = BASE_QUERY + " ORDER BY m.sent_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs, conn));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error reading all: " + e.getMessage(), e);
        }
        return messages;
    }

    @Override
    protected Message readOneFromDatabase(long id) {
        try (Connection conn = DBConnection.getConnection()) {
            return readOneInternal(conn, id);
        } catch (SQLException e) {
            throw new RepositoryException("Error reading one: " + e.getMessage(), e);
        }
    }

    @Override
    protected Collection<Message> getByIdsFromDatabase(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = BASE_QUERY + " WHERE m.id IN (" + placeholders + ") ORDER BY m.sent_at DESC";

        List<Message> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            for (Long id : ids) ps.setLong(idx++, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) messages.add(mapResultSetToMessage(rs, conn));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error reading by IDs: " + e.getMessage(), e);
        }
        return messages;
    }

    @Override
    protected void saveOneToDatabase(Message message) {
        String sql = "INSERT INTO messages (sender_id, content, sent_at, reply_to_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, message.getSender().getId());
            ps.setString(2, message.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(message.getTimestamp()));
            if (message instanceof ReplyMessage) {
                ps.setLong(4, ((ReplyMessage) message).getQuotedMessage().getId());
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }

            if (ps.executeUpdate() == 0) throw new SQLException("Save failed.");
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) message.setId(gk.getLong(1));
            }
            saveRecipientsToDb(conn, message);
        } catch (SQLException e) {
            throw new RepositoryException("Error saving: " + e.getMessage(), e);
        }
    }

    private void saveRecipientsToDb(Connection conn, Message message) throws SQLException {
        if (message.getRecipients() == null || message.getRecipients().isEmpty()) return;
        String sql = "INSERT INTO message_recipients (message_id, recipient_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (AbstractUser r : message.getRecipients()) {
                ps.setLong(1, message.getId());
                ps.setLong(2, r.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    protected void deleteOneFromDatabase(long id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting: " + e.getMessage(), e);
        }
    }
}