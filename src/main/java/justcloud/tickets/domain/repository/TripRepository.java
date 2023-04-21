package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.tickets.ServiceLevelType;
import justcloud.tickets.domain.tickets.Trip;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TripRepository
    extends PagingAndSortingRepository<Trip, String>, JpaSpecificationExecutor<Trip> {

  @Query(
      value =
          "select * from trip where departure_date <= ?2 and bus_id = ?1 order by departure_date desc limit 1",
      nativeQuery = true)
  public Trip findLastByBusId(String busId, Date startDate);

  public Trip findByRunIdAndDepartureDate(String runId, Date departureDate);

  public Trip findByRunIdAndServiceLevelTypeAndDepartureDate(
      String runId, ServiceLevelType serviceLevelType, Date departureDate);

  public List<Trip> findAllByRunBeginningNameAndGuideGeneratedAndDepartureDateBetween(
      String name, Boolean guideGenerated, Date startTime, Date endTime);

  public List<Trip> findAllByRunDestinationNameAndGuideGenerated(
      String name, Boolean guideGenerated);

  @Query(
      value =
          "SELECT t.* FROM trip t INNER JOIN product r ON t.run_id = r.id INNER JOIN individual d1 ON t.driver1_id = d1.id INNER JOIN individual d2 ON t.driver2_id = d2.id INNER JOIN stop_off s ON r.beginning_id = s.id WHERE s.name LIKE '%' || ?1 || '%' AND ((d1.name LIKE '%' || ?2 || '%' or d1.last_name LIKE '%' || ?2 || '%' or d1.second_last_name LIKE '%' || ?2 || '%') or (d2.name LIKE '%' || ?2 || '%' or d2.last_name LIKE '%' || ?2 || '%' or d2.second_last_name LIKE '%' || ?2 || '%')) AND departure_date >= ?3 AND departure_date <= ?4",
      nativeQuery = true)
  public List<Trip> findTrips(
      String beginningName, String driverName, Date startDate, Date endDate);

  public List<Trip> findAllTripsByDepartureDateBetween(Date startDate, Date endDate);

  public List<Trip> findAllTripsByDepartureDateBetweenAndRunName(
      Date startDate, Date endDate, String name);

  public List<Trip> findAllByJoinedPaymentPaymentTerminalOfficeLocationAndDepartureDateBetween(
      OfficeLocation officeLocation, Date startingDate, Date endingDate);

  public List<Trip> findAllByDepartureDateBetween(Date startingDate, Date endingDate);
}
