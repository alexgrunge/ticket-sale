package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Role, Integer> redirectUrlOrder;
	public static volatile SingularAttribute<Role, String> redirectUrl;
	public static volatile ListAttribute<Role, Permission> permissions;
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile SingularAttribute<Role, Boolean> active;

}

