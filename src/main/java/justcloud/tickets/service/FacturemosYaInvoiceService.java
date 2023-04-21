package justcloud.tickets.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Optional;
import javax.xml.rpc.ServiceException;
import justcloud.facturemosya.RecibirComprobanteParaTimbradoLocator;
import justcloud.facturemosya.RecibirComprobanteParaTimbradoPortType;
import justcloud.facturemosya.RespuestaTXT;
import justcloud.tickets.domain.sales.InternetSale;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FacturemosYaInvoiceService {

  @Value("${invoice.username}")
  private String username;

  @Value("${invoice.password}")
  private String password;

  @Value("${invoice.address}")
  private String address;

  @Value("${invoice.settlement}")
  private String settlement;

  @Value("${invoice.externalNumber}")
  private String externalNumber;

  @Value("${invoice.municipality}")
  private String municipality;

  @Value("${invoice.state}")
  private String state;

  @Value("${invoice.country}")
  private String country;

  @Value("${invoice.postalCode}")
  private String postalCode;

  public RespuestaTXT createInvoice(InternetSale internetSale)
      throws ServiceException, IOException {
    long folio = System.currentTimeMillis();

    return createInvoice(folio, internetSale);
  }

  private RespuestaTXT createInvoice(long folio, InternetSale internetSale)
      throws ServiceException, IOException {

    BigDecimal total = internetSale.getTotalAmount();
    BigDecimal subtotal = total.divide(new BigDecimal(1.16), MathContext.DECIMAL128);
    BigDecimal tax = total.subtract(subtotal, MathContext.DECIMAL128);

    StringBuilder builder = new StringBuilder();

    DecimalFormat format = new DecimalFormat("##.##");

    DateTimeFormatter formatter =
        new DateTimeFormatterBuilder()
            .appendYear(4, 4)
            .appendPattern("'-'")
            .appendMonthOfYear(2)
            .appendPattern("'-'")
            .appendDayOfMonth(2)
            .appendPattern("'T'")
            .appendHourOfDay(2)
            .appendPattern("':'")
            .appendMinuteOfHour(2)
            .appendPattern("':'")
            .appendSecondOfMinute(2)
            .toFormatter()
            .withZone(DateTimeZone.forID("America/Mexico_City"));

    DateTime dateTime = new DateTime().minusMinutes(5);

    String rfc =
        Optional.ofNullable(internetSale.getBillingData().getRfc())
            .map(String::toUpperCase)
            .orElse("");

    builder.append("COM|||version|3.3||serie|");
    builder.append("WEB");
    builder.append("||folio|");
    builder.append(folio);
    builder.append("||fecha|");
    builder.append(formatter.print(dateTime));
    builder.append(
        "||formaDePago|PAGO EN UNA SOLA EXHIBICION||TipoCambio|1.0000||condicionesDePago|EFECTOS FISCALES AL PAGO||subTotal|");
    builder.append(format.format(subtotal));
    builder.append("||Moneda|MXN||total|");
    builder.append(format.format(total));
    builder.append(
        "||tipoDeComprobante|I||metodoDePago|PUE||LugarExpedicion|Mexico Distrito Federal\n");
    builder.append("EXE|||calle|");
    builder.append(address);
    builder.append("||noExterior|");
    builder.append(externalNumber);
    builder.append("||colonia|");
    builder.append(settlement);
    builder.append("||localidad|");
    builder.append(internetSale.getBillingAddress().getSettlement());
    builder.append("||municipio|");
    builder.append(municipality);
    builder.append("||estado|");
    builder.append(state);
    builder.append("||pais|MEX||codigoPostal|");
    builder.append(postalCode);
    builder.append("\n");
    builder.append("REF|||Regimen|624\n");
    builder.append("REC|||rfc|");
    builder.append(rfc);
    builder.append("||nombre|");
    builder.append(internetSale.getBillingData().getName());
    builder.append("||UsoCFDI|G03");
    builder.append("\n");
    builder.append(
        "DOR|||calle|"
            + internetSale.getBillingAddress().getAddress()
            + "||colonia|"
            + internetSale.getBillingAddress().getSettlement()
            + "||municipio|"
            + internetSale.getBillingAddress().getMunicipality()
            + "||estado|"
            + internetSale.getBillingAddress().getState()
            + "||pais|MEX"
            + "||codigoPostal|"
            + internetSale.getBillingAddress().getPostalCode()
            + "\n");
    builder.append(
        "CON|||ClaveProdServ|78111802||cantidad|1.00||ClaveUnidad|E48||unidad|Servicio||descripcion|TRANSPORTACION TERRESTRE||valorUnitario|");
    builder.append(format.format(subtotal));
    builder.append("||importe|");
    builder.append(format.format(subtotal));
    builder.append("\n");
    builder.append("CONIT|||Base|");
    builder.append(format.format(subtotal));
    builder.append("||Impuesto|002||TipoFactor|Tasa||TasaOCuota|0.160000||Importe|");
    builder.append(format.format(tax));
    builder.append("\n");
    builder.append("TRA|||impuesto|002||TipoFactor|Tasa||TasaOCuota|0.160000||importe|");
    builder.append(format.format(tax));
    builder.append("\n");
    builder.append(
        "ADD|||addenda|2||codigoimpto|codimp||codigoproveedor|codprov||codigodestino|coddes||referenciaproveedor|refprov||remision|rem||ordencompra|ordcomp||nombresoli|nomsol\n");
    builder.append(
        "ADI|||nombreSucursal|SERVICIOS Y AUTOBUSES DEL GOLFO, S.A. DE C.V||telefonoSucursal|||faxSucursal|||emailSucursal|||webSucursal|\n");

    log.info("{} {}", username, password);
    log.info("DATA\n: {}", builder.toString());
    RecibirComprobanteParaTimbradoLocator locator = new RecibirComprobanteParaTimbradoLocator();
    RecibirComprobanteParaTimbradoPortType port = locator.getRecibirComprobanteParaTimbradoPort();
    RespuestaTXT response = port.recibirTXT(username, password, builder.toString(), "0");

    /*
    String body = buildBody(username, password, builder.toString(), "0");

    OkHttpClient client = new OkHttpClient.Builder()
      .build();

    RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), body);


    Request request = new Request.Builder()
      .addHeader("SOAPAction", "\"https://www.facturemosya.com/webservice/sRecibirXML.php/RecibirTXT")
      .post(requestBody)
      .url("https://www.facturemosya.com/webservice/sRecibirXML.php")
      .build();

    Response response = client.newCall(request).execute();

    log.info("Status code {}", response.code());
    log.info("Headers {}", response.headers());


    return response.body().bytes();
    */
    log.info("DATA {} {}", response.getCodigo(), response.getDescripcion());

    return response;
  }

  public String buildBody(String username, String password, String document, String consecutive) {
    StringBuilder builder = new StringBuilder();
    builder
        .append(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.\n"
                + "org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><RecibirTXT soapenv:encodingStyle=\"http://schemas.x\n"
                + "mlsoap.org/soap/encoding/\"><usuario xsi:type=\"xsd:string\">")
        .append(username)
        .append("</usuario><contra xsi:type=\"xsd:string\">")
        .append(password)
        .append("</contra><documento xsi:type=\"xsd:string\">")
        .append(document)
        .append("</documento><consecutivo xsi:type=\"xsd:string\">")
        .append(consecutive)
        .append("</consecutivo></RecibirTXT></soapenv:Body></soapenv:Envelope>");
    return builder.toString();
  }
}
