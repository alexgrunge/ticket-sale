package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.SalesShift;
import justcloud.tickets.domain.sales.SalesTerminal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 6/26/16. */
public interface SalesShiftRepository extends PagingAndSortingRepository<SalesShift, String> {

  @Query(
      value =
          "select * from sale_shift where sales_terminal_id = ?1 AND finished = true ORDER BY date_created DESC limit 1;",
      nativeQuery = true)
  public SalesShift findLatestBySalesTerminalId(String salesTerminalId);

  @Query(
      value = "select * from sale_shift where user_id = ?1 ORDER BY date_created DESC limit 1;",
      nativeQuery = true)
  public SalesShift findLatestByUser(String userId);

  public SalesShift findBySalesTerminalAndFinished(SalesTerminal salesTerminal, Boolean finished);

  public SalesShift findByUserAndFinished(User user, Boolean finished);

  public List<SalesShift> findAllBySalesTerminalOfficeLocationAndDateCreatedBetween(
      OfficeLocation officeLocation, Date startingDate, Date endingDate);

  public List<SalesShift> findAllByUserAndDateCreatedBetween(
      User user, Date startingDate, Date endingDate);
}
