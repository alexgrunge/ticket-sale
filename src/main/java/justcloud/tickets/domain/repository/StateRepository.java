package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.State;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StateRepository extends PagingAndSortingRepository<State, String> {}
