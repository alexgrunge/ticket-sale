package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.EmployeePeriodicPayment;
import justcloud.tickets.domain.tickets.Trip;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeePeriodicPaymentRepository
    extends PagingAndSortingRepository<EmployeePeriodicPayment, String> {

  List<EmployeePeriodicPayment> findAllByEmployeeEmployeeNumberAndDiscountWeekAndType(
      String employeeNumber, String discountWeek, String type);

  List<EmployeePeriodicPayment> findAllByEmployeeAndTypeAndPayedDateIsNull(
      Employee driver1, String anticipo);

  List<EmployeePeriodicPayment> findAllByEmployeeAndTypeAndDiscountedDateIsNull(
      Employee driver2, String seguro);

  EmployeePeriodicPayment findByEmployeeAndPayedTripAndType(
      Employee driver, Trip trip, String type);
}
