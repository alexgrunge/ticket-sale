package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> secondLastName;
	public static volatile SingularAttribute<User, String> password;
	public static volatile ListAttribute<User, Role> roles;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;

}

