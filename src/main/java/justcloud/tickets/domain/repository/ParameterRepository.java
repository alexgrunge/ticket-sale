package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.Parameter;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ParameterRepository extends PagingAndSortingRepository<Parameter, String> {}
