package repository.interfaces;
import entities.utils.Page;
import entities.utils.Pageable;

public interface PagingRepository<T> {

    Page<T> findAllOnPage(Pageable pageable);
}
