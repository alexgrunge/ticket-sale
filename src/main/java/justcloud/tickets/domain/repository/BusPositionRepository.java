package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.BusPosition;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BusPositionRepository extends PagingAndSortingRepository<BusPosition, String> {}
