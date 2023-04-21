package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.InternetSale;
import justcloud.tickets.domain.tickets.PassengerType;
import justcloud.tickets.domain.tickets.SeatStatus;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.domain.tickets.TripSeat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TripSeatRepository extends PagingAndSortingRepository<TripSeat, String> {

  public List<TripSeat> findAllByPassengerNameContaining(String name);

  public TripSeat findByTripAndSeatName(Trip trip, String seatName);

  public List<TripSeat> findAllByTripAndSeatName(Trip trip, String seatName);

  public TripSeat findByTicketId(String ticketId);

  public List<TripSeat> findAllByInternetSale(InternetSale internetSale);

  public List<TripSeat> findAllByTrip(Trip trip);

  public List<TripSeat> findAllByUser(User user);

  public List<TripSeat> findAllByUserAndDateCreatedBetween(User user, Date from, Date to);

  public List<TripSeat> findAllByTripAndLastUpdatedGreaterThanEqual(Trip trip, Date lastUpdated);

  public Long countByTripIdAndStatus(String tripId, SeatStatus status);

  public Integer countByTripIdAndPassengerType(String tripId, PassengerType passengerType);

  @Query(
      value =
          "SELECT ts.* FROM trip_seat ts INNER JOIN trip t ON ts.trip_id = t.id WHERE t.departure_date >= ?1 AND t.departure_date <= ?2 AND ts.last_updated >= ?3",
      nativeQuery = true)
  public List<TripSeat> findAllByTripAndDates(Date startDate, Date endDate, Date lastUpdated);

  List<TripSeat> findAllByStatus(SeatStatus reserved);
}
