package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.Bus;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BusRepository extends PagingAndSortingRepository<Bus, String> {

  public Bus findByGps(String gps);
}
