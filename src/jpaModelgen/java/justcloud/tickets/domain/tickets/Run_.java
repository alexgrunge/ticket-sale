package justcloud.tickets.domain.tickets;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Run.class)
public abstract class Run_ extends justcloud.tickets.domain.sales.Product_ {

	public static volatile SingularAttribute<Run, Route> route;
	public static volatile SingularAttribute<Run, StopOff> beginning;
	public static volatile ListAttribute<Run, ServiceTypeTime> serviceTypeTimes;
	public static volatile SingularAttribute<Run, StopOff> destination;
	public static volatile ListAttribute<Run, StopOff> stops;
	public static volatile SingularAttribute<Run, Date> validFrom;
	public static volatile SingularAttribute<Run, Date> validTo;

}

