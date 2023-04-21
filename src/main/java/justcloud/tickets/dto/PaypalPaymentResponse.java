package justcloud.tickets.dto;

import lombok.ToString;

@ToString
public class PaypalPaymentResponse extends PaymentResponseData {

  private String approvalUrl;
  private String selfUrl;

  @Override
  public String getPaymentProvider() {
    return "PAYPAL";
  }

  @Override
  public boolean getDelayedResponse() {
    return true;
  }

  @Override
  public PaymentType getPaymentType() {
    return PaymentType.PAYPAL;
  }

  /** @return the approvalUrl */
  public String getApprovalUrl() {
    return approvalUrl;
  }

  /** @param approvalUrl the approvalUrl to set */
  public void setApprovalUrl(String approvalUrl) {
    this.approvalUrl = approvalUrl;
  }

  /** @return the selfUrl */
  public String getSelfUrl() {
    return selfUrl;
  }

  /** @param selfUrl the selfUrl to set */
  public void setSelfUrl(String selfUrl) {
    this.selfUrl = selfUrl;
  }
}
