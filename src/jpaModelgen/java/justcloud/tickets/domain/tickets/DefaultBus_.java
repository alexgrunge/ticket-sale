package justcloud.tickets.domain.tickets;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DefaultBus.class)
public abstract class DefaultBus_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<DefaultBus, Boolean> wifi;
	public static volatile SingularAttribute<DefaultBus, Integer> trunkSize;
	public static volatile SingularAttribute<DefaultBus, Boolean> headphones;
	public static volatile ListAttribute<DefaultBus, BusPosition> positions;
	public static volatile SingularAttribute<DefaultBus, ServiceLevelType> serviceLevel;
	public static volatile ListAttribute<DefaultBus, BusPosition> seats;
	public static volatile SingularAttribute<DefaultBus, Boolean> electricPlugs;

}

