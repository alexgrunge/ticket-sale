package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.RouteType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RouteTypeRepository extends PagingAndSortingRepository<RouteType, String> {}
