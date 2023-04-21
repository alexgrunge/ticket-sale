package justcloud.tickets.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.sales.SalesTerminal;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentShift.class)
public abstract class PaymentShift_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<PaymentShift, SalesTerminal> salesTerminal;
	public static volatile SingularAttribute<PaymentShift, BigDecimal> startingAmount;
	public static volatile SingularAttribute<PaymentShift, Boolean> finished;
	public static volatile SingularAttribute<PaymentShift, Date> finishDate;
	public static volatile SingularAttribute<PaymentShift, User> user;
	public static volatile SingularAttribute<PaymentShift, User> chiefUser;

}

