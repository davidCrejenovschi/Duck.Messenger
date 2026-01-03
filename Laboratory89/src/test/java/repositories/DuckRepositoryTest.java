package repositories;
import connections.DBConnection;
import entities.users.AbstractUser;
import entities.users.Duck;
import entities.users.FlyingDuck;
import entities.users.SwimmingDuck;
import exceptions.RepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DuckRepositoryTest {

    private DuckRepository repository;
    private List<Duck> testDucks;
    private static final Random RANDOM = new Random();
    private static final int MAX_STAT = 10;
    private static final int MIN_STAT = 1;

    @BeforeEach
    void setUp() {

        DBConnection.setTestMode(true);
        initializeH2Database();
        repository = new DuckRepository();
        testDucks = createTestDucks();

    }

    private double generateRandomStat() {
        double rawValue = MIN_STAT + (MAX_STAT - MIN_STAT) * RANDOM.nextDouble();
        return Math.round(rawValue * 100.0) / 100.0;
    }

    private List<Duck> createTestDucks() {

        List<Duck> ducks = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            String username = "SwimmerDuck" + i;
            double stamina = generateRandomStat();
            double speed = generateRandomStat();

            SwimmingDuck duck = new SwimmingDuck.Builder()
                    .username(username)
                    .password("pass123")
                    .email("swim" + i + "@test.com")
                    .speed(speed)
                    .stamina(stamina)
                    .build();
            ducks.add(duck);
        }

        for (int i = 1; i <= 5; i++) {

            String username = "FlyingDuck" + i;
            double stamina = generateRandomStat();
            double speed = generateRandomStat();

            FlyingDuck duck = new FlyingDuck.Builder()
                    .username(username)
                    .password("password123")
                    .email("fly" + i + "@test.com")
                    .speed(speed)
                    .stamina(stamina)
                    .build();
            ducks.add(duck);
        }

        return ducks;
    }

    private void initializeH2Database() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/schema.sql")));

            stmt.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("Error creating tables in H2", e);
        }
    }

    @Test
    void testFindUserByUsername_ShouldLoadFriends() {

        final String username = "AirDuck_04";
        Duck foundDuck = repository.findUserByUsername(username);
        assertNotNull(foundDuck, "The main user should be found.");

        Collection<AbstractUser> friends = foundDuck.getFriends();
        assertNotNull(friends, "The friends list must not be null.");
        assertEquals(2, friends.size(), "AirDuck_04 should have exactly 2 approved friends.");

        System.out.println("Test passed! ");
    }

    @Test
    void addDuckNoException() {

        Duck duck = testDucks.getFirst();
        repository.add(duck);

        Duck duckReceive = repository.getById(duck.getId());

        assertNotNull(duckReceive, "Retrieved Duck should not be null after save operation.");

        assertEquals(duck.getId(), duckReceive.getId(), "IDs must match after retrieval.");

        assertEquals(duck.getUsername(), duckReceive.getUsername(), "Username mismatch.");
        assertEquals(duck.getEmail(), duckReceive.getEmail(), "Email mismatch.");

        assertEquals(duck.getSpeed(), duckReceive.getSpeed(), 0.001, "Speed mismatch.");
        assertEquals(duck.getStamina(), duckReceive.getStamina(), 0.001, "Stamina mismatch.");

        System.out.println("Test passed! ");
    }

    @Test
    void addDucksNoException() {

        for (Duck duck : testDucks) {
            repository.add(duck);
        }

        assertEquals(testDucks.size()+5, repository.getAll().size(),
                "The number of retrieved ducks should match the number of ducks added.");

        for (Duck expectedDuck : testDucks) {
            Duck actualDuck = repository.getById(expectedDuck.getId());

            assertNotNull(actualDuck, "Duck with ID " + expectedDuck.getId() + " was not found after saving.");

            assertEquals(expectedDuck.getId(), actualDuck.getId(), "ID mismatch for duck: " + expectedDuck.getId());
            assertEquals(expectedDuck.getUsername(), actualDuck.getUsername(), "Username mismatch for duck: " + expectedDuck.getId());
            assertEquals(expectedDuck.getEmail(), actualDuck.getEmail(), "Email mismatch for duck: " + expectedDuck.getId());

            assertEquals(expectedDuck.getSpeed(), actualDuck.getSpeed(), 0.001, "Speed mismatch for duck: " + expectedDuck.getId());
            assertEquals(expectedDuck.getStamina(), actualDuck.getStamina(), 0.001, "Stamina mismatch for duck: " + expectedDuck.getId());
        }

        System.out.println("Test passed!");
    }

    @Test
    void deleteOneDuckNoException() {

        final long ID_TO_DELETE = 1;

        assertNotNull(repository.getById(ID_TO_DELETE));

        repository.delete(ID_TO_DELETE);

        Assertions.assertThrows(RepositoryException.class, () -> repository.getById(ID_TO_DELETE));

        System.out.println("Test passed!");
    }

    @Test
    void deleteOneDuckWithException() {

        final long NON_EXISTENT_ID = 99999L;

        Assertions.assertThrows(RepositoryException.class, () -> repository.delete(NON_EXISTENT_ID));

        System.out.println("Test passed!");
    }

    @Test
    void deleteManyDucks() {

        final long[] IDS_TO_DELETE_ARRAY = {1,2,3};
        final long UNTOUCHED_ID = 5;

        for (long id : IDS_TO_DELETE_ARRAY) {
            repository.delete(id);
        }

        for (long id : IDS_TO_DELETE_ARRAY) {
            Assertions.assertThrows(RepositoryException.class, () -> repository.getById(id));
        }

        assertNotNull(repository.getById(UNTOUCHED_ID));

        System.out.println("Test passed!");
    }

    @Test
    void getAllDucks() {

        Collection<Duck> ducks = repository.getAll();

        assertEquals(5, ducks.size());

        System.out.println("Test passed!");
    }

    @Test
    void getDuckByIdFound() {

        long existingId = 4;
        Duck duck = repository.getById(existingId);

        assertNotNull(duck);
        assertEquals(existingId, duck.getId());

        System.out.println("Test passed!");
    }

    @Test
    void getDuckByIdNotFound() {

        long nonExistentId = 99999L;

        Assertions.assertThrows(RepositoryException.class, () -> repository.getById(nonExistentId));

        System.out.println("Test passed!");
    }

    @Test
    void getByIdsFound() {

        List<Long> ids = Arrays.asList(1L, 2L);
        Collection<Duck> ducks = repository.getByIds(ids);

        assertEquals(2, ducks.size());

        System.out.println("Test passed!");
    }

    @Test
    void getByIdsNotFound() {

        List<Long> ids = Arrays.asList(88888L, 99999L);
        Collection<Duck> ducks = repository.getByIds(ids);

        assertNotNull(ducks);
        assertTrue(ducks.isEmpty());

        System.out.println("Test passed!");
    }

    @Test
    void performanceCacheTest() {

        Duck duck = testDucks.getFirst();
        repository.add(duck);

        long start1 = System.nanoTime();
        repository.getById(duck.getId());
        long end1 = System.nanoTime();
        long duration1 = end1 - start1;

        long start2 = System.nanoTime();
        repository.getById(2);
        long end2 = System.nanoTime();
        long duration2 = end2 - start2;

        System.out.println("First retrieval time: " + duration1 + " ns");
        System.out.println("Second retrieval time: " + duration2 + " ns");

        assertTrue(duration2 > duration1);

        System.out.println("Test passed!");
    }
}
