package justcloud.tickets.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("justcloud.tickets.domain.Employee")
public class Employee extends Individual {

  private String employeeNumber;
  private String email;

  @ManyToOne private EmployeePosition employeePosition;

  /** @return the employeeNumber */
  public String getEmployeeNumber() {
    return employeeNumber;
  }

  /** @param employeeNumber the employeeNumber to set */
  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  /** @return the email */
  public String getEmail() {
    return email;
  }

  /** @param email the email to set */
  public void setEmail(String email) {
    this.email = email;
  }

  /** @return the employeePosition */
  public EmployeePosition getEmployeePosition() {
    return employeePosition;
  }

  /** @param employeePosition the employeePosition to set */
  public void setEmployeePosition(EmployeePosition employeePosition) {
    this.employeePosition = employeePosition;
  }
}
