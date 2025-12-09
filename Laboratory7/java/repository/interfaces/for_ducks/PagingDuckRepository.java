package repository.interfaces.for_ducks;
import entities.dtos.users.ducks.DuckFilterDTO;
import entities.utils.Page;
import entities.utils.Pageable;
import repository.interfaces.PagingRepository;

public interface PagingDuckRepository<T> extends PagingRepository<T> {

    Page<T> findAllOnPage(Pageable pageable, DuckFilterDTO duckFilterDTO);
}
