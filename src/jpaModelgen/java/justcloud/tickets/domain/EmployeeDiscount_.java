package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeeDiscount.class)
public abstract class EmployeeDiscount_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<EmployeeDiscount, EmployeeLoan> loan;
	public static volatile SingularAttribute<EmployeeDiscount, Trip> trip;
	public static volatile SingularAttribute<EmployeeDiscount, BigDecimal> discountAmount;
	public static volatile SingularAttribute<EmployeeDiscount, Employee> employee;
	public static volatile SingularAttribute<EmployeeDiscount, EmployeeAccount> account;

}

