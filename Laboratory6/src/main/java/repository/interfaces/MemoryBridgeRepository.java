package repository.interfaces;

public interface MemoryBridgeRepository<T> extends Repository<T> {

    void saveChangesToExternMemory();
}
