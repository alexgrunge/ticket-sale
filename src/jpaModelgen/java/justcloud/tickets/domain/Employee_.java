package justcloud.tickets.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Employee.class)
public abstract class Employee_ extends justcloud.tickets.domain.Individual_ {

	public static volatile SingularAttribute<Employee, EmployeePosition> employeePosition;
	public static volatile SingularAttribute<Employee, String> email;
	public static volatile SingularAttribute<Employee, String> employeeNumber;

}

