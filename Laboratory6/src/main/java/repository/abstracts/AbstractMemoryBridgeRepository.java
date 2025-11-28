package repository.abstracts;
import repository.interfaces.MemoryBridgeRepository;

public abstract class AbstractMemoryBridgeRepository<T> extends AbstractRepository<T> implements MemoryBridgeRepository<T> {

    public abstract void saveChangesToExternMemory();
}
