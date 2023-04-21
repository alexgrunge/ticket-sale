package justcloud.tickets.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.tickets.Trip;

@Entity
public class EmployeePeriodicPayment extends BaseModel {

  @ManyToOne private Employee employee;
  private BigDecimal payedAmount;
  private Date payedDate;
  private Date discountedDate;

  @Column(name = "payment_type")
  private String type;

  @ManyToOne private Trip payedTrip;

  private String discountWeek;

  public Trip getPayedTrip() {
    return payedTrip;
  }

  public void setPayedTrip(Trip payedTrip) {
    this.payedTrip = payedTrip;
  }

  public String getDiscountWeek() {
    return discountWeek;
  }

  public void setDiscountWeek(String discountWeek) {
    this.discountWeek = discountWeek;
  }

  /** @return the employee */
  public Employee getEmployee() {
    return employee;
  }

  /** @param employee the employee to set */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  /** @return the payedAmount */
  public BigDecimal getPayedAmount() {
    return payedAmount;
  }

  /** @param payedAmount the payedAmount to set */
  public void setPayedAmount(BigDecimal payedAmount) {
    this.payedAmount = payedAmount;
  }

  /** @return the payedDate */
  public Date getPayedDate() {
    return payedDate;
  }

  /** @param payedDate the payedDate to set */
  public void setPayedDate(Date payedDate) {
    this.payedDate = payedDate;
  }

  /** @return the discountedDate */
  public Date getDiscountedDate() {
    return discountedDate;
  }

  /** @param discountedDate the discountedDate to set */
  public void setDiscountedDate(Date discountedDate) {
    this.discountedDate = discountedDate;
  }

  /** @return the type */
  public String getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(String type) {
    this.type = type;
  }
}
