package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.ScriptCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScriptCategoryRepository
    extends PagingAndSortingRepository<ScriptCategory, String> {}
