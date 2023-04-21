package justcloud.tickets.dto;

import java.util.List;

public class EmployeeAccountData {

  private List<EmployeeLoanData> loans;
  private List<EmployeeDiscountData> discounts;
  private EmployeeData employee;

  /** @return the loans */
  public List<EmployeeLoanData> getLoans() {
    return loans;
  }

  /** @param loans the loans to set */
  public void setLoans(List<EmployeeLoanData> loans) {
    this.loans = loans;
  }

  /** @return the discounts */
  public List<EmployeeDiscountData> getDiscounts() {
    return discounts;
  }

  /** @param discounts the discounts to set */
  public void setDiscounts(List<EmployeeDiscountData> discounts) {
    this.discounts = discounts;
  }

  /** @return the employee */
  public EmployeeData getEmployee() {
    return employee;
  }

  /** @param employee the employee to set */
  public void setEmployee(EmployeeData employee) {
    this.employee = employee;
  }
}
