package repository.abstracts;
import exceptions.repositories.RepositoryException;
import repository.interfaces.FileRepository;

import java.util.Collection;
import java.util.List;


public abstract class AbstractFileRepository<T> extends AbstractRepository<T> implements FileRepository<T> {

    @Override
    public abstract void readFromFile();
    @Override
    public abstract void writeToFile();

    @Override
    public void add(T item){
        addToCache(item);
        writeToFile();
    }
    @Override
    public void delete(long id){
        deleteFromCache(id);
        writeToFile();
    }
    @Override
    public Collection<T> getAll(){
        return getAllFromCache();

    }
    @Override
    public T getById(long id){
        return getByIdFromCache(id);
    }
    @Override
    public Collection<T> getByIds(List<Long> ids){
        return getByIdsFromCache(ids);
    }
    @Override
    public void exists(long id) throws RepositoryException{
        existInCache(id);
    }
    @Override
    protected abstract long getId(T item);
}
