package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {}
