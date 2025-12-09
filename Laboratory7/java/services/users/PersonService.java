package services.users;
import entities.users.persons.Person;
import entities.dtos.users.PersonDTO;
import exceptions.validators.UserValidationException;
import repository.abstracts.AbstractMemoryBridgeRepository;
import services.users.abstracts.UserServiceAbstract;
import validators.users.PersonValidator;


public class PersonService extends UserServiceAbstract<Person, PersonDTO, PersonValidator> {

    public PersonService(AbstractMemoryBridgeRepository<Person> repository, PersonValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addUser(PersonDTO dto) throws UserValidationException {
        Person person = new Person(
                dto.id().orElseThrow(() -> new UserValidationException("ID is missing")),
                dto.username().orElseThrow(() -> new UserValidationException("Username is missing")),
                dto.password().orElseThrow(() -> new UserValidationException("Password is missing")),
                dto.email().orElseThrow(() -> new UserValidationException("Email is missing")),
                dto.firstName().orElseThrow(() -> new UserValidationException("First name is missing")),
                dto.lastName().orElseThrow(() -> new UserValidationException("Last name is missing")),
                dto.occupation().orElseThrow(() -> new UserValidationException("Occupation is missing")),
                dto.empathyLevel().orElseThrow(() -> new UserValidationException("Empathy level is missing")),
                dto.birthDate().orElseThrow(() -> new UserValidationException("Birth date is missing"))
        );
        validator.validateUser(person);
        repository.add(person);
        repository.saveChangesToExternMemory();
    }

}
