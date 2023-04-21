package justcloud.tickets.service;

import java.util.List;
import java.util.stream.Collectors;
import justcloud.tickets.domain.repository.EmployeeRepository;
import justcloud.tickets.domain.repository.UserRepository;
import justcloud.tickets.dto.EmployeeData;
import justcloud.tickets.dto.UserData;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private MapperFacade mapper;

  public List<EmployeeData> listOperators() {
    return employeeRepository.findAllByEmployeePositionId("operator-id").stream()
        .filter(
            individual -> {
              return individual.getActive() != null && individual.getActive();
            })
        .map(employee -> mapper.map(employee, EmployeeData.class))
        .collect(Collectors.toList());
  }

  public List<UserData> listSalesmen() {
    return userRepository.findAllByRoleId("taquilla").stream()
        .map(employee -> mapper.map(employee, UserData.class))
        .collect(Collectors.toList());
  }

  public List<UserData> listPayers() {
    return userRepository.findAllByRoleId("liquidador").stream()
        .map(employee -> mapper.map(employee, UserData.class))
        .collect(Collectors.toList());
  }
}
