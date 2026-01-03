package repositories;


public interface URepository<T> extends Repository<T> {

    T findUserByUsername(String username);
}
