package entities.dtos.users;
import entities.dtos.users.abstracts.UserDTO;
import java.time.LocalDate;
import java.util.Optional;

public class PersonDTO extends UserDTO {

    private final String firstName;
    private final String lastName;
    private final String occupation;
    private final Integer empathyLevel;
    private final LocalDate birthDate;

    public PersonDTO(
            Long id,
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String occupation,
            Integer empathyLevel,
            LocalDate birthDate
    ) {
        super(id, username, password, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
        this.birthDate = birthDate;
    }

    public Optional<String> firstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> lastName() {
        return Optional.ofNullable(lastName);
    }

    public Optional<String> occupation() {
        return Optional.ofNullable(occupation);
    }

    public Optional<Integer> empathyLevel() {
        return Optional.ofNullable(empathyLevel);
    }

    public Optional<LocalDate> birthDate() {
        return Optional.ofNullable(birthDate);
    }
}



