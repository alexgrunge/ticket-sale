package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.ServiceType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiceTypeRepository extends PagingAndSortingRepository<ServiceType, String> {}
