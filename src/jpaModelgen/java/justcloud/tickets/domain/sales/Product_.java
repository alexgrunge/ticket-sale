package justcloud.tickets.domain.sales;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, ProductStatus> status;

}

