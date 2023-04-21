package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.ClientAccount;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClientAccountRepository extends PagingAndSortingRepository<ClientAccount, String> {

  ClientAccount findByName(String name);
}
