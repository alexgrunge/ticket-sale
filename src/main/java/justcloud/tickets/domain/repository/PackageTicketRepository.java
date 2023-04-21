package justcloud.tickets.domain.repository;

import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.InternetSale;
import justcloud.tickets.domain.sales.PackageTicket;
import justcloud.tickets.domain.tickets.Trip;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PackageTicketRepository extends PagingAndSortingRepository<PackageTicket, String> {

  public List<PackageTicket> findAllByTrip(Trip trip);

  public List<PackageTicket> findAllByUser(User user);

  public List<PackageTicket> findAllBySenderNameContaining(String name);

  public List<PackageTicket> findAllByUserAndDateCreatedBetween(User user, Date from, Date to);

  public PackageTicket findBySale(InternetSale internetSale);

  public PackageTicket findByTicketId(String ticketId);
}
