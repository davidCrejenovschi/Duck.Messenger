package entities.users;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractUser implements User {

    long id;
    String username;
    String password;
    String email;
    Set<AbstractUser> friends = new HashSet<>();

    protected AbstractUser(Builder<?> builder) {
        username = builder.username;
        password = builder.password;
        email = builder.email;
    }

    protected abstract static class Builder<T extends Builder<T>>{

        private String username;
        private String password;
        private String email;

        protected abstract T self();

        public T username(String username) {
            this.username = username;
            return self();
        }

        public T password(String password) {
            this.password = password;
            return self();
        }

        public T email(String email) {
            this.email = email;
            return self();
        }
    }

    public abstract void login();
    public abstract void logout();

    public void sendMessage(){
        System.out.println("You send a message");
    }
    public void receiveMessage(){
        System.out.println("You received a message");
    }

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void setId(long id) {
        this.id = id;
    }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

    public void addFriend(AbstractUser user) {

        if(friends.contains(user)){
            throw new IllegalArgumentException("User " + user.getUsername() +" is already friend");
        }
        if(this.equals(user)){
            throw new IllegalArgumentException("Cannot add self as friend");
        }
        friends.add(user);
    }
    public  void removeFriend(AbstractUser user) {
        if(!friends.contains(user)){
            throw new IllegalArgumentException("User " + user.getUsername() +" is not friend");
        }
        friends.remove(user);
    }
    public Set<AbstractUser> getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUser that = (AbstractUser) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

