package justcloud.tickets.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeePeriodicPayment.class)
public abstract class EmployeePeriodicPayment_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<EmployeePeriodicPayment, BigDecimal> payedAmount;
	public static volatile SingularAttribute<EmployeePeriodicPayment, Date> discountedDate;
	public static volatile SingularAttribute<EmployeePeriodicPayment, String> discountWeek;
	public static volatile SingularAttribute<EmployeePeriodicPayment, Date> payedDate;
	public static volatile SingularAttribute<EmployeePeriodicPayment, Trip> payedTrip;
	public static volatile SingularAttribute<EmployeePeriodicPayment, Employee> employee;
	public static volatile SingularAttribute<EmployeePeriodicPayment, String> type;

}

