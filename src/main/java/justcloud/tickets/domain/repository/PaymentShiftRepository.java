package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.PaymentShift;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.SalesTerminal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentShiftRepository extends PagingAndSortingRepository<PaymentShift, String> {

  @Query(
      value =
          "select * from payment_shift where sales_terminal_id = ?1 AND finished = true ORDER BY date_created DESC limit 1;",
      nativeQuery = true)
  public PaymentShift findLatestBySalesTerminalId(String salesTerminalId);

  @Query(
      value = "select * from payment_shift where user_id = ?1 ORDER BY date_created DESC limit 1;",
      nativeQuery = true)
  public PaymentShift findLatestByUser(String userId);

  public PaymentShift findBySalesTerminalAndFinished(SalesTerminal salesTerminal, Boolean finished);

  public PaymentShift findBySalesTerminalIdAndFinished(String salesTerminalId, Boolean finished);

  public PaymentShift findByUserAndFinished(User currentUser, boolean b);

  public List<PaymentShift> findAllBySalesTerminalOfficeLocationAndDateCreatedBetween(
      OfficeLocation location, Date startDate, Date endDate);
}
