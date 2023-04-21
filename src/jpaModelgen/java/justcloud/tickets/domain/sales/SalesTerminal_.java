package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.OfficeLocation;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesTerminal.class)
public abstract class SalesTerminal_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<SalesTerminal, String> terminalName;
	public static volatile SingularAttribute<SalesTerminal, Boolean> paymentTerminal;
	public static volatile SingularAttribute<SalesTerminal, OfficeLocation> officeLocation;
	public static volatile SingularAttribute<SalesTerminal, BigDecimal> paymentAvailableAmount;
	public static volatile SingularAttribute<SalesTerminal, String> stopOffName;
	public static volatile SingularAttribute<SalesTerminal, Boolean> salesTerminal;
	public static volatile SingularAttribute<SalesTerminal, BigDecimal> currentAmount;
	public static volatile SingularAttribute<SalesTerminal, BigDecimal> currentPayedAmount;
	public static volatile SingularAttribute<SalesTerminal, String> terminalId;

}

