package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.OfficeLocation;

@Entity
public class SalesTerminal extends BaseModel {

  private String stopOffName;

  @ManyToOne private OfficeLocation officeLocation;

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

  public void setPaymentTerminal(Boolean paymentTerminal) {
    this.paymentTerminal = paymentTerminal;
  }

  /** @return the stopOffName */
  public String getStopOffName() {
    return stopOffName;
  }

  /** @param stopOffName the stopOffName to set */
  public void setStopOffName(String stopOffName) {
    this.stopOffName = stopOffName;
  }

  /** @return the officeLocation */
  public OfficeLocation getOfficeLocation() {
    return officeLocation;
  }

  /** @param officeLocation the officeLocation to set */
  public void setOfficeLocation(OfficeLocation officeLocation) {
    this.officeLocation = officeLocation;
  }

  /** @return the terminalName */
  public String getTerminalName() {
    return terminalName;
  }

  /** @param terminalName the terminalName to set */
  public void setTerminalName(String terminalName) {
    this.terminalName = terminalName;
  }

  /** @return the terminalId */
  public String getTerminalId() {
    return terminalId;
  }

  /** @param terminalId the terminalId to set */
  public void setTerminalId(String terminalId) {
    this.terminalId = terminalId;
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
