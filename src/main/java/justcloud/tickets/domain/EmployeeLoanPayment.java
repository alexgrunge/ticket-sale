package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.tickets.Trip;

/** Created by iamedu on 6/25/16. */
@Entity
public class EmployeeLoanPayment extends BaseModel {

  @ManyToOne private EmployeeAccount account;

  @ManyToOne private EmployeeLoan loan;

  @ManyToOne private Trip trip;

  private BigDecimal originalAmount;

  private BigDecimal previousAmount;

  private BigDecimal amountPayed;

  public EmployeeAccount getAccount() {
    return account;
  }

  public void setAccount(EmployeeAccount account) {
    this.account = account;
  }

  public EmployeeLoan getLoan() {
    return loan;
  }

  public void setLoan(EmployeeLoan loan) {
    this.loan = loan;
  }

  public BigDecimal getAmountPayed() {
    return amountPayed;
  }

  public void setAmountPayed(BigDecimal amountPayed) {
    this.amountPayed = amountPayed;
  }

  public Trip getTrip() {
    return trip;
  }

  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  public BigDecimal getOriginalAmount() {
    return originalAmount;
  }

  public void setOriginalAmount(BigDecimal originalAmount) {
    this.originalAmount = originalAmount;
  }

  public BigDecimal getPreviousAmount() {
    return previousAmount;
  }

  public void setPreviousAmount(BigDecimal previousAmount) {
    this.previousAmount = previousAmount;
  }
}
