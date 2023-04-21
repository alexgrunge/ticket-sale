package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.ServiceTypeTime;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiceTypeTimeRepository
    extends PagingAndSortingRepository<ServiceTypeTime, String> {}
