package justcloud.tickets.domain.tickets;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ServiceTypeTime.class)
public abstract class ServiceTypeTime_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<ServiceTypeTime, ServiceType> serviceType;
	public static volatile SingularAttribute<ServiceTypeTime, Date> departureTime;
	public static volatile ListAttribute<ServiceTypeTime, DayOfWeek> days;
	public static volatile SingularAttribute<ServiceTypeTime, ServiceLevelType> serviceLevelType;
	public static volatile SingularAttribute<ServiceTypeTime, Boolean> reverse;

}

