package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.tickets.Route;
import justcloud.tickets.domain.tickets.Run;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RunRepository extends PagingAndSortingRepository<Run, String> {

  @Query(
      "select r from Run r where r.route in ?1 and (r.validFrom is null or r.validFrom <= ?2) and (r.validTo is null or r.validTo >= ?2)")
  public List<Run> findValidInRoutes(List<Route> routes, Date checkDate);

  public List<Run> findByRouteId(String routeId);
}
