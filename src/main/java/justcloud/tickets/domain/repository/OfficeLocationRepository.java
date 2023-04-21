package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.OfficeLocation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfficeLocationRepository
    extends PagingAndSortingRepository<OfficeLocation, String> {}
