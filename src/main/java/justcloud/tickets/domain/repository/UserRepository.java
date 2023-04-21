package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, String> {

  @Query(
      value =
          "SELECT * FROM tickets_user WHERE id in (SELECT user_roles_id FROM tickets_user_role WHERE role_id = ?1)",
      nativeQuery = true)
  List<User> findAllByRoleId(String roleId);

  User findByUsername(String username);

  User findByEmail(String username);
}
