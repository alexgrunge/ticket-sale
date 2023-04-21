package justcloud.tickets.service.sales;

import java.math.BigDecimal;
import justcloud.tickets.dto.BoothPaymentResponse;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.SaleRequest;
import justcloud.tickets.service.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("booth")
@Qualifier("booth")
public class BoothPaymentService implements PaymentService {

  @Override
  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception {

    BoothPaymentResponse response = new BoothPaymentResponse();
    response.setPaymentMethod(saleRequest.getPaymentMethod());
    response.setAmount(amount);
    response.setAuthorized(true);

    return response;
  }
}
