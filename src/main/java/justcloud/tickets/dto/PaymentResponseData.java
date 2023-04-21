package justcloud.tickets.dto;

import java.math.BigDecimal;

public abstract class PaymentResponseData {

  private String id;
  private boolean authorized;
  private BigDecimal amount;

  public abstract String getPaymentProvider();

  public abstract boolean getDelayedResponse();

  public abstract PaymentType getPaymentType();

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the authorized */
  public boolean isAuthorized() {
    return authorized;
  }

  /** @param authorized the authorized to set */
  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  /** @return the amount */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @param amount the amount to set */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
