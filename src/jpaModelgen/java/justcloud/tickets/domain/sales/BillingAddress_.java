package justcloud.tickets.domain.sales;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BillingAddress.class)
public abstract class BillingAddress_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<BillingAddress, String> country;
	public static volatile SingularAttribute<BillingAddress, String> address;
	public static volatile SingularAttribute<BillingAddress, String> externalNumber;
	public static volatile SingularAttribute<BillingAddress, String> postalCode;
	public static volatile SingularAttribute<BillingAddress, String> municipality;
	public static volatile SingularAttribute<BillingAddress, String> block;
	public static volatile SingularAttribute<BillingAddress, String> state;
	public static volatile SingularAttribute<BillingAddress, String> internalNumber;
	public static volatile SingularAttribute<BillingAddress, String> settlement;

}

