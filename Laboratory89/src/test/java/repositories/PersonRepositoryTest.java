package repositories;
import connections.DBConnection;
import entities.users.Person;
import exceptions.RepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PersonRepositoryTest {

    private PersonRepository repository;
    private List<Person> testPersons;

    @BeforeEach
    void setUp() {

        DBConnection.setTestMode(true);
        initializeH2Database();
        repository = new PersonRepository();
        testPersons = createTestPersons();

    }

    private List<Person> createTestPersons() {

        List<Person> testPersons = new ArrayList<>();

        Person p1 = new Person.Builder()
                .username("JohnDoe")
                .password("jdpass")
                .email("john.doe@test.com")
                .firstName("John")
                .lastName("Doe")
                .occupation("Software Developer")
                .empathyLevel(7)
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        Person p2 = new Person.Builder()
                .username("JaneSmith")
                .password("jspass")
                .email("jane.smith@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .occupation("Data Analyst")
                .empathyLevel(9)
                .birthDate(LocalDate.of(1995, 8, 20))
                .build();

        Person p3 = new Person.Builder()
                .username("MarkWilson")
                .password("mwpass")
                .email("mark.wilson@test.com")
                .firstName("Mark")
                .lastName("Wilson")
                .occupation("Project Manager")
                .empathyLevel(5)
                .birthDate(LocalDate.of(1985, 1, 10))
                .build();

        testPersons.add(p1);
        testPersons.add(p2);
        testPersons.add(p3);

        return testPersons;
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
    void addPersonNoException() {

        Person person = testPersons.getFirst();

        repository.add(person);

        Person personReceive = repository.getById(person.getId());

        Assertions.assertNotNull(personReceive);

        Assertions.assertEquals(person.getId(), personReceive.getId());

        System.out.println("Test passed!");
    }

    @Test
    void addMultiplePersons() {


        for (Person person : testPersons) {
            repository.add(person);
        }

        Assertions.assertEquals(5 + testPersons.size(), repository.getAll().size());

        for (Person expectedPerson : testPersons) {
            Person actualPerson = repository.getById(expectedPerson.getId());
            Assertions.assertNotNull(actualPerson);
            Assertions.assertEquals(expectedPerson.getId(), actualPerson.getId());
            Assertions.assertEquals(expectedPerson.getUsername(), actualPerson.getUsername());
        }

        System.out.println("Test passed!");
    }

    @Test
    void deleteOnePersonNoException() {

        final long ID_TO_DELETE = 6;

        Assertions.assertNotNull(repository.getById(ID_TO_DELETE));

        repository.delete(ID_TO_DELETE);

        Assertions.assertThrows(RepositoryException.class, () -> repository.getById(ID_TO_DELETE));

        System.out.println("Test passed!");
    }

    @Test
    void deleteManyPersons() {

        final long[] IDS_TO_DELETE_ARRAY = {6, 7, 8};
        final long UNTOUCHED_ID = 9;

        for (long id : IDS_TO_DELETE_ARRAY) {
            repository.delete(id);
        }

        for (long id : IDS_TO_DELETE_ARRAY) {
            Assertions.assertThrows(RepositoryException.class, () -> repository.getById(id));
        }

        Assertions.assertNotNull(repository.getById(UNTOUCHED_ID));

        System.out.println("Test passed!");
    }

    @Test
    void deleteOnePersonWithException() {

        final long NON_EXISTENT_ID = 99999;

        Assertions.assertThrows(RepositoryException.class,
                () -> repository.delete(NON_EXISTENT_ID));

        System.out.println("Test passed!");
    }

    @Test
    void getAllPersons() {

        Collection<Person> persons = repository.getAll();

        Assertions.assertEquals(5, persons.size());

        System.out.println("Test passed!");
    }

    @Test
    void getPersonByIdFound() {

        long existingId = 6;
        Person person = repository.getById(existingId);

        Assertions.assertNotNull(person);
        Assertions.assertEquals(existingId, person.getId());
        Assertions.assertEquals("John_2001", person.getUsername());
        Assertions.assertEquals("John", person.getFirstName());
        Assertions.assertEquals("Smith", person.getLastName());
        Assertions.assertEquals("Engineer", person.getOccupation());

        System.out.println("Test passed!");
    }

    @Test
    void getPersonByIdNotFound() {

        long nonExistentId = 99999L;

        Assertions.assertThrows(RepositoryException.class, () -> repository.getById(nonExistentId));

        System.out.println("Test passed!");
    }

    @Test
    void getPersonsByIdsFound() {

        List<Long> ids = Arrays.asList(6L, 7L);
        Collection<Person> persons = repository.getByIds(ids);

        Assertions.assertEquals(2, persons.size());

        System.out.println("Test passed!");
    }

    @Test
    void getPersonsByIdsNotFound() {

        List<Long> ids = Arrays.asList(88888L, 99999L);
        Collection<Person> persons = repository.getByIds(ids);

        Assertions.assertNotNull(persons);
        Assertions.assertTrue(persons.isEmpty());

        System.out.println("Test passed!");
    }

    @Test
    void performanceTest() {

        Person person = testPersons.getFirst();

        repository.add(person);

        long start1 = System.nanoTime();
        repository.getById(person.getId());
        long end1 = System.nanoTime();
        long duration1 = end1 - start1;

        long start2 = System.nanoTime();
        repository.getById(6);
        long end2 = System.nanoTime();
        long duration2 = end2 - start2;

        System.out.println("First retrieval time: " + duration1 + " ns");
        System.out.println("Second retrieval time: " + duration2 + " ns");

        Assertions.assertTrue(duration2 > duration1);

        System.out.println("Test passed!");
    }
}




