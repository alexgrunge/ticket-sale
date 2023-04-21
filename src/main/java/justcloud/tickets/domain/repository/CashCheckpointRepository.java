package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.sales.CashCheckpoint;
import justcloud.tickets.domain.sales.SalesShift;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 6/26/16. */
public interface CashCheckpointRepository
    extends PagingAndSortingRepository<CashCheckpoint, String> {
  List<CashCheckpoint> findBySalesShift(SalesShift shift);
}
