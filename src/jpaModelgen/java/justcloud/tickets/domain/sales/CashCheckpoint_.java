package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CashCheckpoint.class)
public abstract class CashCheckpoint_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<CashCheckpoint, SalesShift> salesShift;
	public static volatile SingularAttribute<CashCheckpoint, BigDecimal> newAmount;
	public static volatile SingularAttribute<CashCheckpoint, BigDecimal> previousAmount;

}

