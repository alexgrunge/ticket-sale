package justcloud.tickets.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import justcloud.tickets.domain.*;
import justcloud.tickets.domain.repository.EmployeeAccountRepository;
import justcloud.tickets.domain.repository.EmployeeLoanPaymentRepository;
import justcloud.tickets.domain.repository.EmployeeLoanRepository;
import justcloud.tickets.domain.repository.IndividualRepository;
import justcloud.tickets.dto.EmployeeAccountData;
import justcloud.tickets.dto.EmployeeData;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

  @Autowired private EmployeeLoanRepository employeeLoanRepository;

  @Autowired private EmployeeAccountRepository employeeAccountRepository;

  @Autowired private IndividualRepository individualRepository;

  @Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;

  @Autowired private MapperFacade mapper;

  public List<EmployeeData> listOperators() {
    return individualRepository.findAllOperators().stream()
        .filter(
            individual -> {
              return individual.getActive() != null && individual.getActive();
            })
        .map(
            individual -> {
              return mapper.map(individual, EmployeeData.class);
            })
        .collect(Collectors.toList());
  }

  public EmployeeAccountData findAccountByUserId(String userId) {
    EmployeeAccount domain = employeeAccountRepository.findByEmployeeId(userId);
    EmployeeAccountData account;

    if (domain != null) {
      account = mapper.map(domain, EmployeeAccountData.class);
      account.setLoans(
          account.getLoans().stream()
              .filter(loan -> loan.getMissingAmount().compareTo(BigDecimal.ZERO) > 0)
              .collect(Collectors.toList()));
    } else {
      account = new EmployeeAccountData();
      account.setEmployee(mapper.map(individualRepository.findOne(userId), EmployeeData.class));
      account.setDiscounts(Collections.emptyList());
      account.setLoans(Collections.emptyList());
    }
    return account;
  }

  public Map<String, Object> authorizeLoan(Map<String, Object> body) {
    String userId = (String) body.get("userId");
    BigDecimal amount = new BigDecimal((String) body.get("amount"));
    String typeString = (String) body.get("type");
    String concept = (String) body.get("concept");
    String observations = (String) body.get("observations");
    BigDecimal number = new BigDecimal((String) body.get("num"));
    EmployeeAccount domain = employeeAccountRepository.findByEmployeeId(userId);

    Individual employee = individualRepository.findOne(userId);

    if (domain == null) {
      domain = new EmployeeAccount();
      domain.setEmployee((Employee) employee);
      domain.setCurrentBalance(BigDecimal.ZERO);
      employeeAccountRepository.save(domain);
      domain.setLoans(new ArrayList<>(1));
    }

    domain.setCurrentBalance(domain.getCurrentBalance().add(amount));

    EmployeeLoan loan = new EmployeeLoan();
    loan.setPayed(false);
    loan.setAccount(domain);
    loan.setEmployee((Employee) employee);
    loan.setLoanAmount(amount);
    loan.setMissingAmount(amount);
    loan.setNumber(number);
    loan.setConcept(concept);
    loan.setObservations(observations);

    if ("percentage".equals(typeString)) {
      loan.setType(EmployeeLoan.PaymentType.PERCENTAGE);
    } else {
      loan.setType(EmployeeLoan.PaymentType.NUMBER);
    }

    employeeLoanRepository.save(loan);

    domain.getLoans().add(loan);

    employeeAccountRepository.save(domain);

    return body;
  }

  public EmployeeAccountData changeAmount(Map<String, Object> loanData) {
    EmployeeLoan loan = employeeLoanRepository.findOne(loanData.getOrDefault("id", "").toString());
    BigDecimal olderMissingAmount = loan.getMissingAmount();
    loan.setMissingAmount(new BigDecimal(loanData.getOrDefault("amount", "0").toString()));
    EmployeeLoanPayment payment = new EmployeeLoanPayment();
    payment.setAccount(loan.getAccount());
    payment.setAmountPayed(loan.getMissingAmount().subtract(olderMissingAmount));
    payment.setLoan(loan);
    if (loan.getMissingAmount().compareTo(BigDecimal.ZERO) <= 0) {
      loan.setMissingAmount(BigDecimal.ZERO);
      loan.setPayed(true);
    }
    employeeLoanPaymentRepository.save(payment);
    employeeLoanRepository.save(loan);
    return findAccountByUserId(loan.getEmployee().getId());
  }
}
