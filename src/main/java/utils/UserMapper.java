package utils;
import entities.users.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    private static final StringEncryptor encryptor = new StringEncryptor();

    public static AbstractUser extractUserFromResultSet(ResultSet rs) throws SQLException {

        String username = rs.getString("username");
        String password = encryptor.decrypt(rs.getString("password"));
        String email = rs.getString("email");
        long userId = rs.getLong("id");

        String duckType = rs.getString("duck_type");

        AbstractUser user;

        if (duckType != null) {
            double speed = rs.getDouble("speed");
            double stamina = rs.getDouble("stamina");
            if ("SWIMMER".equalsIgnoreCase(duckType)) {
                user = new SwimmingDuck.Builder()
                        .username(username).password(password).email(email)
                        .speed(speed).stamina(stamina).build();
            } else {
                user = new FlyingDuck.Builder()
                        .username(username).password(password).email(email)
                        .speed(speed).stamina(stamina).build();
            }
        } else {
            java.sql.Date sqlBirthDate = rs.getDate("birth_date");
            java.time.LocalDate birthDate = (sqlBirthDate != null) ? sqlBirthDate.toLocalDate() : null;
            user = new Person.Builder()
                    .username(username).password(password).email(email)
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .occupation(rs.getString("occupation"))
                    .empathyLevel(rs.getInt("empathy_level"))
                    .birthDate(birthDate)
                    .build();
        }

        user.setId(userId);
        return user;
    }
}
