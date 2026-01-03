package connections;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static boolean isTestMode = false;

    private static final String REAL_URL = "jdbc:postgresql://localhost:5432/Duck Man";
    private static final String REAL_USER = "kreje";
    private static final String REAL_PASSWORD = "Davidcrj08";

    private static final String TEST_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";

    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }

    public static Connection getConnection() throws SQLException {
        if (isTestMode) {
            return DriverManager.getConnection(TEST_URL, "sa", "");
        } else {
            return DriverManager.getConnection(REAL_URL, REAL_USER, REAL_PASSWORD);
        }
    }
}