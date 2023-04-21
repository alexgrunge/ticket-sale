package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.*;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InternetSaleRepository extends PagingAndSortingRepository<InternetSale, String> {

  public InternetSale findByPaymentIdAndPaymentProvider(String paymentId, String paymentProvider);

  public InternetSale findByShortId(String shortId);

  public List<InternetSale> findAllBySalesTerminal(SalesTerminal salesTerminal);

  public List<InternetSale> findAllBySalesShift(SalesShift salesShift);

  public List<InternetSale> findAllBySalesShiftIn(List<SalesShift> salesShift);

  public List<InternetSale> findAllBySalesman(User user);

  public List<InternetSale> findAllBySalesTerminalAndCashCheckpointIsNull(
      SalesTerminal salesTerminal);

  List<InternetSale> findAllByCashCheckpoint(CashCheckpoint latest);

  InternetSale findByBillingData(BillingData billingData);
}
