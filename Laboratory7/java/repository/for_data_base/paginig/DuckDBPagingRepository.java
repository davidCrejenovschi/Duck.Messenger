package repository.for_data_base.paginig;

import configs.DBConnection;
import entities.dtos.users.ducks.DuckFilterDTO;
import entities.pairs.Pair;
import entities.users.ducks.FlyingDuck;
import entities.users.ducks.SwimmingDuck;
import entities.users.ducks.abstracts.Duck;
import entities.users.ducks.enums.DuckType;
import entities.utils.Page;
import entities.utils.Pageable;
import exceptions.repositories.RepositoryException;
import repository.for_data_base.DuckDataBaseRepository;
import repository.interfaces.for_ducks.PagingDuckRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DuckDBPagingRepository extends DuckDataBaseRepository implements PagingDuckRepository<Duck> {

    @Override
    public Page<Duck> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
    }

    @Override
    public Page<Duck> findAllOnPage(Pageable pageable, DuckFilterDTO filter) {
        try (Connection connection = DBConnection.getConnection()) {

            int totalNumberOfDucks = count(connection, filter);

            List<Duck> ducksOnPage = totalNumberOfDucks > 0
                    ? findAllOnPage(connection, pageable, filter)
                    : new ArrayList<>();

            return new Page<>(ducksOnPage, totalNumberOfDucks);

        } catch (SQLException e) {
            throw new RepositoryException("Error fetching ducks on page: " + e.getMessage(), e);
        }
    }

    private Pair<String, List<Object>> toSql(DuckFilterDTO filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }

        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        filter.getId().ifPresent(id -> {
            conditions.add("u.id = ?");
            params.add(id);
        });

        filter.getUsername().ifPresent(username -> {
            conditions.add("u.username LIKE ?");
            params.add("%" + username + "%");
        });

        filter.getEmail().ifPresent(email -> {
            conditions.add("u.email LIKE ?");
            params.add("%" + email + "%");
        });

        filter.getDuckType().ifPresent(type -> {
            conditions.add("d.duck_type = ?");
            params.add(type.name());
        });

        filter.getSpeed().ifPresent(speed -> {
            switch (filter.getSpeedComparison().orElse(DuckFilterDTO.Comparison.EQ)) {
                case GT -> conditions.add("d.speed > ?");
                case LT -> conditions.add("d.speed < ?");
                case EQ -> conditions.add("d.speed = ?");
            }
            params.add(speed);
        });

        filter.getStamina().ifPresent(stamina -> {
            switch (filter.getStaminaComparison().orElse(DuckFilterDTO.Comparison.EQ)) {
                case GT -> conditions.add("d.stamina > ?");
                case LT -> conditions.add("d.stamina < ?");
                case EQ -> conditions.add("d.stamina = ?");
            }
            params.add(stamina);
        });

        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }

    private int count(Connection connection, DuckFilterDTO filter) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users u JOIN ducks d ON u.id = d.user_id";

        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getLeft().isEmpty()) {
            sql += " WHERE " + sqlFilter.getLeft();
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int idx = 0;
            for (Object param : sqlFilter.getRight()) {
                statement.setObject(++idx, param);
            }
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("count") : 0;
            }
        }
    }

    private List<Duck> findAllOnPage(Connection connection, Pageable pageable, DuckFilterDTO filter) throws SQLException {
        List<Duck> ducksOnPage = new ArrayList<>();

        String sql = """
            SELECT u.id, u.username, u.password, u.email,
                   d.duck_type, d.speed, d.stamina
            FROM users u
            JOIN ducks d ON u.id = d.user_id
        """;

        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getLeft().isEmpty()) {
            sql += " WHERE " + sqlFilter.getLeft();
        }

        sql += " LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int idx = 0;
            for (Object param : sqlFilter.getRight()) {
                stmt.setObject(++idx, param);
            }

            stmt.setInt(++idx, pageable.pageSize());
            stmt.setInt(++idx, pageable.pageSize() * pageable.pageNumber());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    DuckType type = DuckType.valueOf(rs.getString("duck_type"));
                    double speed = rs.getDouble("speed");
                    double stamina = rs.getDouble("stamina");

                    Duck duck = switch (type) {
                        case SWIMMER -> new SwimmingDuck(id, username, password, email, speed, stamina);
                        case FLYER -> new FlyingDuck(id, username, password, email, speed, stamina);
                    };

                    ducksOnPage.add(duck);
                }
            }
        }

        return ducksOnPage;
    }
}
