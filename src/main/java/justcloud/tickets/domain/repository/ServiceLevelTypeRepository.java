package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.ServiceLevelType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiceLevelTypeRepository
    extends PagingAndSortingRepository<ServiceLevelType, String> {}
