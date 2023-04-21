package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.EmployeeLoan;
import justcloud.tickets.domain.tickets.Trip;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeLoanRepository extends PagingAndSortingRepository<EmployeeLoan, String> {

  public List<EmployeeLoan> findAllByEmployeeIdAndPayed(String employeeId, boolean payed);

  public List<EmployeeLoan> findAllByEmployeeIdAndPayedTrip(String employeeId, Trip trip);
}
