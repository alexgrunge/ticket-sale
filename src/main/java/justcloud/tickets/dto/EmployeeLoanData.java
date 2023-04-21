package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import justcloud.tickets.domain.EmployeeLoan;

public class EmployeeLoanData {

  private String id;
  private BigDecimal number;
  private BigDecimal loanAmount;
  private BigDecimal missingAmount;
  private String concept;
  private String observations;
  private Boolean payed;
  private EmployeeLoan.PaymentType type;
  private Date dateCreated;
  private List<EmployeePaymentData> payments;

  public List<EmployeePaymentData> getPayments() {
    return payments;
  }

  public void setPayments(List<EmployeePaymentData> payments) {
    this.payments = payments;
  }

  /** @return the number */
  public BigDecimal getNumber() {
    return number;
  }

  /** @param number the number to set */
  public void setNumber(BigDecimal number) {
    this.number = number;
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

  /** @return the payed */
  public Boolean getPayed() {
    return payed;
  }

  /** @param payed the payed to set */
  public void setPayed(Boolean payed) {
    this.payed = payed;
  }

  /** @return the type */
  public EmployeeLoan.PaymentType getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(EmployeeLoan.PaymentType type) {
    this.type = type;
  }

  /** @return the dateCreated */
  public Date getDateCreated() {
    return dateCreated;
  }

  /** @param dateCreated the dateCreated to set */
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
