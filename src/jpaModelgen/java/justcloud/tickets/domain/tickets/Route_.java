package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Route.class)
public abstract class Route_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Route, BigDecimal> totalKilometers;
	public static volatile SingularAttribute<Route, Boolean> masterPrice;
	public static volatile SingularAttribute<Route, StopOff> beginning;
	public static volatile SingularAttribute<Route, StopOff> destination;
	public static volatile SingularAttribute<Route, String> name;
	public static volatile SingularAttribute<Route, String> description;
	public static volatile SingularAttribute<Route, Boolean> hasReverse;
	public static volatile ListAttribute<Route, StopOff> stops;
	public static volatile SingularAttribute<Route, RouteType> routeType;
	public static volatile SingularAttribute<Route, String> trackingId;
	public static volatile SingularAttribute<Route, BigDecimal> advance;

}

