package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Municipality.class)
public abstract class Municipality_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile ListAttribute<Municipality, Settlement> settlements;
	public static volatile SingularAttribute<Municipality, String> name;
	public static volatile SingularAttribute<Municipality, State> state;

}

