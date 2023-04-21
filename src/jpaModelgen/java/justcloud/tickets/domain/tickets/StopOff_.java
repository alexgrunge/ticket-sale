package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StopOff.class)
public abstract class StopOff_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<StopOff, Boolean> checkpoint;
	public static volatile SingularAttribute<StopOff, BigDecimal> stopPrice;
	public static volatile SingularAttribute<StopOff, Route> route;
	public static volatile SingularAttribute<StopOff, BigDecimal> kilometers;
	public static volatile SingularAttribute<StopOff, String> name;
	public static volatile SingularAttribute<StopOff, Scale> scale;
	public static volatile SingularAttribute<StopOff, String> description;
	public static volatile SingularAttribute<StopOff, Long> travelMinutes;
	public static volatile SingularAttribute<StopOff, Long> orderIndex;
	public static volatile SingularAttribute<StopOff, Long> missingMinutes;
	public static volatile SingularAttribute<StopOff, Boolean> notNecessary;
	public static volatile SingularAttribute<StopOff, Long> waitingMinutes;

}

