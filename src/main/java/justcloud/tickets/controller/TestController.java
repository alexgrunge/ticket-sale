package justcloud.tickets.controller;

import com.google.api.client.util.Base64;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import justcloud.tickets.domain.sales.BillingAddress;
import justcloud.tickets.domain.sales.BillingData;
import justcloud.tickets.domain.sales.InternetSale;
import justcloud.tickets.dto.TestData;
import justcloud.tickets.service.FacturemosYaInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @Autowired private FacturemosYaInvoiceService invoiceService;

  @RequestMapping("/data")
  public TestData dataService() throws Exception {
    InternetSale internetSale = new InternetSale();
    internetSale.setTotalAmount(BigDecimal.TEN);
    BillingAddress billingAddress = new BillingAddress();
    billingAddress.setAddress("Henriquez Ureña 444 K1 701");
    billingAddress.setCountry("México");
    billingAddress.setPostalCode("04330");
    billingAddress.setState("Distrito Federal");
    billingAddress.setMunicipality("Coyoacán");
    billingAddress.setBlock("Los reyes coyoacan");
    billingAddress.setSettlement("Los reyes");

    internetSale.setBillingAddress(billingAddress);

    BillingData billingData = new BillingData();
    billingData.setName("Eduardo Diaz Real");
    billingData.setRfc("DIRE880105GA4");

    internetSale.setBillingData(billingData);

    byte[] bytes =
        Base64.decodeBase64(invoiceService.createInvoice(internetSale).getDocumentopdf());

    Files.write(FileSystems.getDefault().getPath("/Users/iamedu/Desktop/prueba"), bytes);

    return TestData.builder().test("Hola").build();
  }
}
