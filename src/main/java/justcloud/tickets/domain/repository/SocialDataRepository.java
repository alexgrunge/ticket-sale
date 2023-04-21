package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.SocialData;
import justcloud.tickets.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SocialDataRepository extends PagingAndSortingRepository<SocialData, String> {
  public SocialData findByProviderId(String providerId);

  public SocialData findByUser(User user);
}
