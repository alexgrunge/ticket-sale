package justcloud.tickets.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import justcloud.tickets.dto.*;
import justcloud.tickets.service.TripService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trip")
public class TripController {

  @Autowired private TripService tripService;

  @RequestMapping("/ticketsDatabase")
  public List<TicketData> listTickets(
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate,
      @RequestParam("lastUpdated") String lastUpdated,
      @RequestParam("timeZone") String timeZone) {
    return tripService.findTickets(startDate, endDate, lastUpdated, timeZone);
  }

  @RequestMapping("/stopsDatabase")
  public List<TripStopData> listStops(
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate,
      @RequestParam("lastUpdated") String lastUpdated,
      @RequestParam("timeZone") String timeZone) {
    return tripService.findStops(startDate, endDate, lastUpdated, timeZone);
  }

  @RequestMapping("/joinPayments")
  public void joinPayments(@RequestBody Map<String, Object> data) {

    tripService.joinPayments(data);
  }

  @RequestMapping("/beginnings")
  public List<StartTripData> findBeginningTrips(@RequestParam("stopOff") String stopOffName) {
    return tripService.findStartingTrips(stopOffName, new Date());
  }

  @RequestMapping("/endings")
  public List<StartTripData> findEndingTrips(@RequestParam("stopOff") String stopOffName) {
    return tripService.findEndingTrips(stopOffName, new Date());
  }

  @RequestMapping("/findTrips")
  public List<FullTripData> findAllTrips(
      @RequestParam("stopOff") String stopOffName,
      @RequestParam("driverName") String driverName,
      @RequestParam("date") String date) {
    DateTime dateTime = new DateTime(date);
    return tripService.findTrips(stopOffName, driverName, dateTime.toDate());
  }

  @RequestMapping("/{id}")
  public FullTripData findTripData(@PathVariable("id") String id) {
    return tripService.findTripData(id);
  }

  @RequestMapping("/groups/{id}")
  public FullGroupData findGroupData(@PathVariable("id") String id) {
    return tripService.findGroupData(id);
  }

  @RequestMapping("/stopOffs")
  public List<StopOffData> listStops() {
    return tripService.listStops();
  }

  @RequestMapping(value = "/useTicket/{id}", method = RequestMethod.POST)
  public TicketData markUsed(@PathVariable("id") String id) {
    return tripService.markTicketUsed(id);
  }

  @RequestMapping(value = "/missingStops/{id}", method = RequestMethod.POST)
  public TripStopData markVisited(
      @PathVariable("id") String id,
      @RequestParam(value = "currentStop", required = false) String currentStop) {
    return tripService.markVisitedStop(id, currentStop);
  }

  @RequestMapping("/missingStops/{id}")
  public List<TripStopData> listMissingStops(@PathVariable("id") String id) {
    return tripService.listStops(id);
  }

  @RequestMapping("/recordUsed/{id}")
  public TicketData recordUsed(@PathVariable("id") String id) {
    return tripService.recordUsed(id);
  }

  @RequestMapping("/join")
  public Map<String, Object> join(@RequestBody Map<String, Object> data) {
    return tripService.joinTrips(data);
  }

  @RequestMapping("/generateGuide")
  public Map<String, Object> generateGuide(@RequestBody Map<String, Object> data) {
    return tripService.generateGuide(data);
  }

  @RequestMapping("/savePayment")
  public Map<String, Object> savePayment(@RequestBody Map<String, Object> data) {
    return tripService.savePayment(data, true);
  }

  @RequestMapping("/savePayments")
  public Map<String, Object> savePayments(@RequestBody Map<String, Object> data) {
    return tripService.savePayment(data, true);
  }

  @RequestMapping("/savePaymentSnapshot")
  public Map<String, Object> savePaymentSnapshot(@RequestBody Map<String, Object> data) {
    return tripService.savePayment(data, false);
  }

  @RequestMapping("/paymentReport/{id}.pdf")
  public ResponseEntity<InputStreamResource> downloadPaymentReport(
      @PathVariable("id") String tripId, @RequestParam("timeZone") String timeZone)
      throws Exception {

    InputStream ticketStream = tripService.generatePaymentReport(tripId, timeZone);

    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.parseMediaType("application/pdf"));

    headers.setContentDispositionFormData("attachment", "liquidacion.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadBoardingList/{id}.pdf")
  public ResponseEntity<InputStreamResource> downloadBoardingList(
      @PathVariable("id") String tripId, @RequestParam("timeZone") String timeZone)
      throws Exception {

    InputStream ticketStream = tripService.downloadBoardingList(tripId, timeZone);

    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.parseMediaType("application/pdf"));

    headers.setContentDispositionFormData("attachment", "listaDeAbordar.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/downloadGuide/{id}.pdf")
  public ResponseEntity<InputStreamResource> downloadGuide(
      @PathVariable("id") String tripId,
      @RequestParam("amount") String amount,
      @RequestParam("userId") String userId,
      @RequestParam("timeZone") String timeZone)
      throws Exception {

    InputStream ticketStream =
        tripService.generateLeavingPackage(tripId, userId, new BigDecimal(amount), timeZone);

    InputStreamResource res = new InputStreamResource(ticketStream);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.parseMediaType("application/pdf"));

    headers.setContentDispositionFormData("attachment", "guia.pdf");

    return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
  }

  @RequestMapping("/trips/block/trip/data")
  public Map<String, Object> tripBlock(@RequestBody Map<String, Object> data) {
    return tripService.blockTrips(data);
  }

  @RequestMapping("/trips/all/{date}")
  public List<TripBusData> tripsByDay(
      @PathVariable("date") String date, @RequestParam("timezone") String tz) {
    return tripService.findTripsByDay(date, tz);
  }

  @RequestMapping("/trips/listTrips")
  public List<TripBusData> tripsBetween(
      @RequestParam("startingDate") String startingDate,
      @RequestParam("endingDate") String endingDate) {
    return tripService.listTrips(
        new DateTime(startingDate).toDate(), new DateTime(endingDate).toDate());
  }

  @RequestMapping("/trips/tracking/{date}")
  public List<TripBusData> trackingTripsByDay(
      @PathVariable("date") String date, @RequestParam("timezone") String tz) {
    return tripService.findTrackingTripsByDay(date, tz);
  }

  @RequestMapping("/trips/logistics/{date}")
  public List<TripLogisticData> tripsLogisticsByDay(
      @PathVariable("date") String date, @RequestParam("timezone") String tz) {
    return tripService.findLogisticTripsByDay(date, tz);
  }

  @RequestMapping("/trips/logistics")
  public List<TripLogisticData> tripsLogistics(
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate,
      @RequestParam("routeName") String routeName,
      @RequestParam("timezone") String tz) {
    return tripService.findLogisticTrips(startDate, endDate, routeName, tz);
  }

  @RequestMapping("/trips/logistics/detail/{id}")
  public List<TripTicketDetail> tripsLogisticsDetail(@PathVariable("id") String id) {
    return tripService.findTripDetail(id);
  }
}
