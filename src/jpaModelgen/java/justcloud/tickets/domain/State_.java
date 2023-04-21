package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(State.class)
public abstract class State_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<State, String> isoCode;
	public static volatile SingularAttribute<State, String> name;
	public static volatile ListAttribute<State, Municipality> municipalities;

}

