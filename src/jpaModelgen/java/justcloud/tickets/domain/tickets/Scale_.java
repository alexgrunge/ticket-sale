package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Scale.class)
public abstract class Scale_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Scale, BigDecimal> latitude;
	public static volatile SingularAttribute<Scale, String> name;
	public static volatile SingularAttribute<Scale, String> description;
	public static volatile SingularAttribute<Scale, BigDecimal> longitude;

}

