package justcloud.tickets.dto;

import java.math.BigDecimal;

public class PaymentPartData {

  private String paymentType;
  private String reference;
  private String coupon;
  private BigDecimal amount;
  private BigDecimal change;

  /** @return the paymentType */
  public String getPaymentType() {
    return paymentType;
  }

  /** @param paymentType the paymentType to set */
  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  /** @return the reference */
  public String getReference() {
    return reference;
  }

  /** @param reference the reference to set */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /** @return the coupon */
  public String getCoupon() {
    return coupon;
  }

  /** @param coupon the coupon to set */
  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }

  /** @return the amount */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @param amount the amount to set */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** @return the change */
  public BigDecimal getChange() {
    return change;
  }

  /** @param change the change to set */
  public void setChange(BigDecimal change) {
    this.change = change;
  }
}
