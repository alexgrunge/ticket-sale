package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.BillingAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BillingAddressRepository
    extends PagingAndSortingRepository<BillingAddress, String> {}
