package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OfficeLocation.class)
public abstract class OfficeLocation_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<OfficeLocation, String> address;
	public static volatile SingularAttribute<OfficeLocation, Long> currentShift;
	public static volatile SingularAttribute<OfficeLocation, BigDecimal> latitude;
	public static volatile SingularAttribute<OfficeLocation, String> name;
	public static volatile SingularAttribute<OfficeLocation, String> shiftPrefix;
	public static volatile SingularAttribute<OfficeLocation, BigDecimal> longitude;

}

