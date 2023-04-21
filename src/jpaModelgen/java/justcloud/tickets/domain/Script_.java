package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Script.class)
public abstract class Script_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Script, String> variables;
	public static volatile SingularAttribute<Script, String> name;
	public static volatile SingularAttribute<Script, String> description;
	public static volatile SingularAttribute<Script, String> language;
	public static volatile SingularAttribute<Script, ScriptCategory> category;
	public static volatile SingularAttribute<Script, String> body;

}

