package justcloud.tickets.domain.sales;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountSale.class)
public abstract class AccountSale_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<AccountSale, CashCheckpoint> cashCheckpoint;
	public static volatile SingularAttribute<AccountSale, ClientAccount> clientAccount;
	public static volatile SingularAttribute<AccountSale, SalesTerminal> salesTerminal;
	public static volatile SingularAttribute<AccountSale, SalesShift> salesShift;
	public static volatile SingularAttribute<AccountSale, User> salesman;
	public static volatile ListAttribute<AccountSale, PaymentPart> paymentParts;

}

