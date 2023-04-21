package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.InternetSale;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TripSeat.class)
public abstract class TripSeat_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<TripSeat, String> passengerName;
	public static volatile SingularAttribute<TripSeat, String> comments;
	public static volatile SingularAttribute<TripSeat, StopOff> endingStop;
	public static volatile SingularAttribute<TripSeat, BusPosition> seat;
	public static volatile SingularAttribute<TripSeat, PassengerType> passengerType;
	public static volatile SingularAttribute<TripSeat, Trip> trip;
	public static volatile SingularAttribute<TripSeat, InternetSale> internetSale;
	public static volatile SingularAttribute<TripSeat, BigDecimal> soldPrice;
	public static volatile SingularAttribute<TripSeat, StopOff> startingStop;
	public static volatile SingularAttribute<TripSeat, String> seatName;
	public static volatile SingularAttribute<TripSeat, User> user;
	public static volatile SingularAttribute<TripSeat, String> ticketId;
	public static volatile SingularAttribute<TripSeat, SeatStatus> status;

}

