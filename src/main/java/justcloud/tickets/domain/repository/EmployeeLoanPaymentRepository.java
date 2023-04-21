package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.EmployeeAccount;
import justcloud.tickets.domain.EmployeeLoan;
import justcloud.tickets.domain.EmployeeLoanPayment;
import justcloud.tickets.domain.tickets.Trip;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 6/25/16. */
public interface EmployeeLoanPaymentRepository
    extends PagingAndSortingRepository<EmployeeLoanPayment, String> {

  public List<EmployeeLoanPayment> findAllByLoan(EmployeeLoan loan);

  public List<EmployeeLoanPayment> findAllByLoanId(String loanId);

  List<EmployeeLoanPayment> findAllByAccountAndTrip(EmployeeAccount account, Trip trip);
}
