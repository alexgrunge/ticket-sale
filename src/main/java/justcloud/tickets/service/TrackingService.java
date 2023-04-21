package justcloud.tickets.service;

import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import justcloud.tickets.domain.repository.BusLocationRepository;
import justcloud.tickets.domain.repository.BusRepository;
import justcloud.tickets.domain.repository.TripRepository;
import justcloud.tickets.domain.tickets.Bus;
import justcloud.tickets.domain.tickets.BusLocation;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.dto.BusLocationData;
import justcloud.tickets.dto.LocationData;
import justcloud.tickets.dto.TripData;
import justcloud.tickets.service.tracking.UpdateInfo;
import justcloud.tracking.HistoryDataLastLocationByUserAttributesResponseHistoryDataLastLocationByUserAttributesResult;
import justcloud.tracking.WsHistoryGetByPlateLocator;
import ma.glasnost.orika.MapperFacade;
import org.apache.axis.message.MessageElement;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

  private WsHistoryGetByPlateLocator locator;
  private Logger log = LoggerFactory.getLogger(TrackingService.class);

  @Value("${tracking.username}")
  private String username;

  @Value("${tracking.password}")
  private String password;

  @Autowired private BusRepository busRepository;

  @Autowired private MapperFacade mapper;

  @Autowired private BusLocationRepository busLocationRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private UpdateInfo updateInfo;

  public TrackingService() {
    locator = new WsHistoryGetByPlateLocator();
  }

  public List<BusLocationData> getBusLocations() {
    return StreamSupport.stream(busLocationRepository.findAll().spliterator(), false)
        .map(
            (it) -> {
              BusLocationData data = mapper.map(it, BusLocationData.class);
              Trip trip = tripRepository.findLastByBusId(data.getBus().getId(), new Date());
              TripData tripData = mapper.map(trip, TripData.class);
              data.setTrip(tripData);
              if (trip != null) {
                data.setOriginName(trip.getRun().getBeginning().getName());
                data.setDestinationName(trip.getRun().getDestination().getName());
                data.setRouteName(trip.getRun().getName());
                data.setServiceLevelTypeName(trip.getServiceLevelType().getName());
              }
              return data;
            })
        .filter(
            busLocationData -> {
              return busLocationData.getTrip() != null
                  && busLocationData.getLatitude().compareTo(BigDecimal.ZERO) != 0
                  && busLocationData.getLongitude().compareTo(BigDecimal.ZERO) != 0
                  && !ImmutableSet.of("Reporte por tiempo Movil Apagado")
                      .contains(busLocationData.getEvent());
            })
        .map(updateInfo::checkRoute)
        .collect(Collectors.toList());
  }

  public List<LocationData> getFilteredLocations() throws Exception {
    return getLocations().stream()
        .filter(location -> busRepository.findByGps(location.getPlate().trim()) != null)
        .collect(Collectors.toList());
  }

  public List<LocationData> getLocations() throws Exception {
    HistoryDataLastLocationByUserAttributesResponseHistoryDataLastLocationByUserAttributesResult
        result =
            locator
                .getwsHistoryGetByPlateSoap()
                .historyDataLastLocationByUserAttributes(username, password);

    MessageElement root = Arrays.asList(result.get_any()).get(0);
    MessageElement response = (MessageElement) root.getChildren().get(0);

    MessageElement status = findNode(response, "Status");
    MessageElement statusCode = findNode(status, "code");
    Boolean valid = statusCode.getValue().equals("100");

    if (valid) {
      List<MessageElement> plates = findNodes(response, "Plate");

      return plates.stream()
          .filter(plate -> StringUtils.isNotBlank(plate.getAttribute("id")))
          .map(this::convertElement)
          .collect(Collectors.toList());
    } else {
      log.debug("Cannot read locations");
      return Collections.emptyList();
    }
  }

  private LocationData convertElement(MessageElement element) {
    LocationData location = new LocationData();
    MessageElement hst = findNode(element, "hst");

    location.setPlate(element.getAttribute("id"));
    location.setLocation(hst.getAttribute("Location"));
    location.setHeading(hst.getAttribute("Heading"));
    location.setEvent(hst.getAttribute("Event"));
    location.setZones(hst.getAttribute("Zones"));
    location.setLatitude(new BigDecimal(hst.getAttribute("Latitude")));
    location.setLongitude(new BigDecimal(hst.getAttribute("Longitude")));
    location.setServerTime(parseDate(hst.getAttribute("DateTimeServer")));
    location.setGpsTime(parseDate(hst.getAttribute("DateTimeGPS")));

    return location;
  }

  private MessageElement findNode(MessageElement element, String nodeName) {
    return findNodes(element, nodeName).get(0);
  }

  @SuppressWarnings("unchecked")
  private List<MessageElement> findNodes(MessageElement response, String nodeName) {
    return ((Stream<MessageElement>) response.getChildren().stream())
        .filter(
            child -> {
              return child.getNodeName().equals(nodeName);
            })
        .collect(Collectors.toList());
  }

  private Date parseDate(String dateString) {
    return DateTimeFormat.forPattern("YYYY/MM/dd HH:mm:ss")
        .parseDateTime(dateString)
        .withZoneRetainFields(DateTimeZone.forID("America/Mexico_City"))
        .toDate();
  }

  @Scheduled(fixedRate = 120000)
  public void lookupLocations() {
    try {
      List<LocationData> locations = getFilteredLocations();
      locations.forEach(
          locationData -> {
            Bus bus = busRepository.findByGps(locationData.getPlate().trim());
            BusLocation busLocation =
                Optional.ofNullable(busLocationRepository.findByBus(bus)).orElse(new BusLocation());

            busLocation.setBus(bus);
            busLocation.setEvent(locationData.getEvent());
            busLocation.setGpsTime(locationData.getGpsTime());
            busLocation.setServerTime(locationData.getServerTime());
            busLocation.setHeading(locationData.getHeading());
            busLocation.setLatitude(locationData.getLatitude());
            busLocation.setLongitude(locationData.getLongitude());
            busLocation.setLocation(locationData.getLocation());
            busLocation.setZones(locationData.getZones());

            busLocationRepository.save(busLocation);
          });
    } catch (Exception ex) {
      log.error("Cannot get locations", ex);
    }
  }
}
