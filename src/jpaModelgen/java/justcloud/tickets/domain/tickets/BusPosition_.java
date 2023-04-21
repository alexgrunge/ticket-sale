package justcloud.tickets.domain.tickets;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BusPosition.class)
public abstract class BusPosition_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<BusPosition, String> name;
	public static volatile SingularAttribute<BusPosition, Integer> width;
	public static volatile SingularAttribute<BusPosition, Integer> column;
	public static volatile SingularAttribute<BusPosition, Integer> row;
	public static volatile SingularAttribute<BusPosition, PositionType> type;
	public static volatile SingularAttribute<BusPosition, Integer> floor;
	public static volatile SingularAttribute<BusPosition, Integer> height;

}

