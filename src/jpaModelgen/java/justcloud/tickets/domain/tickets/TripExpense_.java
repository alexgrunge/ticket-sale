package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import justcloud.tickets.domain.tickets.TripExpense.ExpenseType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TripExpense.class)
public abstract class TripExpense_ extends justcloud.tickets.domain.BaseModel_ {

	public static volatile SingularAttribute<TripExpense, ExpenseType> expenseType;
	public static volatile SingularAttribute<TripExpense, BigDecimal> amount;
	public static volatile SingularAttribute<TripExpense, String> comments;
	public static volatile SingularAttribute<TripExpense, String> type;

}

