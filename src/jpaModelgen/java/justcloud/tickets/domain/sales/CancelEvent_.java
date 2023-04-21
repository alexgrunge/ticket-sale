package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.BusPosition;
import justcloud.tickets.domain.tickets.PassengerType;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CancelEvent.class)
public abstract class CancelEvent_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<CancelEvent, CashCheckpoint> cashCheckpoint;
	public static volatile SingularAttribute<CancelEvent, String> passengerName;
	public static volatile SingularAttribute<CancelEvent, StopOff> endingStop;
	public static volatile SingularAttribute<CancelEvent, PaymentPartType> paymentType;
	public static volatile SingularAttribute<CancelEvent, BusPosition> seat;
	public static volatile SingularAttribute<CancelEvent, PassengerType> passengerType;
	public static volatile SingularAttribute<CancelEvent, Trip> trip;
	public static volatile SingularAttribute<CancelEvent, User> cancelUser;
	public static volatile SingularAttribute<CancelEvent, SalesShift> saleShift;
	public static volatile SingularAttribute<CancelEvent, BigDecimal> soldPrice;
	public static volatile SingularAttribute<CancelEvent, InternetSale> internetSale;
	public static volatile SingularAttribute<CancelEvent, StopOff> startingStop;
	public static volatile SingularAttribute<CancelEvent, Date> originalDate;
	public static volatile SingularAttribute<CancelEvent, String> seatName;
	public static volatile SingularAttribute<CancelEvent, ClientAccount> account;
	public static volatile SingularAttribute<CancelEvent, String> ticketId;

}

