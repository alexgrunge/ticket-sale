package justcloud.tickets.domain.tickets;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.Employee;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bus.class)
public abstract class Bus_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Bus, ServiceType> serviceType;
	public static volatile SingularAttribute<Bus, String> plates;
	public static volatile SingularAttribute<Bus, Integer> year;
	public static volatile ListAttribute<Bus, BusPosition> positions;
	public static volatile SingularAttribute<Bus, String> gps;
	public static volatile SingularAttribute<Bus, ServiceLevelType> serviceLevelType;
	public static volatile ListAttribute<Bus, BusPosition> seats;
	public static volatile SingularAttribute<Bus, String> busNumber;
	public static volatile SingularAttribute<Bus, String> model;
	public static volatile SingularAttribute<Bus, Employee> driver2;
	public static volatile SingularAttribute<Bus, String> brand;
	public static volatile SingularAttribute<Bus, Employee> driver1;
	public static volatile SingularAttribute<Bus, BusStatus> status;

}

