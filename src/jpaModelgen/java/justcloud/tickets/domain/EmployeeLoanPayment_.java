package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeeLoanPayment.class)
public abstract class EmployeeLoanPayment_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<EmployeeLoanPayment, EmployeeLoan> loan;
	public static volatile SingularAttribute<EmployeeLoanPayment, BigDecimal> amountPayed;
	public static volatile SingularAttribute<EmployeeLoanPayment, EmployeeAccount> account;

}

