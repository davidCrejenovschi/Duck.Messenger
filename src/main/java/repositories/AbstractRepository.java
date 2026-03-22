package repositories;
import exceptions.RepositoryException;
import java.util.*;

public abstract class AbstractRepository<T> implements Repository<T> {

    protected final Map<Long, T> items = new HashMap<>();

    @Override
    public abstract void add(T item);
    protected void addToCache(T item){
        long id = getId(item);
        if (items.containsKey(id)) {
            throw new RepositoryException("Item with id " + id + " already exists.");
        }
        items.put(id, item);
    }

    @Override
    public abstract void delete(long id);
    protected void deleteFromCache(long id){
        if (!items.containsKey(id)) {
            throw new RepositoryException("Cannot delete: item with id " + id + " does not exist.");
        }
        items.remove(id);
    }

    @Override
    public abstract Collection<T> getAll();
    protected Collection<T> getAllFromCache(){
        return items.values();
    }

    @Override
    public abstract T getById(long id);
    protected T  getByIdFromCache(long id){
        if (!items.containsKey(id)) {
            throw new RepositoryException("Item with id " + id + " was not found.");
        }
        return items.get(id);
    }

    @Override
    public abstract Collection<T> getByIds(List<Long> ids);

    protected abstract long getId(T item);
}