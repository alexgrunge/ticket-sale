package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.domain.tickets.TripStopControl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TripStopControlRepository
    extends PagingAndSortingRepository<TripStopControl, String> {

  public List<TripStopControl> findAllByStopOffNameAndVisited(String name, Boolean visited);

  public List<TripStopControl> findAllByTripAndLastUpdatedGreaterThanEqual(Trip trip, Date date);

  @Query(
      value =
          "SELECT tsc.* FROM trip_stop_control tsc INNER JOIN trip t ON tsc.trip_id = t.id WHERE t.departure_date >= ?1 AND t.departure_date <= ?2 AND tsc.last_updated >= ?3",
      nativeQuery = true)
  public List<TripStopControl> findAllByTripUpdated(Date startDate, Date endDate, Date lastUpdated);

  public List<TripStopControl> findAllByTrip(Trip trip);

  public TripStopControl findByTripAndStopOff(Trip trip, StopOff stopOff);
}
