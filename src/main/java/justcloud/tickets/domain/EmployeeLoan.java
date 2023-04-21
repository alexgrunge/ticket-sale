package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.tickets.Trip;

@Entity
public class EmployeeLoan extends BaseModel {

  public static enum PaymentType {
    PERCENTAGE,
    NUMBER
  }

  @ManyToOne private EmployeeAccount account;

  @ManyToOne private Employee employee;

  @ManyToOne private Trip trip;

  @ManyToOne private Trip payedTrip;

  private String concept;

  private String observations;

  private BigDecimal loanAmount;

  private BigDecimal missingAmount;

  private BigDecimal number;

  private Boolean payed;

  @Column(name = "payment_type")
  private PaymentType type;

  /** @return the account */
  public EmployeeAccount getAccount() {
    return account;
  }

  /** @param account the account to set */
  public void setAccount(EmployeeAccount account) {
    this.account = account;
  }

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

  /** @return the concept */
  public String getConcept() {
    return concept;
  }

  /** @param concept the concept to set */
  public void setConcept(String concept) {
    this.concept = concept;
  }

  /** @return the observations */
  public String getObservations() {
    return observations;
  }

  /** @param observations the observations to set */
  public void setObservations(String observations) {
    this.observations = observations;
  }

  /** @return the loanAmount */
  public BigDecimal getLoanAmount() {
    return loanAmount;
  }

  /** @param loanAmount the loanAmount to set */
  public void setLoanAmount(BigDecimal loanAmount) {
    this.loanAmount = loanAmount;
  }

  /** @return the missingAmount */
  public BigDecimal getMissingAmount() {
    return missingAmount;
  }

  /** @param missingAmount the missingAmount to set */
  public void setMissingAmount(BigDecimal missingAmount) {
    this.missingAmount = missingAmount;
  }

  /** @return the number */
  public BigDecimal getNumber() {
    return number;
  }

  /** @param number the number to set */
  public void setNumber(BigDecimal number) {
    this.number = number;
  }

  /** @return the payed */
  public Boolean getPayed() {
    return payed;
  }

  /** @param payed the payed to set */
  public void setPayed(Boolean payed) {
    this.payed = payed;
  }

  /** @return the type */
  public PaymentType getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(PaymentType type) {
    this.type = type;
  }

  public Trip getPayedTrip() {
    return payedTrip;
  }

  public void setPayedTrip(Trip payedTrip) {
    this.payedTrip = payedTrip;
  }
}
