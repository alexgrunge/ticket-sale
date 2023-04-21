package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Address, String> address;
	public static volatile SingularAttribute<Address, String> postalCode;
	public static volatile SingularAttribute<Address, Municipality> municipality;
	public static volatile SingularAttribute<Address, State> state;
	public static volatile SingularAttribute<Address, Settlement> settlement;

}

