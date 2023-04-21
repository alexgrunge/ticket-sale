package justcloud.tickets.controller;

import java.util.List;
import java.util.Map;
import justcloud.tickets.dto.EmployeeAccountData;
import justcloud.tickets.dto.EmployeeData;
import justcloud.tickets.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {

  @Autowired private LoanService loanService;

  @RequestMapping("/operators")
  public List<EmployeeData> listOperators() {
    return loanService.listOperators();
  }

  @RequestMapping("/account/{userId}")
  public EmployeeAccountData getAccount(@PathVariable("userId") String userId) {
    return loanService.findAccountByUserId(userId);
  }

  @RequestMapping("/changeAmount")
  public EmployeeAccountData changeAmount(@RequestBody Map<String, Object> loanData) {
    return loanService.changeAmount(loanData);
  }

  @RequestMapping("/authorizeLoan")
  public Map<String, Object> authorizeLoan(@RequestBody Map<String, Object> body) {
    return loanService.authorizeLoan(body);
  }
}
