package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {}
