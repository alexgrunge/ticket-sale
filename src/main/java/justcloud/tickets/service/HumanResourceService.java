package justcloud.tickets.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.EmployeePeriodicPayment;
import justcloud.tickets.domain.repository.EmployeePeriodicPaymentRepository;
import justcloud.tickets.domain.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanResourceService {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private EmployeePeriodicPaymentRepository employeePeriodicPaymentRepository;

  private Logger logger = LoggerFactory.getLogger(HumanResourceService.class);

  public Map<String, Object> processPayments(InputStream inputStream, String type)
      throws IOException {
    Map<String, Object> result = new HashMap<>();
    List<String> lines = read(inputStream);
    List<List<String>> records =
        lines.stream()
            .skip(1l)
            .map(
                (line) -> {
                  return Arrays.asList(line.split(";")).stream()
                      .map(String::trim)
                      .collect(Collectors.toList());
                })
            .collect(Collectors.toList());

    List<List<Object>> invalidUsers =
        records.stream()
            .filter(
                (record) -> {
                  String employeeNumber = record.get(0);
                  return employeeRepository.findByEmployeeNumber(employeeNumber) == null;
                })
            .map(
                record -> {
                  try {
                    List<Object> newRecord = new ArrayList<Object>(record);
                    String amountString = record.get(3).replaceAll(",", "");
                    newRecord.set(3, new BigDecimal(amountString));
                    return newRecord;
                  } catch (Exception ex) {
                    logger.warn("Problem parsing line", ex);
                    return null;
                  }
                })
            .filter(it -> it != null)
            .collect(Collectors.toList());

    result.put("invalidUsers", invalidUsers);

    List<EmployeePeriodicPayment> payments =
        records.stream()
            .map(
                (item) -> {
                  String employeeNumber = item.get(0);
                  Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber);
                  String amountString = item.get(3).replaceAll(",", "");
                  String weekString = item.get(2).replaceAll(",", "");
                  String dateString = item.get(4);
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                  BigDecimal amount = new BigDecimal(amountString);

                  return Optional.ofNullable(employee)
                      .map(
                          (e) -> {
                            EmployeePeriodicPayment payment = new EmployeePeriodicPayment();
                            Date date;

                            try {
                              date = sdf.parse(dateString);
                            } catch (ParseException ex) {
                              date = null;
                            }

                            payment.setType(type);
                            payment.setEmployee(e);
                            payment.setDiscountWeek(weekString);
                            payment.setPayedAmount(amount);
                            payment.setPayedDate(date);

                            return payment;
                          });
                })
            .filter(
                (item) -> {
                  return item.isPresent();
                })
            .map((item) -> item.get())
            .collect(Collectors.toList());

    payments.forEach(
        (line) -> {
          employeePeriodicPaymentRepository
              .findAllByEmployeeEmployeeNumberAndDiscountWeekAndType(
                  line.getEmployee().getEmployeeNumber(), line.getDiscountWeek(), type)
              .forEach(data -> employeePeriodicPaymentRepository.delete(data));
          employeePeriodicPaymentRepository.save(line);
        });

    result.put(
        "payments",
        payments.stream()
            .map(
                payment -> {
                  return Arrays.asList(
                      payment.getEmployee().getEmployeeNumber(),
                      payment.getEmployee().getName() + " " + payment.getEmployee().getLastName(),
                      "",
                      payment.getPayedAmount(),
                      payment.getPayedDate());
                })
            .collect(Collectors.toList()));

    return result;
  }

  public static List<String> read(InputStream input) throws IOException {
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
      return buffer.lines().collect(Collectors.toList());
    }
  }
}
