package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.*;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CancelEventRepository extends PagingAndSortingRepository<CancelEvent, String> {

  List<CancelEvent> findAllBySaleShiftAndCashCheckpointIsNull(SalesShift salesShift);

  List<CancelEvent> findAllBySaleShift(SalesShift salesShift);

  List<CancelEvent> findAllByCancelUserAndDateCreatedBetween(User user, Date from, Date to);

  List<CancelEvent> findAllByCashCheckpoint(CashCheckpoint cashCheckpoint);
}
