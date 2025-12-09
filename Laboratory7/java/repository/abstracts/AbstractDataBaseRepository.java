package repository.abstracts;
import exceptions.repositories.RepositoryException;
import java.util.*;

public abstract class AbstractDataBaseRepository<T> extends AbstractMemoryBridgeRepository<T> {

    @Override
    protected abstract long getId(T item);

    @Override
    public void add(T item){
        addToCache(item);
        saveOneToDatabase(item);
    }
    @Override
    public void delete(long id){

        try {
            deleteFromCache(id);
        }   catch (RepositoryException e) {
            deleteOneFromDatabase(id);
            return;
        }

        deleteOneFromDatabase(id);
    }

    @Override
    public Collection<T> getAll() {
        Collection<T> fromDb = readAllFromDatabase();
        for (T item : fromDb) {
            long id = getId(item);
            if (!items.containsKey(id)) {
                addToCache(item);
            }
        }
        return getAllFromCache();
    }
    @Override
    public T getById(long id){

        try {
            return getByIdFromCache(id);
        }catch (RepositoryException e){
            T item = readOneFromDatabase(id);
            if(item == null){
                throw e;
            }
            addToCache(item);
            return item;
        }
    }
    @Override
    public Collection<T> getByIds(List<Long> ids) {
        Collection<T> result = new ArrayList<>();
        Set<Long> idsToLoadFromDb = new HashSet<>();

        for (Long id : ids) {
            T item = items.get(id);
            if (item != null) {
                result.add(item);
            } else {
                idsToLoadFromDb.add(id);
            }
        }

        if (!idsToLoadFromDb.isEmpty()) {
            Collection<T> fromDb = getByIdsFromDatabase(idsToLoadFromDb);
            for (T item : fromDb) {
                long id = getId(item);
                if (!items.containsKey(id)) {
                    addToCache(item);
                }
                result.add(item);
            }
        }
        return result;
    }
    @Override
    public void exists(long id) throws RepositoryException {
        try {
            existInCache(id);
        }catch (RepositoryException e){
            existInDataBase(id);
        }
    }
    @Override
    public abstract void saveChangesToExternMemory();

    protected abstract Collection<T> readAllFromDatabase();
    protected abstract T readOneFromDatabase(long id);
    protected abstract Collection<T> getByIdsFromDatabase(Set<Long> ids);
    protected abstract void saveOneToDatabase(T object);
    protected abstract void deleteOneFromDatabase(long id);
    protected abstract void existInDataBase(long id);

}
