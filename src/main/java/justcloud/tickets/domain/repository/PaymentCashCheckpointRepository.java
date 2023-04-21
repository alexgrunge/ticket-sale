package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.PaymentCashCheckpoint;
import justcloud.tickets.domain.PaymentShift;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 6/30/16. */
public interface PaymentCashCheckpointRepository
    extends PagingAndSortingRepository<PaymentCashCheckpoint, String> {

  public List<PaymentCashCheckpoint> findAllByPaymentShift(PaymentShift shift);
}
