package entities.dtos.users.abstracts;

import entities.dtos.users.interfaces.UserDTOI;

public abstract class UserDTO implements UserDTOI {

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

    @Override
    public long id() {
        return id;
    }

    @Override
    public  String username(){
        return username;
    }

    @Override
    public  String password(){
        return password;
    }

    @Override
    public  String email(){
        return email;
    }
    
}
