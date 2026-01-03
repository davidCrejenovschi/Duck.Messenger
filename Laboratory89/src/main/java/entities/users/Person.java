package entities.users;
import java.time.LocalDate;

public class Person extends AbstractUser {

    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    private String occupation;
    private int empathy_level;

    public Person(Builder builder) {
        super(builder);
        this.first_name = builder.firstName;
        this.last_name = builder.lastName;
        this.birth_date = builder.birthDate;
        this.occupation = builder.occupation;
        this.empathy_level = builder.empathyLevel;
    }

    public static class Builder extends  AbstractUser.Builder<Builder> {

        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String occupation;
        private int empathyLevel;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return self();
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return self();
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return self();
        }

        public Builder occupation(String occupation) {
            this.occupation = occupation;
            return self();
        }

        public Builder empathyLevel(int empathyLevel) {
            this.empathyLevel = empathyLevel;
            return self();
        }

        public Person build() {
            return new Person(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    @Override
    public void login(){}
    @Override
    public void logout(){}

    public String getFirstName() {
        return first_name;
    }
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getBirthDate() {
        return birth_date;
    }
    public void setBirthDate(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public int getEmpathyLevel() {
        return empathy_level;
    }
    public void setEmpathyLevel(int empathyLevel) {
        this.empathy_level = empathyLevel;
    }

}

