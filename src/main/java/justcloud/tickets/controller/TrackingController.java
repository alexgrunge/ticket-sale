package justcloud.tickets.controller;

import java.util.List;
import justcloud.tickets.dto.BusLocationData;
import justcloud.tickets.dto.LocationData;
import justcloud.tickets.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracking")
public class TrackingController {

  @Autowired private TrackingService trackingService;

  @RequestMapping("/locations")
  public List<BusLocationData> locations() throws Exception {
    return trackingService.getBusLocations();
  }

  @RequestMapping("/test")
  public List<LocationData> filteredLocations() throws Exception {
    return trackingService.getLocations();
  }
}
