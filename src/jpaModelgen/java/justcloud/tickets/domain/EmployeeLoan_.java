package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.EmployeeLoan.PaymentType;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeeLoan.class)
public abstract class EmployeeLoan_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<EmployeeLoan, BigDecimal> number;
	public static volatile SingularAttribute<EmployeeLoan, Trip> trip;
	public static volatile SingularAttribute<EmployeeLoan, String> concept;
	public static volatile SingularAttribute<EmployeeLoan, String> observations;
	public static volatile SingularAttribute<EmployeeLoan, BigDecimal> missingAmount;
	public static volatile SingularAttribute<EmployeeLoan, Trip> payedTrip;
	public static volatile SingularAttribute<EmployeeLoan, Employee> employee;
	public static volatile SingularAttribute<EmployeeLoan, PaymentType> type;
	public static volatile SingularAttribute<EmployeeLoan, EmployeeAccount> account;
	public static volatile SingularAttribute<EmployeeLoan, BigDecimal> loanAmount;
	public static volatile SingularAttribute<EmployeeLoan, Boolean> payed;

}

