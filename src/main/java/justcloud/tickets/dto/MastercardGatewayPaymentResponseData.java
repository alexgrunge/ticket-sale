package justcloud.tickets.dto;

import lombok.Data;

@Data
public class MastercardGatewayPaymentResponseData extends PaymentResponseData {

  private String sessionId;

  @Override
  public String getPaymentProvider() {
    return "mastercardGateway";
  }

  @Override
  public boolean getDelayedResponse() {
    return true;
  }

  @Override
  public PaymentType getPaymentType() {
    return PaymentType.CREDIT_CARD;
  }
}
