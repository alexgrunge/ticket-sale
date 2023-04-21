package justcloud.tickets.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import justcloud.tickets.dto.SalesBoothData;
import justcloud.tickets.dto.SalesTerminalData;
import justcloud.tickets.dto.TripData;
import justcloud.tickets.dto.UserData;
import justcloud.tickets.service.sales.PaymentBoothService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

  @Autowired private PaymentBoothService paymentBoothService;

  @RequestMapping("/office/{officeId}")
  List<SalesTerminalData> findPaymentTerminalsByOffice(@PathVariable("officeId") String officeId) {
    return paymentBoothService.listTerminalsByOffice(officeId);
  }

  @RequestMapping("/office/people/{officeId}")
  public List<UserData> findPeopleByOffice(
      @PathVariable("officeId") String officeId,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) {
    return paymentBoothService.findPeople(officeId, startDate, endDate);
  }

  @RequestMapping("/payments/{officeId}")
  public List<TripData> listPayedTrips(
      @PathVariable("officeId") String officeId,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate,
      @RequestParam(value = "operator", required = false) String operator,
      @RequestParam(value = "route", required = false) String route) {
    return paymentBoothService.findTrips(officeId, startDate, endDate, operator, route);
  }

  @RequestMapping("/unjoined/{officeId}")
  public List<TripData> listUnjoinedTrips(
      @PathVariable("officeId") String officeId,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) {
    return paymentBoothService.findUnjoinedTrips(startDate, endDate);
  }

  @RequestMapping("/payments")
  List<SalesBoothData> findSalesTerminals(@RequestParam("terminalId") String terminalId) {
    return paymentBoothService.listBooths(terminalId);
  }

  @RequestMapping("/downloadStartShift/{terminalId}.pdf")
  ResponseEntity<InputStreamResource> startDownloadShift(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = paymentBoothService.getStartShiftInputStream(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "AperturaLiquidación.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/startShift")
  SalesBoothData startShift(
      @RequestParam("terminalId") String terminalId,
      @RequestParam("employeeId") String employeeId) {
    return paymentBoothService.startShift(terminalId, employeeId);
  }

  @RequestMapping("/downloadCloseShift/{terminalId}.pdf")
  ResponseEntity<InputStreamResource> recordClose(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = paymentBoothService.downloadCloseShift(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "CorteLiquidacion.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/closeShift")
  SalesBoothData closeShift(
      @RequestParam("terminalId") String terminalId, @RequestParam("amount") double amount) {
    return paymentBoothService.closeShift(terminalId, amount);
  }

  @RequestMapping("/downloadRecordSnapshot/{terminalId}.pdf")
  ResponseEntity<InputStreamResource> recordSnapshot(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = paymentBoothService.getRecordSnapshot(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "PrecorteLiquidacion.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/recordSnapshot")
  SalesBoothData recordSnapshot(
      @RequestParam("terminalId") String terminalId, @RequestParam("amount") double amount) {
    return paymentBoothService.recordSnapshot(terminalId, amount);
  }

  @RequestMapping("/downloadDetail/{personId}.pdf")
  public ResponseEntity<InputStreamResource> downloadDetail(
      @PathVariable("personId") String personId,
      @RequestParam("timeZone") String timeZone,
      @RequestParam("startDate") String from,
      @RequestParam("endDate") String to)
      throws Exception {
    Date fromDate = new DateTime(from).toDate();
    Date toDate = new DateTime(to).toDate();
    InputStream ticketStream =
        paymentBoothService.getPersonDetail(personId, timeZone, fromDate, toDate);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "DetalleLiquidacion.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/status")
  SalesBoothData getStatus(@RequestParam("terminalId") String terminalId) {
    return paymentBoothService.getStatus(terminalId);
  }
}
