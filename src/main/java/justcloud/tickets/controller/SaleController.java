package justcloud.tickets.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import justcloud.tickets.dto.*;
import justcloud.tickets.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale")
public class SaleController {

  @Autowired private SaleService saleService;

  @RequestMapping("/accountData/{accountId}")
  public AccountData findAccount(@PathVariable("accountId") String accountId) {
    return saleService.findAccount(accountId);
  }

  @RequestMapping("/generateBill/{saleId}")
  public Map<String, Object> generateBill(
      @PathVariable("saleId") String saleId, @RequestBody BillRequest request) throws Exception {
    return saleService.generateBill(saleId, request);
  }

  @RequestMapping("/lookupBill")
  public BillRequest findUser(@RequestParam("rfc") String rfc) {
    return saleService.lookupBillingData(rfc);
  }

  @RequestMapping("/updateTicket/{ticketId}")
  public Map<String, Object> cancelTicket(
      @PathVariable("ticketId") String ticketId, @RequestParam("comments") String comments) {
    return saleService.updateComments(ticketId, comments);
  }

  @RequestMapping("/cancelTicket/{ticketId}")
  public Map<String, Object> cancelTicket(
      @PathVariable("ticketId") String ticketId,
      @RequestParam(required = false, name = "cardNumber") String accountId,
      @RequestParam(required = false, name = "phoneNumber") String clientPhone,
      @RequestParam(required = false, name = "returnMoney") String returnMoney) {
    return saleService.cancelTicket(ticketId, accountId, clientPhone, returnMoney.equals("true"));
  }

  @RequestMapping("/cancelPackageTicket/{ticketId}")
  public Map<String, Object> cancelPackageTicket(
      @PathVariable("ticketId") String ticketId,
      @RequestParam(required = false, name = "cardNumber") String accountId,
      @RequestParam(required = false, name = "phoneNumber") String clientPhone,
      @RequestParam(required = false, name = "returnMoney") String returnMoney) {
    return saleService.cancelPackageTicket(
        ticketId, accountId, clientPhone, returnMoney.equals("true"));
  }

  @RequestMapping("/saleData/{shortId}")
  public Map<String, Object> findPayData(@PathVariable("shortId") String shortId) {
    return saleService.findSaleData(shortId);
  }

  @RequestMapping("/packageSaleData/{shortId}")
  public Map<String, Object> findPackagePayData(@PathVariable("shortId") String shortId) {
    return saleService.findPackageSaleData(shortId);
  }

  @RequestMapping("/findByName")
  public List<Map<String, Object>> findSales(@RequestParam("name") String name) {
    return saleService.findSaleDataByName(name);
  }

  @RequestMapping("/findByPackageName")
  public List<Map<String, Object>> findPackageSales(@RequestParam("name") String name) {
    return saleService.findPackageSaleDataByName(name);
  }

  @RequestMapping("/checkUserDetail")
  public LoginResponse checkUserDetail(@RequestBody LoginRequest loginRequest) {
    return saleService.checkUserDetail(loginRequest);
  }

  @RequestMapping("/payWithCreditCard")
  public SaleResponse requestCardPayment(@RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("pagoFacil");
    return saleService.buyTrip(saleRequest);
  }

  @RequestMapping("/couponData/{accountId}")
  public AccountData getAccountData(@PathVariable("accountId") String accountId) {
    return saleService.findAccount(accountId);
  }

  @RequestMapping("/couponData/number/{number}")
  public AccountData getAccountDataByNumber(@PathVariable("number") String number) {
    return saleService.findAccountByNumber(number);
  }

  @RequestMapping("/payCoupon")
  public AccountSaleResponse payCoupon(@RequestBody AccountSaleRequest saleRequest)
      throws Exception {
    return saleService.buyAccount(saleRequest);
  }

  @RequestMapping("/reserve")
  public SaleResponse reserveTickets(@RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("booth");

    return saleService.reserveTrip(saleRequest);
  }

  @RequestMapping("/cancelReservation/{id}")
  public SaleResponse cancelReservation(@PathVariable("id") String id) throws Exception {
    return saleService.cancelReservation(id);
  }

  @RequestMapping("/payReservation/{id}")
  public SaleResponse requestReservationPayment(
      @PathVariable("id") String id, @RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("booth");

    return saleService.payTrip(id, saleRequest);
  }

  @RequestMapping("/payBooth")
  public SaleResponse requestBoothPayment(@RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("booth");

    return saleService.buyTrip(saleRequest);
  }

  @RequestMapping("/payInStore")
  public SaleResponse requestStorePayment(@RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("comproPago");
    return saleService.buyTrip(saleRequest);
  }

  @RequestMapping("/payWithPaypal")
  public SaleResponse requestPaypalPayment(@RequestBody SaleRequest saleRequest) throws Exception {
    saleRequest.setPaymentType("paypal");
    return saleService.buyTrip(saleRequest);
  }

  @RequestMapping("/payWithMastercard")
  public SaleResponse requestMastercardPayment(@RequestBody SaleRequest saleRequest)
      throws Exception {
    saleRequest.setPaymentType("mastercardGateway");
    return saleService.buyTrip(saleRequest);
  }

  @RequestMapping("/confirmPaypal")
  public SaleResponse confirmPaypalPayment(
      @RequestParam("paymentId") String paymentId,
      @RequestParam("token") String token,
      @RequestParam("PayerID") String payerId)
      throws Exception {
    return saleService.confirmPaypal(paymentId, token, payerId);
  }

  @RequestMapping("/confirmStore")
  public SaleResponse confirmStore(@RequestBody Map<String, Object> confirmation) throws Exception {
    return saleService.confirmCompropago(confirmation);
  }

  @RequestMapping("/confirmVisa")
  public SaleResponse confirmVisa(@RequestBody Map<String, Object> confirmation) throws Exception {
    return saleService.confirmVisa(confirmation);
  }

  @RequestMapping("/downloadOfficeCsv/{officeId}")
  public ResponseEntity<InputStreamResource> downloadOfficeCsv(
      @PathVariable("officeId") String officeId) throws Exception {

    String csv = saleService.generateReportCsv(officeId);
    ByteArrayInputStream ticketStream = new ByteArrayInputStream(csv.getBytes("UTF-8"));
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.TEXT_PLAIN);
    headers.setContentDispositionFormData("attachment", "reporte.csv");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadBaggageTicket/{ticketId}")
  public ResponseEntity<InputStreamResource> downloadBaggageTicket(
      @PathVariable("ticketId") String ticketId,
      @RequestParam(value = "timeZone", required = false) String timeZone)
      throws Exception {

    InputStream ticketStream = saleService.getBaggageTicket(ticketId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.setContentDispositionFormData("attachment", ticketId + ".pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadTickets/{saleId}")
  public ResponseEntity<InputStreamResource> downloadTickets(
      @PathVariable("saleId") String saleId,
      @RequestParam(value = "timeZone", required = false) String timeZone)
      throws Exception {

    InputStream ticketStream = saleService.generateAllTickets(saleId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.setContentDispositionFormData("attachment", saleId + ".pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadTicket/{ticketId}")
  public ResponseEntity<InputStreamResource> downloadTicket(
      @PathVariable("ticketId") String ticketId,
      @RequestParam(value = "timeZone", required = false) String timeZone)
      throws Exception {

    InputStream ticketStream = saleService.getTicket(ticketId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.setContentDispositionFormData("attachment", ticketId + ".pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadXmlBill/{shortId}")
  public ResponseEntity<InputStreamResource> downloadXmlBill(
      @PathVariable("shortId") String shortId) throws Exception {

    InputStream ticketStream = saleService.getXmlBill(shortId);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "Factura.xml");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadBill/{shortId}")
  public ResponseEntity<InputStreamResource> downloadBill(@PathVariable("shortId") String shortId)
      throws Exception {

    InputStream ticketStream = saleService.getBill(shortId);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "Factura.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }
}
