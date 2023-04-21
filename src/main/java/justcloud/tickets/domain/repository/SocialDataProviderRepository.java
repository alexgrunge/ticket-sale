package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.SocialDataProvider;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SocialDataProviderRepository
    extends PagingAndSortingRepository<SocialDataProvider, String> {}
