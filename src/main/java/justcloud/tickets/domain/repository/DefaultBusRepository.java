package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.DefaultBus;
import justcloud.tickets.domain.tickets.ServiceLevelType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DefaultBusRepository extends PagingAndSortingRepository<DefaultBus, String> {
  DefaultBus findByServiceLevel(ServiceLevelType serviceLevel);
}
