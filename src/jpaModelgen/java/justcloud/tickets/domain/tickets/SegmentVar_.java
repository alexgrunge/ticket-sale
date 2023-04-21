package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SegmentVar.class)
public abstract class SegmentVar_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<SegmentVar, String> stringVar;
	public static volatile SingularAttribute<SegmentVar, String> varType;
	public static volatile SingularAttribute<SegmentVar, Route> route;
	public static volatile SingularAttribute<SegmentVar, BigDecimal> numericVar;
	public static volatile SingularAttribute<SegmentVar, StopOff> endingStop;
	public static volatile SingularAttribute<SegmentVar, StopOff> startingStop;
	public static volatile SingularAttribute<SegmentVar, ServiceLevelType> serviceLevelType;
	public static volatile SingularAttribute<SegmentVar, String> category;

}

