package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Individual.class)
public abstract class Individual_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Individual, String> lastName;
	public static volatile SingularAttribute<Individual, String> secondLastName;
	public static volatile SingularAttribute<Individual, Address> address;
	public static volatile SingularAttribute<Individual, String> name;
	public static volatile SingularAttribute<Individual, Boolean> active;
	public static volatile SingularAttribute<Individual, User> user;
	public static volatile SingularAttribute<Individual, String> rfc;
	public static volatile SingularAttribute<Individual, String> curp;

}

