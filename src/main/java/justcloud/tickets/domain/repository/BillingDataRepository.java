package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.BillingData;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BillingDataRepository extends PagingAndSortingRepository<BillingData, String> {

  public BillingData findByRfc(String rfc);
}
