package justcloud.tickets.dto;

import java.util.List;

public class EmployeeData {

  private String id;
  private String employeeNumber;
  private String name;
  private String lastName;
  private String secondLastName;

  private List<EmployeeLoanData> loans;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the employeeNumber */
  public String getEmployeeNumber() {
    return employeeNumber;
  }

  /** @param employeeNumber the employeeNumber to set */
  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the lastName */
  public String getLastName() {
    return lastName;
  }

  /** @param lastName the lastName to set */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /** @return the secondLastName */
  public String getSecondLastName() {
    return secondLastName;
  }

  /** @param secondLastName the secondLastName to set */
  public void setSecondLastName(String secondLastName) {
    this.secondLastName = secondLastName;
  }

  /** @return the loans */
  public List<EmployeeLoanData> getLoans() {
    return loans;
  }

  /** @param loans the loans to set */
  public void setLoans(List<EmployeeLoanData> loans) {
    this.loans = loans;
  }
}
