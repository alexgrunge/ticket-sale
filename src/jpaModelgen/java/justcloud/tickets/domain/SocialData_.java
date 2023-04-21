package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SocialData.class)
public abstract class SocialData_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<SocialData, String> tokenId;
	public static volatile SingularAttribute<SocialData, String> providerId;
	public static volatile SingularAttribute<SocialData, SocialDataProvider> socialDataProvider;
	public static volatile SingularAttribute<SocialData, User> user;

}

