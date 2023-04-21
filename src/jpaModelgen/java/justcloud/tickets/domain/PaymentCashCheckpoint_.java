package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentCashCheckpoint.class)
public abstract class PaymentCashCheckpoint_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<PaymentCashCheckpoint, BigDecimal> newAmount;
	public static volatile SingularAttribute<PaymentCashCheckpoint, BigDecimal> previousAmount;
	public static volatile SingularAttribute<PaymentCashCheckpoint, PaymentShift> paymentShift;

}

