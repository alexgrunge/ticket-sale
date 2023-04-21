package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Settlement.class)
public abstract class Settlement_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Settlement, String> postalCode;
	public static volatile SingularAttribute<Settlement, Municipality> municipality;
	public static volatile SingularAttribute<Settlement, String> name;
	public static volatile SingularAttribute<Settlement, State> state;

}

