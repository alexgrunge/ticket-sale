package justcloud.tickets.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import justcloud.tickets.dto.OfficeLocationData;
import justcloud.tickets.dto.SalesBoothData;
import justcloud.tickets.dto.SalesTerminalData;
import justcloud.tickets.dto.UserData;
import justcloud.tickets.service.sales.BoothService;
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
@RequestMapping("/booth")
public class BoothController {

  @Autowired private BoothService boothService;

  @RequestMapping("/office/{officeId}")
  public List<SalesTerminalData> findTerminalsByOffice(@PathVariable("officeId") String officeId) {
    return boothService.listBoothsByOffice(officeId);
  }

  @RequestMapping("/office/people/{officeId}")
  public List<UserData> findPeopleByOffice(
      @PathVariable("officeId") String officeId,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) {
    return boothService.findPeople(officeId, startDate, endDate);
  }

  @RequestMapping("/offices")
  public List<OfficeLocationData> listOffices() {
    return boothService.listOffices();
  }

  @RequestMapping("/status")
  public SalesTerminalData getStatus() {
    return boothService.getStatus();
  }

  @RequestMapping("/sales")
  public List<SalesBoothData> findSalesTerminals(@RequestParam("terminalId") String terminalId) {
    return boothService.listBooths(terminalId);
  }

  @RequestMapping("/payments")
  public List<SalesBoothData> findPaymentBooths(@RequestParam("terminalId") String terminalId) {
    return boothService.listPaymentBooths(terminalId);
  }

  @RequestMapping("/startShift")
  public SalesBoothData startShift(
      @RequestParam("terminalId") String terminalId,
      @RequestParam("employeeId") String employeeId,
      @RequestParam("amount") double amount) {
    return boothService.startShift(terminalId, employeeId, amount);
  }

  @RequestMapping("/downloadStartShift/{terminalId}.pdf")
  public ResponseEntity<InputStreamResource> startShift(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = boothService.getStartShiftInputStream(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "AperturaCaja.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadCloseShift/{terminalId}.pdf")
  public ResponseEntity<InputStreamResource> closeShift(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = boothService.closeShift(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "CorteCaja.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/closeShift")
  public SalesBoothData closeShift(@RequestParam("terminalId") String terminalId) {
    return boothService.closeShift(terminalId);
  }

  @RequestMapping("/recordSnapshot")
  public SalesBoothData recordSnapshot(
      @RequestParam("terminalId") String terminalId, @RequestParam("amount") double amount) {
    return boothService.recordSnapshot(terminalId, amount);
  }

  @RequestMapping("/downloadRecordSnapshot/{terminalId}.pdf")
  public ResponseEntity<InputStreamResource> recordSnapshot(
      @PathVariable("terminalId") String terminalId, @RequestParam("timeZone") String timeZone)
      throws Exception {
    InputStream ticketStream = boothService.latestSnapshotReport(terminalId, timeZone);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "PrecorteCaja.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
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
    InputStream ticketStream = boothService.getPersonDetail(personId, timeZone, fromDate, toDate);
    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", "DetalleVenta.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }
}
