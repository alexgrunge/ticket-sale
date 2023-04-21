package justcloud.tickets.dto;

import lombok.ToString;

@ToString
public class BoothPaymentResponse extends PaymentResponseData {

  private String paymentMethod;

  @Override
  public String getPaymentProvider() {
    return "BOOTH";
  }

  @Override
  public boolean getDelayedResponse() {
    return false;
  }

  @Override
  public PaymentType getPaymentType() {
    return PaymentType.BOOTH;
  }

  /** @return the paymentMethod */
  public String getPaymentMethod() {
    return paymentMethod;
  }

  /** @param paymentMethod the paymentMethod to set */
  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
