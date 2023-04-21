package justcloud.tickets.service.sales;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import justcloud.tickets.dto.CreditCardPaymentResponse;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.SaleRequest;
import justcloud.tickets.service.PaymentService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("pagoFacil")
@Qualifier("pagoFacil")
public class PagoFacilPaymentService implements PaymentService {

  private Logger log = LoggerFactory.getLogger(PagoFacilPaymentService.class);

  @Autowired private Gson gson;

  @Override
  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception {
    int maxRetries = 3;

    CreditCardPaymentResponse result = null;
    Exception e = null;

    if (1 == 1) {
      throw new RuntimeException("This sucks");
    }

    for (int i = 0; i < maxRetries; i++) {
      try {
        result = tryPayment(saleRequest, amount);
        if (result.isAuthorized()) {
          break;
        }
      } catch (Exception ex) {
        e = ex;
        log.info("PROBLEM CHARGING TO CARD", ex);
      }
    }

    if (result == null && e != null) {
      throw e;
    }

    return result;
  }

  private CreditCardPaymentResponse tryPayment(SaleRequest saleRequest, BigDecimal amount)
      throws Exception {
    Map<String, Object> requestMap = new HashMap<>();
    Map<String, Object> paramsMap = new HashMap<>();
    Map<String, Object> data = new HashMap<>();

    data.put("nombre", saleRequest.getCreditCard().getName());
    data.put("apellidos", saleRequest.getCreditCard().getLastNames());
    data.put("numeroTarjeta", saleRequest.getCreditCard().getNumber());
    data.put("cvt", saleRequest.getCreditCard().getCvc());
    data.put("mesExpiracion", fixMonth(saleRequest.getCreditCard().getExpirationMonth()));
    data.put("anyoExpiracion", saleRequest.getCreditCard().getExpirationYear());
    data.put("monto", amount);
    // data.put("monto", BigDecimal.ONE);
    data.put("idSucursal", "331fd271bcd309d08b01a45c0ed5683824d3f775");
    data.put("idUsuario", "cc626afefcb1bac00f506bf3a4d903091295aee6");
    data.put("idServicio", 3);
    data.put("telefono", saleRequest.getCreditCard().getPhone());
    // data.put("telefono", "59152054");
    data.put("celular", saleRequest.getCreditCard().getMobilePhone());
    // data.put("celular", "555555555");
    data.put("cp", saleRequest.getAddress().getPostalCode());
    data.put("calleyNumero", saleRequest.getAddress().getAddress());
    data.put("colonia", saleRequest.getAddress().getSettlement());
    data.put("municipio", saleRequest.getAddress().getMunicipality());
    data.put("estado", saleRequest.getAddress().getState());
    data.put("email", saleRequest.getEmail());
    data.put("pais", "M�xico");
    data.put("param1", "");
    data.put("param2", "");
    data.put("param3", "");
    data.put("param4", "");
    data.put("param5", "");

    paramsMap.put("data", data);

    requestMap.put("jsonrpc", "2.0");
    requestMap.put("method", "transaccion");
    requestMap.put("id", "test");
    requestMap.put("params", paramsMap);

    OkHttpClient client =
        new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    MediaType JSON = MediaType.parse("application/json; charset==utf-8");
    RequestBody body = RequestBody.create(JSON, gson.toJson(requestMap));

    Request clientRequest =
        new Request.Builder()
            .header("Content-Type", "application/json")
            .url("https://www.pagofacil.net/ws/public/Wsjtransaccion/")
            .post(body)
            .build();

    String responseBody = client.newCall(clientRequest).execute().body().string();

    log.info("DATA {}", responseBody);

    JsonObject root = new JsonParser().parse(responseBody).getAsJsonObject();
    JsonObject result = root.get("result").getAsJsonObject();
    boolean authorized = result.get("autorizado").getAsInt() == 1;

    CreditCardPaymentResponse response = new CreditCardPaymentResponse();

    response.setAddress(saleRequest.getAddress());
    response.setAmount(amount);
    response.setAuthorized(authorized);

    if (authorized) {
      JsonObject dataVal = result.get("dataVal").getAsJsonObject();
      response.setCardNumber(dataVal.get("numeroTarjeta").getAsString());
      response.setCvt(dataVal.get("cvt").getAsString());
      response.setAuthorization(result.get("autorizacion").getAsString());
      response.setId(result.get("transaccion").getAsString());
      response.setCardType(result.get("TipoTC").getAsString());
      response.setName(dataVal.get("nombre").getAsString());
      response.setLastNames(dataVal.get("apellidos").getAsString());
    }

    return response;
  }

  private String fixMonth(Integer expirationMonth) {
    if (expirationMonth < 10) {
      return "0" + expirationMonth;
    } else {
      return expirationMonth.toString();
    }
  }
}
