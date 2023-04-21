package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PaymentPart.class)
public abstract class PaymentPart_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<PaymentPart, String> reference;
	public static volatile SingularAttribute<PaymentPart, InternetSale> sale;
	public static volatile SingularAttribute<PaymentPart, BigDecimal> amount;
	public static volatile SingularAttribute<PaymentPart, BigDecimal> change;
	public static volatile SingularAttribute<PaymentPart, AccountSale> accountSale;
	public static volatile SingularAttribute<PaymentPart, ClientAccount> account;
	public static volatile SingularAttribute<PaymentPart, PaymentPartType> paymentType;

}

