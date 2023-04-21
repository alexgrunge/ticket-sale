package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesShift.class)
public abstract class SalesShift_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<SalesShift, Long> currentSale;
	public static volatile SingularAttribute<SalesShift, String> shiftNumber;
	public static volatile SingularAttribute<SalesShift, SalesTerminal> salesTerminal;
	public static volatile SingularAttribute<SalesShift, BigDecimal> startingAmount;
	public static volatile SingularAttribute<SalesShift, Boolean> finished;
	public static volatile SingularAttribute<SalesShift, Date> finishDate;
	public static volatile SingularAttribute<SalesShift, User> user;

}

