package entities.dtos;
import java.time.LocalDate;
import java.util.Optional;

public class PersonDTO extends UserDTO {

    private String firstName;
    private String lastName;
    private String occupation;
    private Integer empathyLevel;
    private LocalDate birthDate;

    public Optional<String> firstName() {
        return Optional.ofNullable(firstName);
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Optional<String> lastName() {
        return Optional.ofNullable(lastName);
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Optional<String> occupation() {
        return Optional.ofNullable(occupation);
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Optional<Integer> empathyLevel() {
        return Optional.ofNullable(empathyLevel);
    }
    public void setEmpathyLevel(Integer empathyLevel) {
        this.empathyLevel = empathyLevel;
    }

    public Optional<LocalDate> birthDate() {
        return Optional.ofNullable(birthDate);
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}