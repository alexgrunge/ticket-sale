package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.PaymentCashCheckpoint;
import justcloud.tickets.domain.PaymentShift;
import justcloud.tickets.domain.sales.JoinedPayment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 10/8/16. */
public interface JoinedPaymentRepository extends PagingAndSortingRepository<JoinedPayment, String> {

  List<JoinedPayment> findAllByPaymentShiftAndPaymentCashCheckpointIsNull(
      PaymentShift paymentShift);

  List<JoinedPayment> findAllByPaymentShift(PaymentShift shift);

  List<JoinedPayment> findAllByPaymentCashCheckpoint(PaymentCashCheckpoint latest);

  @Query(
      value =
          "SELECT t.* FROM joined_payment t INNER JOIN payment_shift ps ON t.payment_shift_id = ps.id WHERE ps.user_id = ?1",
      nativeQuery = true)
  List<JoinedPayment> findAllByUserId(String userId);
}
