package justcloud.tickets.service.sales;

import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import justcloud.tickets.dto.MastercardGatewayPaymentResponseData;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.SaleRequest;
import justcloud.tickets.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("mastercardGateway")
@Qualifier("mastercardGateway")
public class MastercardGatewayPaymentService implements PaymentService {

  @Autowired private Gson gson;

  @Value("${mastercard.merchantId}")
  private String mastercardMerchantId;

  @Value("${mastercard.password}")
  private String mastercardPassword;

  @Override
  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception {
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, Object> order = new HashMap<>();
    Map<String, Object> interaction = new HashMap<>();

    interaction.put("returnUrl", "http://www.turismoenomnibus.com.mx/viewTickets/" + saleId);

    order.put("id", saleId);
    order.put("amount", amount);
    order.put("currency", "MXN");

    requestMap.put("apiOperation", "CREATE_CHECKOUT_SESSION");
    requestMap.put("order", order);
    requestMap.put("interaction", interaction);

    OkHttpClient client = new OkHttpClient();

    MediaType JSON = MediaType.parse("application/json; charset==utf-8");
    RequestBody body = RequestBody.create(JSON, gson.toJson(requestMap));

    String credential = Credentials.basic("merchant." + mastercardMerchantId, mastercardPassword);
    Request clientRequest =
        new Request.Builder()
            .header("Authorization", credential)
            .header("Content-Type", "application/json")
            .url(
                "https://banamex.dialectpayments.com/api/rest/version/43/merchant/"
                    + mastercardMerchantId
                    + "/session")
            .post(body)
            .build();

    String responseBody = client.newCall(clientRequest).execute().body().string();
    Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);

    Map<String, Object> session =
        (Map<String, Object>) responseMap.getOrDefault("session", new HashMap<>());
    String sessionId = (String) session.get("id");

    MastercardGatewayPaymentResponseData responseData = new MastercardGatewayPaymentResponseData();
    responseData.setSessionId(sessionId);
    responseData.setAuthorized(false);
    responseData.setAmount(amount);
    return responseData;
  }

  public String attemptApproval(String orderId) throws Exception {
    OkHttpClient client = new OkHttpClient();

    String credential = Credentials.basic("merchant." + mastercardMerchantId, mastercardPassword);
    Request clientRequest =
        new Request.Builder()
            .header("Authorization", credential)
            .header("Content-Type", "application/json")
            .url(
                "https://banamex.dialectpayments.com/api/rest/version/43/merchant/"
                    + mastercardMerchantId
                    + "/order/"
                    + orderId)
            .get()
            .build();
    String responseBody = client.newCall(clientRequest).execute().body().string();
    log.info("Approval %s", responseBody);
    Map<String, Object> confirmation = gson.fromJson(responseBody, Map.class);

    Map<String, Object> order =
        (Map<String, Object>) confirmation.getOrDefault("order", new HashMap<String, Object>());
    String status = (String) confirmation.getOrDefault("status", "");

    return status;
  }
}
