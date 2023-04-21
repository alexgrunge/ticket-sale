package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.EmployeeDiscount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeDiscountRepository
    extends PagingAndSortingRepository<EmployeeDiscount, String> {}
