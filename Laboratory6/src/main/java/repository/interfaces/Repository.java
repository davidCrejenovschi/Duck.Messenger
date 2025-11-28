package repository.interfaces;
import java.util.Collection;
import java.util.List;


public interface Repository<T> {

    void add(T item);
    void delete(long id);
    Collection<T> getAll();
    T getById(long id);
    Collection<T> getByIds(List<Long> ids);
    void exists(long id);
}



