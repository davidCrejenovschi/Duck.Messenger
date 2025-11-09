package repository;

public interface FileRepository<T> extends Repository<T> {

    void readFromFile();
    void writeToFile();
}
