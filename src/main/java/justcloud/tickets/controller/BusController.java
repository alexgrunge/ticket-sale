package justcloud.tickets.controller;

import java.util.List;
import java.util.Map;
import justcloud.tickets.dto.BusData;
import justcloud.tickets.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buses")
public class BusController {

  @Autowired private BusService busService;

  @RequestMapping("/general")
  public List<BusData> listGeneralBusData() {
    return busService.listBusGeneralData();
  }

  @RequestMapping(value = "/assignDrivers", method = RequestMethod.POST)
  public void assignDrivers(@RequestBody BusData busData) {
    busService.assignDrivers(busData);
  }

  @RequestMapping(value = "/assign", method = RequestMethod.POST)
  public void assignBus(@RequestBody Map<String, Object> data) {
    busService.assignBus(data);
  }
}
