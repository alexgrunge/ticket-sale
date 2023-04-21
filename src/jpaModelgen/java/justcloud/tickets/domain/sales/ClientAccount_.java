package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClientAccount.class)
public abstract class ClientAccount_ extends justcloud.tickets.domain.BaseNaturalModel_ {

	public static volatile SingularAttribute<ClientAccount, BigDecimal> amount;
	public static volatile SingularAttribute<ClientAccount, String> name;
	public static volatile SingularAttribute<ClientAccount, String> id;

}

