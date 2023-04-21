package justcloud.tickets.service.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import justcloud.tickets.domain.repository.TripRepository;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.dto.BusLocationData;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateInfo {

  @Autowired private Utils utils;

  @Autowired private TripRepository tripRepository;

  public BusLocationData checkRoute(BusLocationData busLocationData) {
    Trip trip = tripRepository.findOne(busLocationData.getTrip().getId());
    List<Segment> routeSegments = getTripSegments(trip);
    int numSegments = routeSegments.size();
    Coord busCoord = new Coord(busLocationData.getLatitude(), busLocationData.getLongitude());
    DateTime lastGpsUpdate = new DateTime(busLocationData.getGpsTime());
    DateTime departureDate = new DateTime(trip.getDepartureDate());
    int travelTime = (int) ((lastGpsUpdate.getMillis() - departureDate.getMillis()) / 60000);
    // logger.debug("Bus: {}, Num Segments: {}", bus.id, numSegments)
    for (int i = numSegments - 1; i >= 0; i--) {
      Segment segment = routeSegments.get(i);
      if (segment.atEnd(busCoord, utils.atEndRange)) {
        long estimatedTime =
            segment.getTimeFromStart() + segment.getSegmentTime() + segment.getRestTime();
        long delayedTime = travelTime - estimatedTime;
        busLocationData.setCurrentlyDelayed(delayedTime >= utils.minDelay);
        if (busLocationData.getCurrentlyDelayed()) {
          busLocationData.setDelayedTime(delayedTime);
        } else {
          busLocationData.setDelayedTime(0l);
        }
        busLocationData.setOffRoute(false);
        return busLocationData;
      }
      Vector v = segment.vectorToCoord(busCoord);
      double distance = segment.distanceFrom(v);
      double over = segment.projectedSize(v);
      // logger.debug("Segment={}, distance={}, over={}, size={}", i, distance, over,
      // segment.getSize())
      if ((distance <= utils.maxDistance)
          && (-utils.outRange <= over)
          && (over <= segment.getSize() + utils.outRange)) {
        double proportion = segment.proportion(v);
        if (proportion < 0.0) proportion = 0.0;
        else if (proportion >= 1.0) proportion = 1.0;
        long estimatedTime =
            segment.getTimeFromStart() + (int) (segment.getSegmentTime() * proportion);
        long delayedTime = travelTime - estimatedTime;
        busLocationData.setCurrentlyDelayed(delayedTime >= utils.minDelay);

        if (busLocationData.getCurrentlyDelayed()) {
          busLocationData.setDelayedTime(delayedTime);
        } else {
          busLocationData.setDelayedTime(0l);
        }
        busLocationData.setOffRoute(false);
        return busLocationData;
      }
    }
    busLocationData.setOffRoute(true);
    return busLocationData;
  }

  List<Segment> getTripSegments(Trip trip) {
    List<Segment> segments = new ArrayList<>();
    List<StopOff> stops =
        trip.getRun().getStops().stream()
            .sorted((s1, s2) -> s1.getOrderIndex().compareTo(s2.getOrderIndex()))
            .collect(Collectors.toList());
    long totalTime =
        trip.getRun().getStops().stream()
            .mapToLong(
                t ->
                    Optional.ofNullable(t.getTravelMinutes()).orElse(0l)
                        + Optional.ofNullable(t.getWaitingMinutes()).orElse(0l))
            .sum();
    long currentTime = 0;
    for (int i = 1; i < stops.size(); i++) {
      currentTime += Optional.ofNullable(stops.get(i).getTravelMinutes()).orElse(0l);

      segments.add(
          new Segment(
              new Coord(
                  stops.get(i - 1).getScale().getLatitude(),
                  stops.get(i - 1).getScale().getLongitude()),
              new Coord(
                  stops.get(i).getScale().getLatitude(), stops.get(i).getScale().getLongitude()),
              Optional.ofNullable(stops.get(i).getTravelMinutes()).orElse(0l),
              totalTime - currentTime,
              currentTime));
    }

    return segments;
  }
}
