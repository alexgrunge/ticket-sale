package justcloud.tickets.domain.repository;

import justcloud.tickets.domain.tickets.CancelReservation;
import org.springframework.data.repository.PagingAndSortingRepository;

/** Created by iamedu on 10/22/16. */
public interface CancelReservationRepository
    extends PagingAndSortingRepository<CancelReservation, String> {}
