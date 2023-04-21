package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.Settlement;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SettlementRepository extends PagingAndSortingRepository<Settlement, String> {

  List<Settlement> findAllByPostalCode(String postalCode);
}
