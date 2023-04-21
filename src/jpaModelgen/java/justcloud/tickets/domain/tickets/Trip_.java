package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.sales.JoinedPayment;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Trip.class)
public abstract class Trip_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Trip, Bus> bus;
	public static volatile SingularAttribute<Trip, Boolean> currentlyTravelling;
	public static volatile SingularAttribute<Trip, Boolean> guideGenerated;
	public static volatile SingularAttribute<Trip, Integer> dieselLiters;
	public static volatile SingularAttribute<Trip, Boolean> packageArrived;
	public static volatile SingularAttribute<Trip, Boolean> hasAllPlaces;
	public static volatile SingularAttribute<Trip, Run> run;
	public static volatile SingularAttribute<Trip, Date> estimatedArrival;
	public static volatile SingularAttribute<Trip, ServiceLevelType> serviceLevelType;
	public static volatile SingularAttribute<Trip, String> packages;
	public static volatile SingularAttribute<Trip, Boolean> reverse;
	public static volatile ListAttribute<Trip, TripSeat> seats;
	public static volatile SingularAttribute<Trip, JoinedPayment> joinedPayment;
	public static volatile SingularAttribute<Trip, Boolean> hasAllStamps;
	public static volatile SingularAttribute<Trip, Integer> soldTickets;
	public static volatile SingularAttribute<Trip, Integer> delayedMinutes;
	public static volatile SingularAttribute<Trip, BigDecimal> dieselCost;
	public static volatile SingularAttribute<Trip, Date> departureDate;
	public static volatile SingularAttribute<Trip, Long> totalTravelMinutes;
	public static volatile SingularAttribute<Trip, Employee> driver2;
	public static volatile SingularAttribute<Trip, Boolean> offRoute;
	public static volatile SingularAttribute<Trip, TripStatus> status;
	public static volatile SingularAttribute<Trip, Employee> driver1;

}

