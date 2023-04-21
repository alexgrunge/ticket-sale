package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmployeeAccount.class)
public abstract class EmployeeAccount_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile ListAttribute<EmployeeAccount, EmployeeDiscount> discounts;
	public static volatile ListAttribute<EmployeeAccount, EmployeeLoan> loans;
	public static volatile SingularAttribute<EmployeeAccount, BigDecimal> currentBalance;
	public static volatile SingularAttribute<EmployeeAccount, Employee> employee;

}

