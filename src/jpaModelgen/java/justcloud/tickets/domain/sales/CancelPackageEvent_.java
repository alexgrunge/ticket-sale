package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CancelPackageEvent.class)
public abstract class CancelPackageEvent_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<CancelPackageEvent, CashCheckpoint> cashCheckpoint;
	public static volatile SingularAttribute<CancelPackageEvent, String> contactData;
	public static volatile SingularAttribute<CancelPackageEvent, StopOff> endingStop;
	public static volatile SingularAttribute<CancelPackageEvent, String> receiverName;
	public static volatile SingularAttribute<CancelPackageEvent, String> concept;
	public static volatile SingularAttribute<CancelPackageEvent, PaymentPartType> paymentType;
	public static volatile SingularAttribute<CancelPackageEvent, String> senderName;
	public static volatile SingularAttribute<CancelPackageEvent, Trip> trip;
	public static volatile SingularAttribute<CancelPackageEvent, User> cancelUser;
	public static volatile SingularAttribute<CancelPackageEvent, SalesShift> saleShift;
	public static volatile SingularAttribute<CancelPackageEvent, BigDecimal> soldPrice;
	public static volatile SingularAttribute<CancelPackageEvent, InternetSale> internetSale;
	public static volatile SingularAttribute<CancelPackageEvent, StopOff> startingStop;
	public static volatile SingularAttribute<CancelPackageEvent, Date> originalDate;
	public static volatile SingularAttribute<CancelPackageEvent, ClientAccount> account;

}

