package repositories;
import connections.DBConnection;
import entities.friendships.UserFriendship;
import entities.users.AbstractUser;
import exceptions.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipRepositoryTest {

    private FriendshipRepository repo;

    @BeforeEach
    void setUp() {

        DBConnection.setTestMode(true);
        initializeH2Database();
        repo = new FriendshipRepository();

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
    void testAddNoException() {

        PersonRepository personRepository = new PersonRepository();
        DuckRepository duckRepository = new DuckRepository();

        UserFriendship friendship = new UserFriendship(duckRepository.getById(4), personRepository.getById(8));
        assertDoesNotThrow(() -> repo.add(friendship));
        Long generatedId = friendship.getId();
        assertDoesNotThrow(() -> repo.getById(generatedId));

        System.out.println("Test passed!");
    }

    @Test
    void testAddWithException() {

        DuckRepository duckRepository = new DuckRepository();
        UserFriendship friendship = new UserFriendship(duckRepository.getById(1), duckRepository.getById(2));
        assertThrows(RepositoryException.class, () -> repo.add(friendship));
        System.out.println("Test passed!");
    }

    @Test
    void testAddMultipleFriendships() {

        DuckRepository duckRepository = new DuckRepository();
        AbstractUser sender = duckRepository.getById(1);
        AbstractUser receiver3 = duckRepository.getById(3);
        AbstractUser receiver4 = duckRepository.getById(4);
        AbstractUser receiver5 = duckRepository.getById(5);

        UserFriendship f1 = new UserFriendship(sender, receiver3);
        UserFriendship f2 = new UserFriendship(sender, receiver4);
        UserFriendship f3 = new UserFriendship(sender, receiver5);

        assertDoesNotThrow(() -> repo.add(f1));
        assertDoesNotThrow(() -> repo.add(f2));
        assertDoesNotThrow(() -> repo.add(f3));

        assertNotNull(f1.getId());
        assertNotNull(f2.getId());
        assertNotNull(f3.getId());

        UserFriendship savedF1 = repo.getById(f1.getId());
        assertEquals(sender, savedF1.getSender());
        assertEquals(receiver3, savedF1.getReceiver());

        UserFriendship savedF2 = repo.getById(f2.getId());
        assertEquals(sender, savedF2.getSender());
        assertEquals(receiver4, savedF2.getReceiver());

        UserFriendship savedF3 = repo.getById(f3.getId());
        assertEquals(sender, savedF3.getSender());
        assertEquals(receiver5, savedF3.getReceiver());
        System.out.println("Test passed!");
    }

    @Test
    void testDeleteNoException() {
        long id = 1L;
        assertDoesNotThrow(() -> repo.delete(id));

        assertThrows(RepositoryException.class, () ->repo.getById(id));
        System.out.println("Test passed!");
    }

    @Test
    void testDeleteWithException() {
        long nonExistentId = 99999L;
        assertThrows(RepositoryException.class, () -> repo.delete(nonExistentId));
        System.out.println("Test passed!");
    }

    @Test
    void testDeleteMultipleFriendships() {


        assertDoesNotThrow(() -> repo.delete(2));
        assertDoesNotThrow(() -> repo.delete(3));
        assertDoesNotThrow(() -> repo.delete(4));

        assertThrows(RepositoryException.class, () ->repo.getById(2));
        assertThrows(RepositoryException.class, () ->repo.getById(3));
        assertThrows(RepositoryException.class, () ->repo.getById(4));
        System.out.println("Test passed!");

    }

    @Test
    void testGetAll() {
        Collection<UserFriendship> all = repo.getAll();
        assertNotNull(all);
        assertEquals(12, all.size());
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdFound() {
        UserFriendship friendship = repo.getById(1L);
        assertNotNull(friendship);
        assertEquals(1L, friendship.getId());
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(RepositoryException.class, () -> repo.getById(9999L));
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdsFoundAll() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Collection<UserFriendship> result = repo.getByIds(ids);

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(f -> f.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(f -> f.getId().equals(2L)));
        assertTrue(result.stream().anyMatch(f -> f.getId().equals(3L)));
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdsPartialFound() {
        List<Long> ids = Arrays.asList(1L, 9999L);
        Collection<UserFriendship> result = repo.getByIds(ids);

        assertEquals(1, result.size());
        assertEquals(1L, result.iterator().next().getId());
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdsNoneFound() {
        List<Long> ids = Arrays.asList(8888L, 9999L);
        Collection<UserFriendship> result = repo.getByIds(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        System.out.println("Test passed!");
    }

    @Test
    void testGetByIdsEmptyInput() {
        Collection<UserFriendship> result = repo.getByIds(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        System.out.println("Test passed!");
    }

    @Test
    void performanceTest() {

        DuckRepository duckRepository = new DuckRepository();

        UserFriendship newFriendship = new UserFriendship(duckRepository.getById(1), duckRepository.getById(4) );
        repo.add(newFriendship);
        Long addedId = newFriendship.getId();

        long start1 = System.nanoTime();
        repo.getById(addedId);
        long end1 = System.nanoTime();
        long duration1 = end1 - start1;

        long knownExistingId = 6L;
        long start2 = System.nanoTime();
        repo.getById(knownExistingId);
        long end2 = System.nanoTime();
        long duration2 = end2 - start2;

        assertTrue(duration2 > duration1, "The second retrieval time should generally be greater than the first retrieval time or caching might affect results.");
        System.out.println("Test passed!");

    }

}
