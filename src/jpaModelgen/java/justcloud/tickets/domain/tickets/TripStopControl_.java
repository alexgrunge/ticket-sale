package justcloud.tickets.domain.tickets;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TripStopControl.class)
public abstract class TripStopControl_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<TripStopControl, StopOff> stopOff;
	public static volatile SingularAttribute<TripStopControl, Trip> trip;
	public static volatile SingularAttribute<TripStopControl, Boolean> visited;
	public static volatile SingularAttribute<TripStopControl, Date> visitedTime;

}

