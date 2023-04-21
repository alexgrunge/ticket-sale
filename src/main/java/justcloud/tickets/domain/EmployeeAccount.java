package justcloud.tickets.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class EmployeeAccount extends BaseModel {

  @OneToMany(mappedBy = "account")
  private List<EmployeeLoan> loans;

  @OneToMany(mappedBy = "account")
  private List<EmployeeDiscount> discounts;

  @ManyToOne private Employee employee;

  @NotNull private BigDecimal currentBalance;

  /** @return the currentBalance */
  public BigDecimal getCurrentBalance() {
    return currentBalance;
  }

  /** @param currentBalance the currentBalance to set */
  public void setCurrentBalance(BigDecimal currentBalance) {
    this.currentBalance = currentBalance;
  }

  /** @return the loans */
  public List<EmployeeLoan> getLoans() {
    return loans;
  }

  /** @param loans the loans to set */
  public void setLoans(List<EmployeeLoan> loans) {
    this.loans = loans;
  }

  /** @return the discounts */
  public List<EmployeeDiscount> getDiscounts() {
    return discounts;
  }

  /** @param discounts the discounts to set */
  public void setDiscounts(List<EmployeeDiscount> discounts) {
    this.discounts = discounts;
  }

  /** @return the employee */
  public Employee getEmployee() {
    return employee;
  }

  /** @param employee the employee to set */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
}
