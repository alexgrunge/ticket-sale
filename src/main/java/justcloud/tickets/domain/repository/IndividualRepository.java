package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.Individual;
import justcloud.tickets.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IndividualRepository extends PagingAndSortingRepository<Individual, String> {

  @Query(
      value = "select * from individual where employee_position_id = 'operator-id'",
      nativeQuery = true)
  public List<Individual> findAllOperators();

  public Individual findByUser(User user);
}
