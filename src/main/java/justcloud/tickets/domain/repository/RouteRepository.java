package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.Route;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RouteRepository extends PagingAndSortingRepository<Route, String> {}
