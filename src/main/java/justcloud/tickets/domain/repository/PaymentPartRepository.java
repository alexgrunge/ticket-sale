package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.sales.PaymentPart;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentPartRepository extends PagingAndSortingRepository<PaymentPart, String> {}
