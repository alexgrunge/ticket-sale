package justcloud.tickets.dto;

import java.math.BigDecimal;

public class SalesTerminalData {

  private String id;

  private String stopOffName;

  private String officeName;

  private String terminalName;
  private String terminalId;

  private BigDecimal currentAmount;
  private BigDecimal currentPayedAmount;
  private BigDecimal paymentAvailableAmount;

  private Boolean salesTerminal;
  private Boolean paymentTerminal;

  public Boolean getSalesTerminal() {
    return salesTerminal;
  }

  public void setSalesTerminal(Boolean salesTerminal) {
    this.salesTerminal = salesTerminal;
  }

  public Boolean getPaymentTerminal() {
    return paymentTerminal;
  }

  public String getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(String terminalId) {
    this.terminalId = terminalId;
  }

  public void setPaymentTerminal(Boolean paymentTerminal) {
    this.paymentTerminal = paymentTerminal;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the stopOffName */
  public String getStopOffName() {
    return stopOffName;
  }

  /** @param stopOffName the stopOffName to set */
  public void setStopOffName(String stopOffName) {
    this.stopOffName = stopOffName;
  }

  /** @return the officeName */
  public String getOfficeName() {
    return officeName;
  }

  /** @param officeName the officeName to set */
  public void setOfficeName(String officeName) {
    this.officeName = officeName;
  }

  /** @return the terminalName */
  public String getTerminalName() {
    return terminalName;
  }

  /** @param terminalName the terminalName to set */
  public void setTerminalName(String terminalName) {
    this.terminalName = terminalName;
  }

  /** @return the currentAmount */
  public BigDecimal getCurrentAmount() {
    return currentAmount;
  }

  /** @param currentAmount the currentAmount to set */
  public void setCurrentAmount(BigDecimal currentAmount) {
    this.currentAmount = currentAmount;
  }

  /** @return the currentPayedAmount */
  public BigDecimal getCurrentPayedAmount() {
    return currentPayedAmount;
  }

  /** @param currentPayedAmount the currentPayedAmount to set */
  public void setCurrentPayedAmount(BigDecimal currentPayedAmount) {
    this.currentPayedAmount = currentPayedAmount;
  }

  /** @return the paymentAvailableAmount */
  public BigDecimal getPaymentAvailableAmount() {
    return paymentAvailableAmount;
  }

  /** @param paymentAvailableAmount the paymentAvailableAmount to set */
  public void setPaymentAvailableAmount(BigDecimal paymentAvailableAmount) {
    this.paymentAvailableAmount = paymentAvailableAmount;
  }
}
