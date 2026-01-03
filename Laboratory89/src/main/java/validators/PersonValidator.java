package validators;
import entities.dtos.PersonDTO;
import entities.users.Person;
import exceptions.ValidationException;

import java.time.LocalDate;
import java.time.Period;

public class PersonValidator extends AbstractUserValidator{

    public String validateFirstName(String firstName) throws ValidationException {

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException("First name is required.");
        }
        return firstName;
    }

    public String validateLastName(String lastName) throws ValidationException {

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException("Last name is required.");
        }
        return lastName;
    }

    public String validateOccupation(String occupation) throws ValidationException {

        if (occupation == null || occupation.trim().isEmpty()) {
            throw new ValidationException("Occupation is required.");
        }
        return occupation;
    }

    public int validateEmpathyLevel(int empathy) throws ValidationException {

        if (empathy < 0 || empathy > 100) {
            throw new ValidationException("Empathy level must be between 0 and 100.");
        }
        return empathy;
    }

    public LocalDate validateBirthDate(LocalDate bd) throws ValidationException {

        if (bd == null) {
            throw new ValidationException("Birth date is required.");
        }
        LocalDate today = LocalDate.now();
        if (bd.isAfter(today)) {
            throw new ValidationException("Birth date cannot be in the future.");
        }
        int age = Period.between(bd, today).getYears();
        if (age < 0 || age > 150) {
            throw new ValidationException("Birth date results in invalid age: " + age);
        }
        return bd;
    }

    public Person validatePerson(PersonDTO personDTO) throws ValidationException {

        if (personDTO == null) {
            throw new ValidationException("PersonDTO is required.");
        }

        String username = validateUsername(personDTO.username().orElse(null));
        String email = validateEmail(personDTO.email().orElse(null));
        String password = validatePassword(personDTO.password().orElse(null));
        String firstName = validateFirstName(personDTO.firstName().orElse(null));
        String lastName = validateLastName(personDTO.lastName().orElse(null));
        LocalDate birthDate = validateBirthDate(personDTO.birthDate().orElse(null));
        String occupation =  validateOccupation(personDTO.occupation().orElse(null));
        int empathyLevel = validateEmpathyLevel(personDTO.empathyLevel().orElse(-1));

        Person.Builder builder = new Person.Builder();
        builder.username(username);
        builder.email(email);
        builder.password(password);
        builder.firstName(firstName);
        builder.lastName(lastName);
        builder.birthDate(birthDate);
        builder.occupation(occupation);
        builder.empathyLevel(empathyLevel);
        return builder.build();
    }
}
