package justcloud.tickets.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import justcloud.tickets.domain.repository.RouteRepository;
import justcloud.tickets.domain.repository.StopOffRepository;
import justcloud.tickets.domain.tickets.Route;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.dto.BusLocationData;
import justcloud.tickets.dto.RouteData;
import justcloud.tickets.dto.StopOffData;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

  @Autowired private RouteRepository routeRepository;

  @Autowired private StopOffRepository stopOffRepository;

  @Autowired private TrackingService trackingService;

  @Autowired private MapperFacade mapper;

  public List<RouteData> listRoutes() {
    return StreamSupport.stream(routeRepository.findAll().spliterator(), false)
        .map(route -> mapper.map(route, RouteData.class))
        .collect(Collectors.toList());
  }

  public List<BusLocationData> listLocations(String stopId) {
    StopOff stopOff = stopOffRepository.findOne(stopId);
    String stopName = stopOff.getName();

    Set<String> routes =
        stopOffRepository.findAllByName(stopName).stream()
            .map(StopOff::getRoute)
            .map(Route::getName)
            .collect(Collectors.toSet());

    return trackingService.getBusLocations().stream()
        .filter(l -> routes.contains(l.getRouteName()))
        .collect(Collectors.toList());
  }

  public List<StopOffData> listAllStops() {
    Map<String, List<StopOffData>> tmp =
        StreamSupport.stream(stopOffRepository.findAll(new Sort("name")).spliterator(), false)
            .map(stop -> mapper.map(stop, StopOffData.class))
            .collect(Collectors.groupingBy(StopOffData::getName, Collectors.toList()));
    return tmp.values().stream()
        .filter(l -> l.size() > 0)
        .map(l -> l.get(0))
        .sorted((a, b) -> a.getName().compareTo(b.getName()))
        .collect(Collectors.toList());
  }
}
