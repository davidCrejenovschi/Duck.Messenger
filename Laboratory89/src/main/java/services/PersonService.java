package services;
import entities.dtos.PersonDTO;
import entities.users.Person;
import exceptions.ValidationException;
import repositories.PersonRepository;
import repositories.URepository;
import validators.PersonValidator;
import java.util.Collection;
import java.util.List;

public class PersonService {

    private final URepository<Person> personRepository;
    private final PersonValidator personValidator;

    public PersonService(PersonRepository personRepository, PersonValidator personValidator) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
    }

    public void addPerson(PersonDTO personDTO) throws ValidationException {

        Person newPerson = personValidator.validatePerson(personDTO);
        personRepository.add(newPerson);
    }

    public void deletePerson(long id) {
        personRepository.delete(id);
    }

    public void getPersonById(long id) {
        personRepository.getById(id);
    }

    public Collection<Person> getAllPersons() {
        return personRepository.getAll();
    }

    public Collection<Person> getPersonsByIds(List<Long> ids) {
        return personRepository.getByIds(ids);
    }

    public Person findPersonByUsername(String username) {
        return personRepository.findUserByUsername(username);
    }
}
