package entities.users;

import java.util.HashSet;
import java.util.Set;

public abstract class User {

    long id;
    String username;
    String password;
    String email;
    Set<Long> friends = new HashSet<Long>();

    public User(long id_init, String username_init, String password_init, String email_inti) {
        id = id_init;
        username = username_init;
        password = password_init;
        email = email_inti;
    }

    public abstract void login();
    public abstract void logout();
    public abstract void sendMessage();
    public abstract void receiveMessage();

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

    public void addFriend(Long id){
        friends.add(id);
    }
    public  void removeFriend(Long id){
        friends.remove(id);
    }
    public Set<Long> getFriends() {
        return friends;
    }

}
