package entities.users;

public class UserDTO {

    private final long id;
    private final String username;
    private final String password;
    private final  String email;

    public UserDTO(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public long id() {
        return id;
    }
    public  String username(){
        return username;
    }
    public  String password(){
        return password;
    }
    public  String email(){
        return email;
    }
    
}
