package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.EmployeeAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeAccountRepository
    extends PagingAndSortingRepository<EmployeeAccount, String> {

  public EmployeeAccount findByEmployeeId(String userId);
}
