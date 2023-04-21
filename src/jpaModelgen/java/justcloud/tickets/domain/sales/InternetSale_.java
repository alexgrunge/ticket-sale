package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(InternetSale.class)
public abstract class InternetSale_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<InternetSale, BigDecimal> payedAmount;
	public static volatile SingularAttribute<InternetSale, CashCheckpoint> cashCheckpoint;
	public static volatile SingularAttribute<InternetSale, String> shortId;
	public static volatile SingularAttribute<InternetSale, SalesShift> salesShift;
	public static volatile SingularAttribute<InternetSale, String> timeZone;
	public static volatile SingularAttribute<InternetSale, String> fullResponse;
	public static volatile SingularAttribute<InternetSale, Boolean> bill;
	public static volatile ListAttribute<InternetSale, PackageTicket> packages;
	public static volatile SingularAttribute<InternetSale, BigDecimal> changeAmount;
	public static volatile SingularAttribute<InternetSale, String> saleNumber;
	public static volatile SingularAttribute<InternetSale, String> paymentType;
	public static volatile SingularAttribute<InternetSale, BigDecimal> totalAmount;
	public static volatile SingularAttribute<InternetSale, String> paymentProvider;
	public static volatile SingularAttribute<InternetSale, String> paymentId;
	public static volatile SingularAttribute<InternetSale, SalesTerminal> salesTerminal;
	public static volatile SingularAttribute<InternetSale, String> billPdf;
	public static volatile SingularAttribute<InternetSale, User> salesman;
	public static volatile SingularAttribute<InternetSale, BillingAddress> billingAddress;
	public static volatile SingularAttribute<InternetSale, BillingData> billingData;
	public static volatile SingularAttribute<InternetSale, String> email;
	public static volatile ListAttribute<InternetSale, PaymentPart> paymentParts;
	public static volatile SingularAttribute<InternetSale, Boolean> payed;

}

