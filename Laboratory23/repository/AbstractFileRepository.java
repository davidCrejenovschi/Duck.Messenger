package repository;
import exceptions.repositories.RepositoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public abstract class AbstractFileRepository<T> implements FileRepository<T> {

    protected final Map<Long, T> items = new HashMap<>();

    @Override
    public void add(T item) {
        long id = getId(item);
        if (items.containsKey(id)) {
            throw new RepositoryException("Item with id " + id + " already exists.");
        }
        items.put(id, item);
    }

    @Override
    public void delete(long id) {
        if (!items.containsKey(id)) {
            throw new RepositoryException("Cannot delete: item with id " + id + " does not exist.");
        }
        items.remove(id);
    }

    @Override
    public Collection<T> getAll() {
        return items.values();
    }

    public T getById(long id) {

        if (!items.containsKey(id)) {
            throw new RepositoryException("Item with id " + id + " was not found.");
        }
        return items.get(id);
    }

    protected abstract long getId(T item);

    @Override
    public abstract void readFromFile();
    @Override
    public abstract void writeToFile();
}
