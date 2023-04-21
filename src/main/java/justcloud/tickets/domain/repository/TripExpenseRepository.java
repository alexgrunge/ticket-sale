package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.TripExpense;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TripExpenseRepository extends PagingAndSortingRepository<TripExpense, String> {}
