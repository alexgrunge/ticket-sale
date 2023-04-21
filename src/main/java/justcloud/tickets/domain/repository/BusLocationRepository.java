package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.Bus;
import justcloud.tickets.domain.tickets.BusLocation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BusLocationRepository extends PagingAndSortingRepository<BusLocation, String> {
  BusLocation findByBus(Bus bus);
}
