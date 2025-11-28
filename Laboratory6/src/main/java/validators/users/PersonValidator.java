package validators.users;
import entities.users.persons.Person;
import exceptions.validators.PersonValidationException;
import exceptions.validators.UserValidationException;
import validators.users.abstracts.UserValidatorAbstract;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class PersonValidator extends UserValidatorAbstract<Person> {

    @Override
    public void validateUser(Person user) throws UserValidationException {

        List<String> errors = new ArrayList<>();

        try {
            validateId(user.getId());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateUsername(user.getUsername());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validatePassword(user.getPassword());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateEmail(user.getEmail());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateFirstName(user.getFirst_name());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateLastName(user.getLast_name());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateOccupation(user.getOccupation());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateEmpathyLevel(user.getEmpathyLevel());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        try {
            validateBirthDate(user.getBirth_date());
        } catch (PersonValidationException ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new PersonValidationException(String.join(" ", errors));
        }
    }

    @Override
    public void validateId(long id) throws UserValidationException {

            if (id <= 0) {
                throw new PersonValidationException("Id must be a positive number.");
            }
            String idString = Long.toString(id);
            if (!idString.endsWith("01")) {
                throw new PersonValidationException("Duck Id must end with '00' (examples: 100, 200, 1300).");
            }
    }

    public void validateFirstName(String firstName) throws UserValidationException {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new PersonValidationException("First name is required.");
        }
    }

    public void validateLastName(String lastName) throws UserValidationException {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new PersonValidationException("Last name is required.");
        }
    }

    public void validateOccupation(String occupation) throws UserValidationException {
        if (occupation == null || occupation.trim().isEmpty()) {
            throw new PersonValidationException("Occupation is required.");
        }
    }

    public void validateEmpathyLevel(int empathy) throws UserValidationException {
        if (empathy < 0 || empathy > 100) {
            throw new PersonValidationException("Empathy level must be between 0 and 100.");
        }
    }

    public void validateBirthDate(LocalDate bd) throws UserValidationException {
        if (bd == null) {
            throw new PersonValidationException("Birth date is required.");
        }
        LocalDate today = LocalDate.now();
        if (bd.isAfter(today)) {
            throw new PersonValidationException("Birth date cannot be in the future.");
        }
        int age = Period.between(bd, today).getYears();
        if (age < 0 || age > 150) {
            throw new PersonValidationException("Birth date results in invalid age: " + age);
        }
    }
}
