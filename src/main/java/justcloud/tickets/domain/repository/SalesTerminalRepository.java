package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.sales.SalesTerminal;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SalesTerminalRepository extends PagingAndSortingRepository<SalesTerminal, String> {

  public SalesTerminal findByTerminalId(String terminalId);

  public List<SalesTerminal> findAllByOfficeLocationId(String officeLocationId);

  public List<SalesTerminal> findAllByOfficeLocation(OfficeLocation officeLocation);

  public List<SalesTerminal> findAllByOfficeLocationAndSalesTerminal(
      OfficeLocation officeLocation, Boolean salesTerminal);

  public List<SalesTerminal> findAllByOfficeLocationAndPaymentTerminal(
      OfficeLocation officeLocation, boolean b);
}
