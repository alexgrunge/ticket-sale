package justcloud.tickets.dto;

import java.util.Map;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class MastercardGatewayPaymentResponse extends PaymentResponseData {

  private Map<String, Object> serverResponse;

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
