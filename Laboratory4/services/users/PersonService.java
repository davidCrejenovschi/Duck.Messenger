package services.users;
import entities.users.persons.Person;
import entities.dtos.users.PersonDTO;
import exceptions.validators.UserValidationException;
import repository.abstracts.AbstractRepository;
import services.users.abstracts.UserServiceAbstract;
import validators.users.PersonValidator;


public class PersonService extends UserServiceAbstract<Person, PersonDTO, PersonValidator> {

    public PersonService(AbstractRepository<Person> repository, PersonValidator validator) {
        super(repository, validator);
    }

    @Override
    public void addUser(PersonDTO dto) throws UserValidationException {
        Person person = new Person(
                dto.id(),
                dto.username(),
                dto.password(),
                dto.email(),
                dto.firstName(),
                dto.lastName(),
                dto.occupation(),
                dto.empathyLevel(),
                dto.birthDate()
        );

        validator.validateUser(person);
        repository.add(person);
    }


}
