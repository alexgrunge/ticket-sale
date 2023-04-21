package justcloud.tickets.service.sales;

import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.*;
import justcloud.tickets.dto.ComproPagoPaymentResponse;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.SaleRequest;
import justcloud.tickets.service.PaymentService;
import okhttp3.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("comproPago")
@Qualifier("comproPago")
public class ComproPagoPaymentService implements PaymentService {

  private Logger log = LoggerFactory.getLogger(ComproPagoPaymentService.class);

  @Autowired private Gson gson;

  @Value("${compropago.key}")
  private String comproPagoPrivateKey = "sk_test_44134871787949e36";

  @SuppressWarnings("unchecked")
  @Override
  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception {
    Map<String, Object> requestMap = new HashMap<>();
    // TODO Auto-generated method stub
    requestMap.put("order_price", amount);
    requestMap.put("order_name", "Boletos medher");
    requestMap.put("order_id", "MEDHER_TICKETS");
    requestMap.put(
        "customer_name",
        saleRequest.getStore().getName() + " " + saleRequest.getStore().getLastNames());
    requestMap.put("customer_email", saleRequest.getStore().getEmail());
    requestMap.put("payment_type", saleRequest.getStore().getPlace());
    requestMap.put("currency", "MXN");

    OkHttpClient client = new OkHttpClient();

    MediaType JSON = MediaType.parse("application/json; charset==utf-8");
    RequestBody body = RequestBody.create(JSON, gson.toJson(requestMap));

    String credential = Credentials.basic(comproPagoPrivateKey, null);
    Request clientRequest =
        new Request.Builder()
            .header("Authorization", credential)
            .header("Content-Type", "application/json")
            .url("https://api.compropago.com/v1/charges")
            .post(body)
            .build();

    String responseBody = client.newCall(clientRequest).execute().body().string();
    Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);
    log.info("Result {}", responseMap);

    requestMap = new HashMap<>();
    requestMap.put("customer_phone", saleRequest.getStore().getMobilePhone());
    body = RequestBody.create(JSON, gson.toJson(requestMap));
    clientRequest =
        new Request.Builder()
            .header("Authorization", credential)
            .header("Content-Type", "application/json")
            .url("https://api.compropago.com/v1/charges/" + responseMap.get("id") + "/sms")
            .post(body)
            .build();

    responseBody = client.newCall(clientRequest).execute().body().string();

    log.info("SMS RESUlT {}", responseBody);

    ComproPagoPaymentResponse response = new ComproPagoPaymentResponse();
    response.setAmount(amount);
    response.setAuthorized(false);

    long creationDate = new BigDecimal(responseMap.get("created").toString()).longValue() * 1000;
    long expirationDate = new BigDecimal(responseMap.get("exp_date").toString()).longValue() * 1000;

    response.setId(responseMap.get("id").toString());
    response.setShortPaymentId(responseMap.get("short_id").toString());
    response.setCreationDate(new DateTime(creationDate).toDate());
    response.setExpirationDate(new DateTime(expirationDate).toDate());
    response.setPaymentInstructions((Map<String, Object>) responseMap.get("instructions"));

    return response;
  }

  public PaymentResponseData confirmPayment(Map<String, Object> confirmation) throws Exception {
    ComproPagoPaymentResponse response = new ComproPagoPaymentResponse();

    response.setId(confirmation.get("id").toString());
    response.setShortPaymentId(confirmation.get("short_id").toString());
    response.setAuthorized((boolean) confirmation.get("paid"));
    response.setAmount(new BigDecimal(confirmation.get("amount").toString()));

    return response;
  }
}
