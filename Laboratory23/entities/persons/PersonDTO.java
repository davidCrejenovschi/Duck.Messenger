package entities.persons;
import entities.users.UserDTO;
import java.time.LocalDate;



public class PersonDTO extends UserDTO {

    private final String firstName;
    private final String lastName;
    private final String occupation;
    private final Integer empathyLevel;
    private final LocalDate birthDate;

    public PersonDTO(long id,
                     String username,
                     String password,
                     String email,
                     String firstName,
                     String lastName,
                     String occupation,
                     Integer empathyLevel,
                     LocalDate birthDate)
    {
        super(id, username, password, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
        this.birthDate = birthDate;
    }

    public PersonDTO(UserDTO dto, String firstName, String lastName,String occupation, Integer empathyLevel, LocalDate birthDate) {
        super(dto.id(), dto.username(), dto.password(), dto.email());
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
        this.birthDate = birthDate;
    }

    public String firstName(){
        return firstName;
    }
    public String lastName(){
        return lastName;
    }
    public String occupation(){
        return occupation;
    }
    public Integer empathyLevel(){
        return empathyLevel;
    }
    public LocalDate birthDate(){
        return birthDate;
    }

}


