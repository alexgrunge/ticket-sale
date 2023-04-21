package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PackageTicket.class)
public abstract class PackageTicket_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<PackageTicket, InternetSale> sale;
	public static volatile SingularAttribute<PackageTicket, String> senderName;
	public static volatile SingularAttribute<PackageTicket, String> contactData;
	public static volatile SingularAttribute<PackageTicket, Trip> trip;
	public static volatile SingularAttribute<PackageTicket, StopOff> endingStop;
	public static volatile SingularAttribute<PackageTicket, BigDecimal> price;
	public static volatile SingularAttribute<PackageTicket, String> receiverName;
	public static volatile SingularAttribute<PackageTicket, String> concept;
	public static volatile SingularAttribute<PackageTicket, StopOff> startingStop;
	public static volatile SingularAttribute<PackageTicket, User> user;
	public static volatile SingularAttribute<PackageTicket, String> ticketId;
	public static volatile SingularAttribute<PackageTicket, BigDecimal> paymentPrice;

}

