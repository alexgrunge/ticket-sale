package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, String> {

  Employee findByEmployeeNumber(String employeeNumber);

  List<Employee> findAllByEmployeePositionId(String id);
}
