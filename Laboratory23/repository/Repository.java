package repository;
import java.util.Collection;


public interface Repository<T> {
    void add(T item);
    void delete(long id);
    Collection<T> getAll();
}



