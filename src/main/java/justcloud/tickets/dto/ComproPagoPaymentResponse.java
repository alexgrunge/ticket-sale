package justcloud.tickets.dto;

import java.util.Date;
import java.util.Map;
import lombok.ToString;

@ToString
public class ComproPagoPaymentResponse extends PaymentResponseData {

  private String shortPaymentId;
  private Date creationDate;
  private Date expirationDate;
  private Map<String, Object> paymentInstructions;

  @Override
  public boolean getDelayedResponse() {
    return true;
  }

  @Override
  public PaymentType getPaymentType() {
    return PaymentType.STORE;
  }

  @Override
  public String getPaymentProvider() {
    return "COMPROPAGO";
  }

  /** @return the shortPaymentId */
  public String getShortPaymentId() {
    return shortPaymentId;
  }

  /** @param shortPaymentId the shortPaymentId to set */
  public void setShortPaymentId(String shortPaymentId) {
    this.shortPaymentId = shortPaymentId;
  }

  /** @return the creationDate */
  public Date getCreationDate() {
    return creationDate;
  }

  /** @param creationDate the creationDate to set */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /** @return the expirationDate */
  public Date getExpirationDate() {
    return expirationDate;
  }

  /** @param expirationDate the expirationDate to set */
  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  /** @return the paymentInstructions */
  public Map<String, Object> getPaymentInstructions() {
    return paymentInstructions;
  }

  /** @param paymentInstructions the paymentInstructions to set */
  public void setPaymentInstructions(Map<String, Object> paymentInstructions) {
    this.paymentInstructions = paymentInstructions;
  }
}
