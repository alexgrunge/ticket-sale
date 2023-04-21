package justcloud.tickets.controller;

import java.util.*;
import justcloud.tickets.dto.*;
import justcloud.tickets.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

  @Autowired private SearchService searchService;

  @RequestMapping("/beginnings")
  public List<StopOffData> listBeginnings() {
    return searchService.listBeginnings();
  }

  @RequestMapping("/destinations")
  public List<StopOffData> listDestinations() {
    return searchService.listDestinations();
  }

  @RequestMapping("/beginning")
  public List<Map<String, String>> findBeginningSuggestions(@RequestParam("s") String text) {
    return searchService.findBeginningSuggestions(text);
  }

  @RequestMapping("/destination")
  public List<Map<String, String>> findDestinationSuggestions(
      @RequestParam("s") String text, @RequestParam("o") String origin) {
    return searchService.findDestinationSuggestions(origin, text);
  }

  @RequestMapping("/trip/{tripId}/counts")
  public TripSeatData findCounts(@PathVariable("tripId") String tripId) {
    return searchService.findTripData(tripId);
  }

  @RequestMapping("/trip")
  public SalesQuote findTrip(@RequestBody TripSearch tripSearch) {
    return searchService.findTrip(tripSearch);
  }

  @RequestMapping("/trip/{tripId}/{seatName}")
  public List<SeatData> findSeatData(
      @PathVariable("tripId") String tripId, @PathVariable("seatName") String seatName) {
    return searchService.findSeatData(tripId, seatName);
  }

  @RequestMapping("/localTrip")
  public SalesQuote findLocalTrip(@RequestBody TripSearch tripSearch) {
    return searchService.findLocalTrip(tripSearch);
  }

  @RequestMapping("/tripData")
  public TripData getTripData(
      @RequestParam("routeId") String routeId,
      @RequestParam("originId") String originId,
      @RequestParam("destinationId") String destinationId,
      @RequestParam("serviceLevelId") String serviceLevelId,
      @RequestParam("reverse") Boolean reverse,
      @RequestParam("tripDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date tripDate) {
    return searchService.getTripData(
        routeId, serviceLevelId, originId, destinationId, tripDate, reverse);
  }
}
