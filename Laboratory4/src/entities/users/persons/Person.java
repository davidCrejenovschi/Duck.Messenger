package entities.users.persons;
import entities.users.abstracts.User;

import java.time.LocalDate;

public class Person extends User {

    private String first_name;
    private String last_name;
    private LocalDate birth_date;
    private String occupation;
    private int empathyLevel;

    public Person(long id_init,
           String username_init,
           String password_init,
           String email_init,
           String first_name_init,
           String last_name_init,
           String occupation_init,
           int empathyLevel_init,
           LocalDate birth_date_init) {
        super(id_init, username_init, password_init, email_init);
        first_name = first_name_init;
        last_name = last_name_init;
        occupation = occupation_init;
        empathyLevel = empathyLevel_init;
        birth_date = birth_date_init;
    }

    @Override
    public void login(){
        return;
    }
    @Override
    public void logout(){
        return;
    }
    @Override
    public void sendMessage(){
        return;
    }
    @Override
    public void receiveMessage(){
        return;
    }

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }
    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public int getEmpathyLevel() {
        return empathyLevel;
    }
    public void setEmpathyLevel(int empathyLevel) {
        this.empathyLevel = empathyLevel;
    }

}
