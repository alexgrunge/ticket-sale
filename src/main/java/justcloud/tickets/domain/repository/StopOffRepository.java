package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.tickets.StopOff;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StopOffRepository extends PagingAndSortingRepository<StopOff, String> {

  List<StopOff> findAllByName(String stopName);
}
