package services;

import entities.persons.Person;
import entities.persons.PersonDTO;
import exceptions.validators.UserValidationException;
import repository.PersonRepository;
import validators.users.PersonValidator;

import java.time.LocalDate;

public class PersonService extends UserServiceAbstract<Person, PersonDTO, PersonValidator> {

    public PersonService(PersonRepository repository, PersonValidator validator) {
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
        repository.writeToFile();
    }

    @Override
    public void updateUser(long id, PersonDTO dto) throws UserValidationException {

        Person existingPerson = repository.getById(id);

        String username = dto.username();
        if (username != null) {
            validator.validateUsername(username);
            existingPerson.setUsername(username);
        }

        String password = dto.password();
        if (password != null) {
            validator.validatePassword(password);
            existingPerson.setPassword(password);
        }

        String email = dto.email();
        if (email != null) {
            validator.validateEmail(email);
            existingPerson.setEmail(email);
        }

        String firstName = dto.firstName();
        if (firstName != null) {
            validator.validateFirstName(firstName);
            existingPerson.setFirst_name(firstName);
        }

        String lastName = dto.lastName();
        if (lastName != null) {
            validator.validateLastName(lastName);
            existingPerson.setLast_name(lastName);
        }

        String occupation = dto.occupation();
        if (occupation != null) {
            validator.validateOccupation(occupation);
            existingPerson.setOccupation(occupation);
        }

        Integer empathy = dto.empathyLevel();
        if (empathy != null) {
            validator.validateEmpathyLevel(empathy);
            existingPerson.setEmpathyLevel(empathy);
        }

        LocalDate birthDate = dto.birthDate();
        if (birthDate != null) {
            validator.validateBirthDate(birthDate);
            existingPerson.setBirth_date(birthDate);
        }

        repository.writeToFile();
    }


}
