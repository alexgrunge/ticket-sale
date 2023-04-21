package justcloud.tickets.dto;

public class SaleResponse {

  private String id;
  private String shortId;
  private PaymentResponseData payment;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the shortId */
  public String getShortId() {
    return shortId;
  }

  /** @param shortId the shortId to set */
  public void setShortId(String shortId) {
    this.shortId = shortId;
  }

  /** @return the payment */
  public PaymentResponseData getPayment() {
    return payment;
  }

  /** @param payment the payment to set */
  public void setPayment(PaymentResponseData payment) {
    this.payment = payment;
  }
}
