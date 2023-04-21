package justcloud.tickets.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseModel.class)
public abstract class BaseModel_ {

	public static volatile SingularAttribute<BaseModel, Date> lastUpdated;
	public static volatile SingularAttribute<BaseModel, Date> dateCreated;
	public static volatile SingularAttribute<BaseModel, String> id;
	public static volatile SingularAttribute<BaseModel, Long> version;

}

