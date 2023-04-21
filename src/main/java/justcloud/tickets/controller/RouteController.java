package justcloud.tickets.controller;

import java.util.List;
import justcloud.tickets.dto.BusLocationData;
import justcloud.tickets.dto.RouteData;
import justcloud.tickets.dto.StopOffData;
import justcloud.tickets.service.RouteService;
import justcloud.tickets.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class RouteController {

  @Autowired private RouteService routeService;

  @Autowired private SearchService searchService;

  @RequestMapping("/list")
  public List<RouteData> listRoutes() {
    return routeService.listRoutes();
  }

  @RequestMapping("/allStops")
  public List<StopOffData> listAllStops() {
    return routeService.listAllStops();
  }

  @RequestMapping("/stopsSchedule/{id}")
  public List<BusLocationData> listAllStops(@PathVariable("id") String id) {
    return routeService.listLocations(id);
  }

  @RequestMapping("/reindex/{id}")
  public void reindex(@PathVariable("id") String id) {
    searchService.indexRoute(id);
  }
}
