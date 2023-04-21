package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.CancelPackageEvent;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CancelPackageEventRepository
    extends PagingAndSortingRepository<CancelPackageEvent, String> {}
