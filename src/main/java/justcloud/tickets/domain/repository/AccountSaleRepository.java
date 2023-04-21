package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.sales.AccountSale;
import justcloud.tickets.domain.sales.CashCheckpoint;
import justcloud.tickets.domain.sales.SalesShift;
import justcloud.tickets.domain.sales.SalesTerminal;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountSaleRepository extends PagingAndSortingRepository<AccountSale, String> {

  public List<AccountSale> findAllBySalesTerminal(SalesTerminal salesTerminal);

  public List<AccountSale> findAllBySalesTerminalAndCashCheckpointIsNull(
      SalesTerminal salesTerminal);

  public List<AccountSale> findAllBySalesShift(SalesShift salesShift);

  List<AccountSale> findAllByCashCheckpoint(CashCheckpoint latest);
}
