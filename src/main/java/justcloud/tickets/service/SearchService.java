package justcloud.tickets.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import justcloud.tickets.domain.Parameter;
import justcloud.tickets.domain.repository.*;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.dto.*;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.search.repository.RouteSegmentRepository;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionFuzzyBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

  private Logger log = LoggerFactory.getLogger(SearchService.class);

  @Value("${maximum.total}")
  private Long maxTotal;

  @Value("${maximum.students}")
  private Long maxStudents;

  @Value("${maximum.olderAdults}")
  private Long maxOlderAdults;

  @Value("${maximum.reserved}")
  private Long maxReserved;

  @Autowired private StopOffRepository stopOffRepository;

  @Autowired private ParameterRepository parameterRepository;

  @Autowired private ServiceLevelTypeRepository serviceLevelTypeRepository;

  @Autowired private TripSeatRepository tripSeatRepository;

  @Autowired private RouteRepository routeRepository;

  @Autowired private RouteSegmentRepository routeSegmentRepository;

  @Autowired private SegmentVarRepository segmentVarRepository;

  @Autowired private DefaultBusRepository defaultBusRepository;

  @Autowired private RunRepository runRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private TripService tripService;

  @Autowired private MapperFacade mapper;

  @Autowired private Client client;

  @Scheduled(fixedRate = 600000)
  public void indexAll() {
    log.debug("About to index");
    for (Route route : routeRepository.findAll()) {
      indexRoute(route);
    }
  }

  public List<StopOffData> listBeginnings() {
    return StreamSupport.stream(routeRepository.findAll().spliterator(), false)
        .filter(route -> route.getStops().size() > 0)
        .map(
            route -> {
              StopOffData data = new StopOffData();

              List<StopOff> stops = route.getStops();
              stops.sort((s1, s2) -> s1.getOrderIndex().compareTo(s2.getOrderIndex()));

              StopOff stopOff = stops.get(0);

              data.setName(stopOff.getName());

              return data;
            })
        .distinct()
        .sorted((d1, d2) -> d1.getName().toLowerCase().compareTo(d2.getName().toLowerCase()))
        .collect(Collectors.toList());
  }

  public List<StopOffData> listDestinations() {
    return StreamSupport.stream(routeRepository.findAll().spliterator(), false)
        .filter(route -> route.getStops().size() > 0)
        .map(
            route -> {
              StopOffData data = new StopOffData();

              List<StopOff> stops = route.getStops();
              stops.sort((s1, s2) -> s1.getOrderIndex().compareTo(s2.getOrderIndex()));

              StopOff stopOff = stops.get(route.getStops().size() - 1);

              data.setName(stopOff.getName());

              return data;
            })
        .distinct()
        .sorted((d1, d2) -> d1.getName().toLowerCase().compareTo(d2.getName().toLowerCase()))
        .collect(Collectors.toList());
  }

  public List<Map<String, String>> findDestinationSuggestions(String origin, String text) {
    BoolQueryBuilder queryBuilder =
        QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("startingStop.name.raw", origin));

    if (!StringUtils.isEmpty(text)) {
      queryBuilder =
          queryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("endingStop.name.i18n", text));
    }

    SearchResponse searchResponse =
        client.prepareSearch("ticket-sales").setTypes("routesegment").setQuery(queryBuilder).get();

    return StreamSupport.stream(searchResponse.getHits().spliterator(), false)
        .map(
            (hit) -> {
              @SuppressWarnings("unchecked")
              Map<String, Object> endingStop =
                  (Map<String, Object>) hit.getSource().get("endingStop");
              String endingStopName = (String) endingStop.get("name");
              Map<String, String> result = new HashMap<>();
              result.put("value", endingStopName);
              return result;
            })
        .distinct()
        .collect(Collectors.toList());
  }

  public List<Map<String, String>> findBeginningSuggestions(String text) {
    if (StringUtils.isEmpty(text)) {
      return listBeginnings().stream()
          .map(
              b -> {
                Map<String, String> result = new HashMap<>();
                result.put("value", b.getName());
                return result;
              })
          .collect(Collectors.toList());
    }

    CompletionSuggestionFuzzyBuilder builder =
        new CompletionSuggestionFuzzyBuilder("segment-suggest")
            .text(text)
            .field("startingStop.suggestion")
            .setUnicodeAware(true)
            .setFuzziness(Fuzziness.TWO);

    SuggestResponse suggestResponse =
        client.prepareSuggest("ticket-sales").addSuggestion(builder).get();

    return suggestResponse.getSuggest().getSuggestion("segment-suggest").getEntries().stream()
        .flatMap((suggestion) -> suggestion.getOptions().stream())
        .map(
            (option) -> {
              Map<String, String> result = new HashMap<>();
              result.put("value", option.getText().string());
              return result;
            })
        .distinct()
        .collect(Collectors.toList());
  }

  public void indexRoute(Route route) {
    List<StopOff> stops =
        route.getStops().stream()
            .sorted((s1, s2) -> s1.getOrderIndex().compareTo(s2.getOrderIndex()))
            .collect(Collectors.toList());

    List<StopOff> reverseStops =
        route.getStops().stream()
            .sorted((s1, s2) -> s2.getOrderIndex().compareTo(s1.getOrderIndex()))
            .collect(Collectors.toList());

    if (stops.size() == 0) {
      return;
    }

    // log.debug("Indexing {}", route.getName());

    Map<String, List<SegmentVar>> segments =
        segmentVarRepository.findByStartingStops(stops).stream()
            .collect(Collectors.groupingBy(SegmentVar::getCategory));

    segments
        .values()
        .forEach(
            (list) ->
                Collections.sort(
                    list,
                    (s1, s2) ->
                        s1.getStartingStop()
                            .getOrderIndex()
                            .compareTo(s2.getStartingStop().getOrderIndex())));

    Map<String, RouteSegment.SegmentData> extraData = calculateData(route, stops);
    Map<String, RouteSegment.SegmentData> reverseExtraData = calculateData(route, reverseStops);
    List<SegmentVar> prices = segments.get("price");

    if (prices == null) {
      return;
    }

    Function<SegmentVar, RouteSegment> convertSegment =
        (segment) -> {
          RouteSegment routeSegment = new RouteSegment();

          RouteSegment.StopData startingStop =
              mapper.map(segment.getStartingStop(), RouteSegment.StopData.class);
          RouteSegment.StopData endingStop =
              mapper.map(segment.getEndingStop(), RouteSegment.StopData.class);

          StringBuilder builder =
              new StringBuilder(route.getId())
                  .append("@")
                  .append(startingStop.getId())
                  .append("@")
                  .append(endingStop.getId())
                  .append("@")
                  .append(segment.getServiceLevelType().getId());

          StringBuilder extraIdBuilder =
              new StringBuilder()
                  .append(startingStop.getId())
                  .append("@")
                  .append(endingStop.getId());

          routeSegment.setExtra(extraData.get(extraIdBuilder.toString()));

          if (routeSegment.getExtra() == null) {
            return null;
          }

          // if(route.getName().equals("6010VH")) {
          //   if(startingStop.getName().equals("Villahermosa Central") &&
          // endingStop.getName().equals("Veracruz")) {
          //     log.debug("Found {} {} {} {} prices", startingStop.getName(), endingStop.getName(),
          // builder.toString(), segment.getServiceLevelType().getName());
          //   }
          // }

          routeSegment.setServiceLevelTypeId(segment.getServiceLevelType().getId());
          routeSegment.setRouteId(route.getId());
          routeSegment.setPrice(segment.getNumericVar());
          routeSegment.setId(builder.toString());
          routeSegment.setStartingStop(startingStop);
          routeSegment.setEndingStop(endingStop);
          routeSegment.setReverse(false);

          return routeSegment;
        };

    Function<SegmentVar, RouteSegment> convertReverseSegment =
        (segment) -> {
          RouteSegment routeSegment = new RouteSegment();

          RouteSegment.StopData startingStop =
              mapper.map(segment.getEndingStop(), RouteSegment.StopData.class);
          RouteSegment.StopData endingStop =
              mapper.map(segment.getStartingStop(), RouteSegment.StopData.class);

          StringBuilder builder =
              new StringBuilder(route.getId())
                  .append("@")
                  .append(startingStop.getId())
                  .append("@")
                  .append(endingStop.getId())
                  .append("@")
                  .append(segment.getServiceLevelType().getId());

          StringBuilder extraIdBuilder =
              new StringBuilder()
                  .append(startingStop.getId())
                  .append("@")
                  .append(endingStop.getId());
          routeSegment.setExtra(reverseExtraData.get(extraIdBuilder.toString()));

          if (routeSegment.getExtra() == null) {
            return null;
          }

          routeSegment.setServiceLevelTypeId(segment.getServiceLevelType().getId());
          routeSegment.setRouteId(route.getId());
          routeSegment.setPrice(segment.getNumericVar());
          routeSegment.setId(builder.toString());
          routeSegment.setStartingStop(startingStop);
          routeSegment.setEndingStop(endingStop);
          routeSegment.setReverse(true);

          return routeSegment;
        };

    List<RouteSegment> pricedSegments =
        prices.stream()
            .map(convertSegment)
            .filter((segment) -> segment != null)
            .collect(Collectors.toList());

    if (route.getHasReverse()) {
      List<RouteSegment> reversePricedSegments =
          prices.stream()
              .map(convertReverseSegment)
              .filter((segment) -> segment != null)
              .collect(Collectors.toList());
      pricedSegments.addAll(reversePricedSegments);
    }

    routeSegmentRepository.save(pricedSegments);
  }

  public TripSeatData findTripData(String tripId) {
    Trip trip = tripRepository.findOne(tripId);
    TripSeatData.TripSeatDataBuilder builder = TripSeatData.builder();

    int studentCount =
        tripSeatRepository.countByTripIdAndPassengerType(tripId, PassengerType.STUDENT);
    int olderAdultCount =
        tripSeatRepository.countByTripIdAndPassengerType(tripId, PassengerType.OLDER_ADULT);

    builder
        .reservedOlderAdults(olderAdultCount)
        .reservedStudents(studentCount)
        .maxOlderAdults(maxOlderAdults.intValue())
        .maxStudents(maxStudents.intValue())
        .maxTotal(maxTotal.intValue());

    return builder.build();
  }

  public SalesQuote findLocalTrip(TripSearch tripSearch) {
    List<TripQuote> quotes =
        IntStream.range(0, 5)
            .mapToObj(
                (idx) -> {
                  return new DateTime().plusDays(idx).toDate();
                })
            .flatMap(
                (date) -> {
                  tripSearch.setDepartureDate(date);
                  tripSearch.setAdultCount(1);
                  tripSearch.setOlderAdultCount(0);
                  tripSearch.setChildrenCount(0);
                  tripSearch.setInfantCount(0);

                  return findTrip(tripSearch).getDepartureQuotes().stream();
                })
            .collect(Collectors.toList());

    SalesQuote quote = new SalesQuote();

    quote.setDepartureQuotes(quotes);
    quote.setPassengers(new ArrayList<>());
    quote.setReturnQuotes(new ArrayList<>());
    quote.setMaxOlderAdults(maxOlderAdults);
    quote.setMaxStudents(maxStudents);

    return quote;
  }

  public SalesQuote findTrip(TripSearch tripSearch) {

    DateTimeZone zone =
        Optional.ofNullable(tripSearch.getTimeZone())
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.getDefault());

    if (tripSearch.getRoundTrip() == null) {
      tripSearch.setRoundTrip(false);
    }

    List<RouteSegment> segments =
        Optional.ofNullable(tripSearch.getDestination())
            .map(
                (destination) -> {
                  return routeSegmentRepository
                      .findByStartingStopNameAndEndingStopName(
                          tripSearch.getOrigin(), destination, new PageRequest(0, 100))
                      .getContent();
                })
            .orElseGet(
                () -> {
                  return routeSegmentRepository
                      .findByStartingStopName(tripSearch.getOrigin(), new PageRequest(0, 100))
                      .getContent();
                });
    List<RouteSegment> returnSegments =
        Optional.of(tripSearch.getRoundTrip())
            .filter((roundTrip) -> roundTrip)
            .map(
                (roundTrip) -> {
                  return routeSegmentRepository
                      .findByStartingStopNameAndEndingStopName(
                          tripSearch.getDestination(),
                          tripSearch.getOrigin(),
                          new PageRequest(0, 100))
                      .getContent();
                })
            .orElse(new ArrayList<RouteSegment>());

    List<String> relevantRouteIds =
        segments.stream().map(RouteSegment::getRouteId).collect(Collectors.toList());

    List<String> relevantReturnRouteIds =
        returnSegments.stream().map(RouteSegment::getRouteId).collect(Collectors.toList());

    Map<String, List<RouteSegment>> departureSegmentsByRoute =
        segments.stream()
            .collect(
                Collectors.groupingBy(
                    (segment) -> {
                      return segment.getRouteId();
                    }));

    Map<String, List<RouteSegment>> returnSegmentsByRoute =
        Optional.ofNullable(tripSearch.getRoundTrip())
            .filter((roundTrip) -> roundTrip)
            .map(
                (roundTrip) -> {
                  return returnSegments.stream()
                      .collect(
                          Collectors.groupingBy(
                              (segment) -> {
                                return segment.getRouteId();
                              }));
                })
            .orElse(null);

    List<PassengerData> passengers = new ArrayList<>();

    int passengerNumber = 1;
    for (int i = 0; i < tripSearch.getAdultCount(); i++) {
      PassengerData passenger = new PassengerData();
      passenger.setOriginalName("Pasajero " + passengerNumber);
      passenger.setName("Pasajero " + passengerNumber++);
      passenger.setPassengerType(PassengerType.ADULT);
      passengers.add(passenger);
    }

    for (int i = 0; i < tripSearch.getChildrenCount(); i++) {
      PassengerData passenger = new PassengerData();
      passenger.setOriginalName("Pasajero " + passengerNumber);
      passenger.setName("Pasajero " + passengerNumber++);
      passenger.setPassengerType(PassengerType.CHILD);
      passengers.add(passenger);
    }

    for (int i = 0; i < Optional.ofNullable(tripSearch.getStudentCount()).orElse(0); i++) {
      PassengerData passenger = new PassengerData();
      passenger.setOriginalName("Pasajero " + passengerNumber);
      passenger.setName("Pasajero " + passengerNumber++);
      passenger.setPassengerType(PassengerType.STUDENT);
      passengers.add(passenger);
    }

    for (int i = 0; i < tripSearch.getOlderAdultCount(); i++) {
      PassengerData passenger = new PassengerData();
      passenger.setOriginalName("Pasajero " + passengerNumber);
      passenger.setName("Pasajero " + passengerNumber++);
      passenger.setPassengerType(PassengerType.OLDER_ADULT);
      passengers.add(passenger);
    }

    for (int i = 0; i < tripSearch.getInfantCount(); i++) {
      PassengerData passenger = new PassengerData();
      passenger.setOriginalName("Pasajero " + passengerNumber);
      passenger.setName("Pasajero " + passengerNumber++);
      passenger.setPassengerType(PassengerType.INFANT);
      passengers.add(passenger);
    }

    @SuppressWarnings("unchecked")
    List<Route> routes = IteratorUtils.toList(routeRepository.findAll(relevantRouteIds).iterator());
    @SuppressWarnings("unchecked")
    List<Route> returnRoutes =
        IteratorUtils.toList(routeRepository.findAll(relevantReturnRouteIds).iterator());

    List<RunServiceHolder> departureRuns = new ArrayList<>();

    Date nextDate = new DateTime(tripSearch.getDepartureDate()).plusDays(1).toDate();
    Date prevDate = new DateTime(tripSearch.getDepartureDate()).minusDays(1).toDate();

    departureRuns.addAll(findRunAlternatives(departureSegmentsByRoute, routes, prevDate, "depart"));
    departureRuns.addAll(
        findRunAlternatives(
            departureSegmentsByRoute, routes, tripSearch.getDepartureDate(), "depart"));
    departureRuns.addAll(findRunAlternatives(departureSegmentsByRoute, routes, nextDate, "depart"));

    List<RunServiceHolder> returnRuns =
        Optional.ofNullable(tripSearch.getReturningDate())
            .filter((returnDate) -> tripSearch.getRoundTrip())
            .map(
                (returnDate) -> {
                  List<RunServiceHolder> result = new ArrayList<>();
                  Date nextReturnDate = new DateTime(returnDate).plusDays(1).toDate();
                  Date prevReturnDate = new DateTime(returnDate).minusDays(1).toDate();
                  result.addAll(
                      findRunAlternatives(
                          returnSegmentsByRoute, returnRoutes, prevReturnDate, "return"));
                  result.addAll(
                      findRunAlternatives(
                          returnSegmentsByRoute, returnRoutes, returnDate, "return"));
                  result.addAll(
                      findRunAlternatives(
                          returnSegmentsByRoute, returnRoutes, nextReturnDate, "return"));
                  return result;
                })
            .orElse(new ArrayList<RunServiceHolder>());

    Function<Boolean, Function<RunServiceHolder, TripQuote>> getQuote =
        (returnTrip) -> {
          return (run) -> {
            Map<String, List<RouteSegment>> segmentsByRoute;
            if (returnTrip) {
              segmentsByRoute = returnSegmentsByRoute;
            } else {
              segmentsByRoute = departureSegmentsByRoute;
            }

            List<RouteSegment> debugList = segmentsByRoute.get(run.getRun().getRoute().getId());
            log.debug(
                "Want price for {} {} {}",
                run.getRun().getRoute().getName(),
                run.getServiceTypeTime().getServiceLevelType().getName(),
                run.getServiceTypeTime().getServiceLevelType().getId());
            List<Route> debugRoutes =
                StreamSupport.stream(
                        routeRepository.findAll(segmentsByRoute.keySet()).spliterator(), false)
                    .collect(Collectors.toList());
            log.debug(
                "Have prices for {}",
                debugRoutes.stream().map((route) -> route.getName()).collect(Collectors.toList()));
            log.debug(
                "Have serivce levels for {}",
                debugList.stream()
                    .map((segment) -> segment.getServiceLevelTypeId())
                    .collect(Collectors.toList()));

            RouteSegment segment =
                segmentsByRoute.get(run.getRun().getRoute().getId()).stream()
                    .filter(
                        (s) -> {
                          return run.getServiceTypeTime()
                              .getServiceLevelType()
                              .getId()
                              .equals(s.getServiceLevelTypeId());
                        })
                    .findFirst()
                    .orElse(null);

            if (segment == null) {
              log.debug("Not found");
              return null;
            }

            TripQuote tripQuote = new TripQuote();

            // LocalDate departingDate;

            // if(returnTrip) {
            // 	departingDate = new LocalDate(tripSearch.getReturningDate());
            // } else {
            // 	departingDate = new LocalDate(tripSearch.getDepartureDate());
            // }

            LocalTime departingTime =
                new DateTime(run.getServiceTypeTime().getDepartureTime())
                    .withZoneRetainFields(DateTimeZone.UTC)
                    .withZone(DateTimeZone.UTC)
                    .toLocalTime();

            DateTime runDate = new DateTime(run.getDate(), zone);
            LocalTime zonedTime =
                runDate
                    .toLocalDate()
                    .toLocalDateTime(departingTime)
                    .toDateTime(DateTimeZone.UTC)
                    .withZone(zone)
                    .toLocalTime();

            LocalTime testZonedTime =
                runDate
                    .plusDays(1)
                    .toLocalDate()
                    .toLocalDateTime(departingTime)
                    .toDateTime(DateTimeZone.UTC)
                    .withZone(zone)
                    .toLocalTime();

            if (zonedTime.getHourOfDay() != testZonedTime.getHourOfDay()) {
              zonedTime = testZonedTime;
              runDate = runDate.plusDays(1);
            }

            DateTime utcDepartingTime =
                new DateTime(run.getServiceTypeTime().getDepartureTime())
                    .withZoneRetainFields(DateTimeZone.UTC)
                    .withZone(DateTimeZone.getDefault());

            departingTime = utcDepartingTime.toLocalTime();

            /*
            LocalDateTime departingDateTime = new DateTime(run.getDate())
              .toLocalDate()
              .toLocalDateTime(departingTime);
            */

            LocalDateTime departingDateTime =
                new DateTime(run.getDate(), zone)
                    .withTime(zonedTime)
                    .withZone(DateTimeZone.UTC)
                    .toLocalDateTime();

            LocalDateTime segmentDepartureDate =
                departingDateTime.plusMinutes(segment.getExtra().getPreviousTime().intValue());
            LocalDateTime arrivalDateTime =
                segmentDepartureDate.plusMinutes(segment.getExtra().getTime().intValue());
            tripQuote.setReverse(segment.getReverse());

            log.debug(
                "Looking at date {} {} {}",
                run.getRun().getRoute().getName(),
                departingDateTime,
                departingTime);

            // if(returnTrip) {
            // 	tripQuote.setOrigin(segment.getEndingStop());
            // 	tripQuote.setDestination(segment.getStartingStop());
            // } else {
            tripQuote.setOrigin(segment.getStartingStop());
            tripQuote.setDestination(segment.getEndingStop());
            // }

            StopOff originStop =
                run.getRun().getStops().stream()
                    .filter(
                        (stop) -> {
                          return stop.getId().equals(tripQuote.getOrigin().getId())
                              && !stop.getCheckpoint();
                        })
                    .findFirst()
                    .orElse(null);

            StopOff destinationStop =
                run.getRun().getStops().stream()
                    .filter(
                        (stop) -> {
                          return stop.getId().equals(tripQuote.getDestination().getId())
                              && !stop.getCheckpoint();
                        })
                    .findFirst()
                    .orElse(null);

            Long originOrderIndex =
                Optional.ofNullable(originStop).map(StopOff::getOrderIndex).orElse(-1l);

            Long destinationOrderIndex =
                Optional.ofNullable(originStop).map(StopOff::getOrderIndex).orElse(-1l);
            if (originOrderIndex < 0) {
              log.error(
                  "Cannot find starting stop {} {}",
                  tripSearch.getOrigin(),
                  tripSearch.getDestination());
              return null;
            }

            if (destinationOrderIndex < 0) {
              StopOff destination =
                  run.getRun().getStops().stream()
                      .filter(
                          (stop) -> {
                            return stop.getName().equals(tripSearch.getDestination())
                                && !stop.getCheckpoint();
                          })
                      .findFirst()
                      .orElse(null);
              if (destination == null) {
                return null;
              }
              destinationOrderIndex = destination.getOrderIndex();
              segment.getEndingStop().setId(destination.getId());
              routeSegmentRepository.save(segment);
              log.error(
                  "Cannot find ending stop {} {}: {}",
                  tripSearch.getOrigin(),
                  tripSearch.getDestination(),
                  destinationOrderIndex);
            }

            Trip trip =
                tripRepository.findByRunIdAndServiceLevelTypeAndDepartureDate(
                    run.getRun().getId(),
                    run.getServiceTypeTime().getServiceLevelType(),
                    departingDateTime.toDate());
            tripQuote.setBus(
                Optional.ofNullable(trip)
                    .map(Trip::getBus)
                    .map(bus -> mapper.map(bus, BusData.class))
                    .orElseGet(
                        () -> {
                          BusData busData = new BusData();
                          if (trip != null) {
                            DefaultBus defaultBus =
                                defaultBusRepository.findByServiceLevel(trip.getServiceLevelType());
                            if (defaultBus != null) {
                              busData.setCafeteria(defaultBus.getCafeteria());
                              busData.setHeadphones(defaultBus.getHeadphones());
                              busData.setWifi(defaultBus.getWifi());
                              busData.setBathroom(defaultBus.getBathroom());
                            }
                          }
                          return busData;
                        }));

            Optional.ofNullable(trip)
                .ifPresent(
                    trip1 -> {
                      tripQuote.setReservedTickets(
                          tripSeatRepository.countByTripIdAndStatus(
                              trip1.getId(), SeatStatus.RESERVED));
                      log.debug("Found trip {} {}", trip1.getId(), trip1.getBus());
                    });

            Parameter parameter = parameterRepository.findOne("student-discount");
            float studentPricePercentage =
                parameter != null && parameter.getValue().equals("enabled") ? 0.7f : 1.0f;

            BigDecimal totalPrice =
                segment
                    .getPrice()
                    .multiply(BigDecimal.valueOf(tripSearch.getAdultCount()))
                    .add(
                        segment
                            .getPrice()
                            .multiply(BigDecimal.valueOf(0.7f))
                            .multiply(BigDecimal.valueOf(tripSearch.getChildrenCount()))
                            .setScale(0, RoundingMode.CEILING))
                    .add(
                        segment
                            .getPrice()
                            .multiply(BigDecimal.valueOf(studentPricePercentage))
                            .multiply(
                                BigDecimal.valueOf(
                                    Optional.ofNullable(tripSearch.getStudentCount()).orElse(0)))
                            .setScale(0, RoundingMode.CEILING))
                    .add(
                        segment
                            .getPrice()
                            .multiply(BigDecimal.valueOf(0.7f))
                            .multiply(BigDecimal.valueOf(tripSearch.getOlderAdultCount()))
                            .setScale(0, RoundingMode.CEILING));

            tripQuote.setServiceLevel(run.getServiceTypeTime().getServiceLevelType().getName());
            tripQuote.setServiceLevelId(run.getServiceTypeTime().getServiceLevelType().getId());
            tripQuote.setReverse(originOrderIndex > destinationOrderIndex);
            tripQuote.setRouteDepartingDate(departingDateTime.toDate());
            tripQuote.setDepartingDate(segmentDepartureDate.toDate());
            tripQuote.setArrivalDate(arrivalDateTime.toDate());
            tripQuote.setRoute(mapper.map(run.getRun().getRoute(), RouteData.class));
            tripQuote.setBasePrice(segment.getPrice());
            tripQuote.setAdultPrice(segment.getPrice());
            tripQuote.setOlderAdultPrice(
                segment
                    .getPrice()
                    .multiply(BigDecimal.valueOf(0.7f))
                    .setScale(0, RoundingMode.CEILING));
            tripQuote.setStudentPrice(
                segment
                    .getPrice()
                    .multiply(BigDecimal.valueOf(studentPricePercentage))
                    .setScale(0, RoundingMode.CEILING));
            tripQuote.setChildPrice(
                segment
                    .getPrice()
                    .multiply(BigDecimal.valueOf(0.7f))
                    .setScale(0, RoundingMode.CEILING));
            tripQuote.setTotalPrice(totalPrice);
            tripQuote.setInfantPrice(BigDecimal.ZERO);
            tripQuote.setExtra(segment.getExtra());

            return tripQuote;
          };
        };

    DateTime startDate =
        new DateTime(tripSearch.getDepartureDate())
            .withZone(zone)
            .minusDays(2)
            .withTimeAtStartOfDay();
    DateTime endDate =
        new DateTime(tripSearch.getDepartureDate())
            .withZone(zone)
            .plusDays(2)
            .withTimeAtStartOfDay();

    List<TripQuote> departureQuotes =
        departureRuns.stream()
            .map(getQuote.apply(false))
            .filter((quote) -> quote != null)
            .filter(
                quote -> {
                  DateTime date = new DateTime(quote.getDepartingDate());
                  return (date.isAfter(startDate) || date.isEqual(startDate))
                      && (date.isBefore(endDate) || date.isEqual(endDate));
                })
            .distinct()
            .collect(Collectors.toList());

    DateTime returnStartDate =
        new DateTime(tripSearch.getReturningDate())
            .withZone(zone)
            .minusDays(2)
            .withTimeAtStartOfDay();
    DateTime returnEndDate =
        new DateTime(tripSearch.getReturningDate())
            .withZone(zone)
            .plusDays(2)
            .withTimeAtStartOfDay();

    log.debug("Found quotes {}", departureQuotes.size());
    List<TripQuote> returnQuotes =
        returnRuns.stream()
            .map(getQuote.apply(true))
            .filter((quote) -> quote != null)
            .filter(
                quote -> {
                  DateTime date = new DateTime(quote.getDepartingDate());
                  return (date.isAfter(returnStartDate) || date.isEqual(returnStartDate))
                      && (date.isBefore(returnEndDate) || date.isEqual(returnEndDate));
                })
            .distinct()
            .collect(Collectors.toList());
    log.debug("Found quotes {} {}", departureQuotes.size(), returnQuotes.size());

    SalesQuote salesQuote = new SalesQuote();

    salesQuote.setDepartureQuotes(departureQuotes);
    salesQuote.setReturnQuotes(returnQuotes);
    salesQuote.setPassengers(passengers);
    salesQuote.setMaxOlderAdults(maxOlderAdults);
    salesQuote.setMaxStudents(maxStudents);

    return salesQuote;
  }

  public List<SeatData> findSeatData(String tripId, String seatName) {
    Trip trip = tripRepository.findOne(tripId);
    List<TripSeat> seats = tripSeatRepository.findAllByTripAndSeatName(trip, seatName);

    return seats.stream()
        .sorted(
            (s1, s2) -> {
              if (trip.getReverse()) {
                return s2.getStartingStop()
                    .getOrderIndex()
                    .compareTo(s1.getEndingStop().getOrderIndex());
              } else {
                return s1.getStartingStop()
                    .getOrderIndex()
                    .compareTo(s2.getEndingStop().getOrderIndex());
              }
            })
        .map(
            (seat) -> {
              SeatData seatData = new SeatData();
              seatData.setStartingStop(seat.getStartingStop().getName());
              seatData.setEndingStop(seat.getEndingStop().getName());
              return seatData;
            })
        .collect(Collectors.toList());
  }

  public TripData getTripData(
      String routeId,
      String serviceLevelId,
      String originId,
      String destinationId,
      Date tripDate,
      final Boolean reverse) {
    List<Run> runs = runRepository.findByRouteId(routeId);

    Run currentRun =
        runs.stream()
            .filter(
                (run) -> {
                  log.info("Looking for {}", run.getName());
                  return run.getServiceTypeTimes().stream()
                      .anyMatch(
                          (schedule) -> {
                            DateTime testingDate = new DateTime(schedule.getDepartureTime());
                            DateTime tomorrowTestingDate = testingDate.plusDays(1);
                            DateTime pivotTime = testingDate;

                            if (tomorrowTestingDate.getHourOfDay() != testingDate.getHourOfDay()) {
                              pivotTime = tomorrowTestingDate;
                            }

                            LocalDate scheduleDate = new LocalDate(tripDate);
                            DateTime utcDepartingTime =
                                pivotTime
                                    .withZoneRetainFields(DateTimeZone.UTC)
                                    .withZone(DateTimeZone.getDefault());

                            LocalTime departingTime = utcDepartingTime.toLocalTime();
                            LocalDateTime scheduledDateTime =
                                scheduleDate.toLocalDateTime(departingTime);
                            LocalDateTime askedDateTime = new LocalDateTime(tripDate);
                            log.info(
                                "TIME {} {} {}",
                                askedDateTime,
                                scheduledDateTime,
                                askedDateTime.equals(scheduledDateTime));
                            return askedDateTime.equals(scheduledDateTime);
                          });
                })
            .findFirst()
            .get();

    ServiceLevelType serviceLevelType = serviceLevelTypeRepository.findOne(serviceLevelId);

    Trip trip =
        tripRepository.findByRunIdAndServiceLevelTypeAndDepartureDate(
            currentRun.getId(), serviceLevelType, tripDate);

    if (trip == null) {
      trip = tripService.createTrip(currentRun.getId(), serviceLevelId, tripDate, reverse);
    }

    log.debug("About to check for run {} {}", currentRun.getName(), currentRun.getId());

    StopOff startingStop =
        currentRun.getStops().stream()
            .filter(
                (stop) -> {
                  return stop.getId().equals(originId);
                })
            .findFirst()
            .get();

    StopOff endingStop =
        currentRun.getStops().stream()
            .filter(
                (stop) -> {
                  return stop.getId().equals(destinationId);
                })
            .findFirst()
            .get();

    List<RouteSegment.StopData> tripStops =
        currentRun.getStops().stream()
            .filter(
                (stop) -> {
                  if (reverse) {
                    return stop.getOrderIndex() <= startingStop.getOrderIndex()
                        && stop.getOrderIndex() >= endingStop.getOrderIndex();
                  } else {
                    return stop.getOrderIndex() >= startingStop.getOrderIndex()
                        && stop.getOrderIndex() <= endingStop.getOrderIndex();
                  }
                })
            .sorted(
                (stop1, stop2) -> {
                  if (reverse) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .map(
                (stop) -> {
                  return mapper.map(stop, RouteSegment.StopData.class);
                })
            .collect(Collectors.toList());

    List<StopOff> allStops =
        currentRun.getStops().stream()
            .sorted(
                (stop1, stop2) -> {
                  if (reverse) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .filter(stop -> !stop.getCheckpoint())
            .collect(Collectors.toList());

    tripStops.get(0).setTravelMinutes(0l);

    List<BusPosition> busPositions = findBusPositions(trip);

    Integer maxRow =
        busPositions.stream()
            .mapToInt(
                (position) -> {
                  return position.getRow();
                })
            .max()
            .getAsInt();

    Integer maxColumn =
        busPositions.stream()
            .mapToInt(
                (position) -> {
                  return position.getColumn();
                })
            .max()
            .getAsInt();

    Integer maxFloor =
        busPositions.stream()
            .mapToInt(
                (position) -> {
                  return position.getFloor();
                })
            .max()
            .getAsInt();

    List<TripSeat> tickets = tripSeatRepository.findAllByTrip(trip);

    Map<String, List<TripSeat>> tripSeats =
        tickets.stream().collect(Collectors.groupingBy(TripSeat::getSeatName));

    BusPositionData[][][] positions = new BusPositionData[maxFloor][maxRow + 1][maxColumn + 1];

    Map<String, Occupation> occupiedSeat =
        checkOccupation(tripSeats, allStops, originId, destinationId);

    busPositions.forEach(
        (position) -> {
          int floor = position.getFloor() - 1;
          int row = position.getRow();
          int column = position.getColumn();

          positions[floor][row][column] = mapper.map(position, BusPositionData.class);

          if (occupiedSeat.containsKey(position.getName())) {
            positions[floor][row][column].setReserved(
                occupiedSeat.get(position.getName()).isOccupied());
            positions[floor][row][column].setPartiallyReserved(
                occupiedSeat.get(position.getName()).isOccupied());
            positions[floor][row][column].setStatus(
                occupiedSeat.get(position.getName()).getStatus());
          }

          /*
          if(tripSeats.containsKey(position.getName())) {
                // List<TripSeat> seats = tripSeats.get(position.getName());
          	positions[floor][row][column].setReserved(true);
          	positions[floor][row][column].setPartiallyReserved(true);
          }
          */
        });

    Map<PassengerType, Long> typeCounts =
        tripSeatRepository.findAllByTrip(trip).stream()
            .collect(Collectors.groupingBy(TripSeat::getPassengerType, Collectors.counting()));

    TripData data = new TripData();
    RouteData routeData = new RouteData();

    routeData.setId(trip.getRun().getRoute().getId());
    routeData.setName(trip.getRun().getRoute().getName());

    data.setMaxStudents(maxStudents);
    data.setMaxOlderAdults(maxOlderAdults);
    if ("Sección 42".equals(trip.getServiceLevelType().getName())) {
      data.setMaxReserved(10l);
    } else {
      data.setMaxReserved(maxReserved);
    }
    data.setReservedCount(
        tickets.stream()
            .filter(t -> t.getStatus() == SeatStatus.RESERVED)
            .collect(Collectors.counting()));
    data.setOlderAdultCount(typeCounts.getOrDefault(PassengerType.OLDER_ADULT, 0l));
    data.setStudentCount(typeCounts.getOrDefault(PassengerType.STUDENT, 0l));
    data.setReverse(reverse);
    data.setId(trip.getId());
    data.setStops(tripStops);
    data.setRouteData(routeData);
    data.setPositions(positions);

    if (trip.getBus() != null) {
      data.setWifi(trip.getBus().getWifi());
      data.setBathroom(trip.getBus().getBathroom());
      data.setCafeteria(trip.getBus().getCafeteria());
      data.setHeadphones(trip.getBus().getHeadphones());
    } else {
      DefaultBus defaultBus = defaultBusRepository.findByServiceLevel(trip.getServiceLevelType());
      if (defaultBus != null) {
        data.setWifi(defaultBus.getWifi());
        data.setBathroom(defaultBus.getBathroom());
        data.setCafeteria(defaultBus.getCafeteria());
        data.setHeadphones(defaultBus.getHeadphones());
      }
    }

    return data;
  }

  private Map<String, Occupation> checkOccupation(
      Map<String, List<TripSeat>> tripSeats,
      List<StopOff> tripStops,
      String originId,
      String destinationId) {
    int index = 0;
    Map<String, Occupation> result = new HashMap<>();

    Map<String, Integer> stopDict = new HashMap<>();
    for (StopOff stop : tripStops) {
      stopDict.put(stop.getId(), index++);
    }

    Integer wantedStart = stopDict.get(originId);
    Integer wantedEnd = stopDict.get(destinationId);

    HashSet<Integer> wantedPosition = new HashSet<>();

    log.debug("We want this positions {} {}", originId, destinationId);
    log.debug("We got this {} {}", wantedStart, wantedEnd);

    for (int i = wantedStart; i < wantedEnd; i++) {
      wantedPosition.add(i);
    }

    log.debug("Wanted position: {}", wantedPosition);

    tripSeats.forEach(
        (seatName, tickets) -> {
          HashSet<Integer> occupiedPositions = new HashSet<>();

          AtomicBoolean justReserved = new AtomicBoolean(false);

          tickets.forEach(
              ticket -> {
                Integer startingPosition = stopDict.get(ticket.getStartingStop().getId());
                Integer endingPosition = stopDict.get(ticket.getEndingStop().getId());

                if (ticket.getStatus() == SeatStatus.RESERVED) {
                  justReserved.set(true);
                }

                log.debug("Data 1 {} {}", startingPosition, ticket.getStartingStop().getId());
                log.debug("Data 2 {} {}", endingPosition, ticket.getEndingStop().getId());

                for (int i = startingPosition; i < endingPosition; i++) {
                  occupiedPositions.add(i);
                }
              });

          log.debug("Occupation {} {}", seatName, occupiedPositions);

          HashSet<Integer> intersection = new HashSet<>(wantedPosition);
          intersection.retainAll(occupiedPositions);

          result.put(
              seatName,
              new Occupation(
                  intersection.size() > 0,
                  justReserved.get() ? SeatStatus.RESERVED : SeatStatus.OCCUPIED));
        });

    return result;
  }

  private List<BusPosition> findBusPositions(Trip trip) {
    return Optional.ofNullable(trip.getBus())
        .map(
            (bus) -> {
              return bus.getPositions();
            })
        .orElseGet(
            () -> {
              return defaultBusRepository
                  .findByServiceLevel(trip.getServiceLevelType())
                  .getPositions();
            });
  }

  private List<RunServiceHolder> findRunAlternatives(
      Map<String, List<RouteSegment>> segmentsByRoute,
      List<Route> routes,
      Date date,
      String tripType) {
    List<RunServiceHolder> alternatives = findAlternatives(segmentsByRoute, routes, date);

    List<DayOfWeek> daysOfWeek =
        Arrays.asList(date).stream().map(DayOfWeek::extractDayOfWeek).collect(Collectors.toList());

    log.debug(
        "The requested date {} is {} with {} alternatives", date, daysOfWeek, alternatives.size());

    return alternatives.stream()
        .filter(
            (holder) -> {
              return holder.getServiceTypeTime().getDays().stream()
                  .anyMatch(
                      day -> {
                        return daysOfWeek.contains(day);
                      });
            })
        .collect(Collectors.toList());
  }

  private List<RunServiceHolder> findAlternatives(
      Map<String, List<RouteSegment>> segmentsByRoute, List<Route> routes, Date date) {
    if (routes.size() == 0) {
      return new ArrayList<>();
    }

    List<Run> runs = runRepository.findValidInRoutes(routes, date);

    List<RunServiceHolder> runSchedules =
        runs.stream()
            .flatMap(
                (run) -> {
                  return run.getServiceTypeTimes().stream()
                      .map(
                          (serviceTypeTime) -> {
                            log.debug("Route {}", run.getRoute().getId());
                            if (run.getName().equals("9060")) {
                              log.debug("Asked for time {}", serviceTypeTime.getDepartureTime());
                            }
                            return new RunServiceHolder(run, date, serviceTypeTime);
                          });
                })
            .collect(Collectors.toList());

    return runSchedules.stream()
        .filter(
            (holder) -> {
              RouteSegment routeSegment =
                  segmentsByRoute.get(holder.getRun().getRoute().getId()).get(0);
              return routeSegment.getReverse() == holder.getServiceTypeTime().getReverse();
            })
        .collect(Collectors.toList());
  }

  private Map<String, RouteSegment.SegmentData> calculateData(
      Route route, List<StopOff> routeStops) {
    Map<String, RouteSegment.SegmentData> results = new HashMap<>();

    Long previousTime = 0l;

    for (int i = 0; i < routeStops.size(); i++) {
      StopOff previousStop = routeStops.get(i);

      if (previousStop.getTravelMinutes() == null) {
        previousStop.setTravelMinutes(0l);
      }

      if (previousStop.getWaitingMinutes() == null) {
        previousStop.setWaitingMinutes(0l);
      }

      previousTime += previousStop.getTravelMinutes();
      previousTime += previousStop.getWaitingMinutes();

      Long currentTime = 0l;
      Long shownCurrentTime = 0l;
      Long currentDistance = 0l;
      Long stops = 0l;

      for (int j = i + 1; j < routeStops.size(); j++) {
        StopOff currentStop = routeStops.get(j);

        stops++;

        currentTime += currentStop.getTravelMinutes();
        shownCurrentTime += currentStop.getTravelMinutes();

        currentDistance += currentStop.getKilometers().longValue();
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(previousStop.getId());
        idBuilder.append("@");
        idBuilder.append(currentStop.getId());
        results.put(
            idBuilder.toString(),
            new RouteSegment.SegmentData(
                currentTime, currentDistance, stops, previousTime, shownCurrentTime));
        currentTime += currentStop.getWaitingMinutes();
      }
    }

    return results;
  }

  public void indexRoute(String id) {
    indexRoute(routeRepository.findOne(id));
  }

  private static class RunServiceHolder {

    private Run run;

    private Date date;

    private ServiceTypeTime serviceTypeTime;

    public RunServiceHolder(Run run, Date date, ServiceTypeTime serviceTypeTime) {
      setRun(run);
      setDate(date);
      setServiceTypeTime(serviceTypeTime);
    }

    /** @return the run */
    public Run getRun() {
      return run;
    }

    /** @param run the run to set */
    public void setRun(Run run) {
      this.run = run;
    }

    /** @return the date */
    public Date getDate() {
      return date;
    }

    /** @param date the date to set */
    public void setDate(Date date) {
      this.date = date;
    }

    /** @return the serviceTypeTime */
    public ServiceTypeTime getServiceTypeTime() {
      return serviceTypeTime;
    }

    /** @param serviceTypeTime the serviceTypeTime to set */
    public void setServiceTypeTime(ServiceTypeTime serviceTypeTime) {
      this.serviceTypeTime = serviceTypeTime;
    }
  }

  public static class Occupation {
    private boolean occupied;
    private SeatStatus status;

    public Occupation(boolean occupied, SeatStatus status) {
      this.occupied = occupied;
      this.status = status;
    }

    public boolean isOccupied() {
      return occupied;
    }

    public void setOccupied(boolean occupied) {
      this.occupied = occupied;
    }

    public SeatStatus getStatus() {
      return status;
    }

    public void setStatus(SeatStatus status) {
      this.status = status;
    }
  }
}
