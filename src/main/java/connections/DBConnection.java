package connections;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static boolean isTestMode = false;
    private static HikariDataSource realDataSource;
    private static HikariDataSource testDataSource;
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String REAL_URL = dotenv.get("DB_URL");
    private static final String REAL_USER = dotenv.get("DB_USER");
    private static final String REAL_PASSWORD = dotenv.get("DB_PASSWORD");

    private static final String TEST_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";

    private DBConnection() {}

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closePools();
        }));
    }

    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }

    private static void initRealPool() {
        if (REAL_URL == null || REAL_USER == null || REAL_PASSWORD == null) {
            throw new RuntimeException("Datele de conectare lipsesc! Verifica fisierul .env sau variabilele de sistem.");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(REAL_URL);
        config.setUsername(REAL_USER);
        config.setPassword(REAL_PASSWORD);

        config.setMaximumPoolSize(15);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        realDataSource = new HikariDataSource(config);
    }

    private static void initTestPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(TEST_URL);
        config.setUsername("sa");
        config.setPassword("");

        config.setMaximumPoolSize(5);

        testDataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (isTestMode) {
            if (testDataSource == null) {
                initTestPool();
            }
            return testDataSource.getConnection();
        } else {
            if (realDataSource == null) {
                initRealPool();
            }
            return realDataSource.getConnection();
        }
    }


    public static void closePools() {
        if (realDataSource != null && !realDataSource.isClosed()) {
            realDataSource.close();
            System.out.println("[DBConnection] Pool-ul real a fost inchis.");
        }
        if (testDataSource != null && !testDataSource.isClosed()) {
            testDataSource.close();
            System.out.println("[DBConnection] Pool-ul de test a fost inchis.");
        }
    }
}