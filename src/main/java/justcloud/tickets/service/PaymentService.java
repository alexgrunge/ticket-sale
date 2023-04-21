package justcloud.tickets.service;

import java.math.BigDecimal;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.SaleRequest;

public interface PaymentService {

  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception;
}
