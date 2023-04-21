package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.tickets.Trip;

@Entity
public class EmployeeDiscount extends BaseModel {

  @ManyToOne private Employee employee;

  @ManyToOne private Trip trip;

  @ManyToOne private EmployeeLoan loan;

  @ManyToOne private EmployeeAccount account;

  private BigDecimal discountAmount;

  /** @return the employee */
  public Employee getEmployee() {
    return employee;
  }

  /** @param employee the employee to set */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  /** @return the trip */
  public Trip getTrip() {
    return trip;
  }

  /** @param trip the trip to set */
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  /** @return the loan */
  public EmployeeLoan getLoan() {
    return loan;
  }

  /** @param loan the loan to set */
  public void setLoan(EmployeeLoan loan) {
    this.loan = loan;
  }

  /** @return the account */
  public EmployeeAccount getAccount() {
    return account;
  }

  /** @param account the account to set */
  public void setAccount(EmployeeAccount account) {
    this.account = account;
  }

  /** @return the discountAmount */
  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  /** @param discountAmount the discountAmount to set */
  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
  }
}
