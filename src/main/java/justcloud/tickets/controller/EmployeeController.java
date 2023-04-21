package justcloud.tickets.controller;

import java.util.List;
import justcloud.tickets.dto.EmployeeData;
import justcloud.tickets.dto.UserData;
import justcloud.tickets.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  @Autowired private EmployeeService employeeService;

  @RequestMapping("/operators")
  public List<EmployeeData> listOperators() {
    return employeeService.listOperators();
  }

  @RequestMapping("/salesmen")
  public List<UserData> listSalesmen() {
    return employeeService.listSalesmen();
  }

  @RequestMapping("/payers")
  public List<UserData> listPayers() {
    return employeeService.listPayers();
  }
}
