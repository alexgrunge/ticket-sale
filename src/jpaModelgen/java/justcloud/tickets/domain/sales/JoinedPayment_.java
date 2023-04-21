package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.PaymentCashCheckpoint;
import justcloud.tickets.domain.PaymentShift;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.domain.tickets.TripAdvance;
import justcloud.tickets.domain.tickets.TripExpense;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(JoinedPayment.class)
public abstract class JoinedPayment_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Nominal;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Nominal;
	public static volatile SingularAttribute<JoinedPayment, PaymentCashCheckpoint> paymentCashCheckpoint;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Earnings;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Insurance;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Earnings;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1ExpensesAdvance;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Amount;
	public static volatile ListAttribute<JoinedPayment, TripAdvance> advances;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2ExpensesAdvance;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Expenses;
	public static volatile SingularAttribute<JoinedPayment, Boolean> payed;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Loans;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Expenses;
	public static volatile SingularAttribute<JoinedPayment, String> driver1NominalWeek;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Amount;
	public static volatile SingularAttribute<JoinedPayment, String> driver2InsuranceWeek;
	public static volatile SingularAttribute<JoinedPayment, String> driver1InsuranceWeek;
	public static volatile SingularAttribute<JoinedPayment, PaymentShift> paymentShift;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver2Insurance;
	public static volatile SingularAttribute<JoinedPayment, String> driver2NominalWeek;
	public static volatile SingularAttribute<JoinedPayment, SalesTerminal> paymentTerminal;
	public static volatile ListAttribute<JoinedPayment, Trip> trips;
	public static volatile SingularAttribute<JoinedPayment, Employee> driver2;
	public static volatile SingularAttribute<JoinedPayment, BigDecimal> driver1Loans;
	public static volatile SingularAttribute<JoinedPayment, Date> payDate;
	public static volatile SingularAttribute<JoinedPayment, Employee> driver1;
	public static volatile ListAttribute<JoinedPayment, TripExpense> expenses;

}

