package justcloud.tickets.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.repository.BusRepository;
import justcloud.tickets.domain.repository.EmployeeRepository;
import justcloud.tickets.domain.repository.TripRepository;
import justcloud.tickets.domain.tickets.Bus;
import justcloud.tickets.domain.tickets.BusStatus;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.dto.BusData;
import justcloud.tickets.dto.DriverData;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BusService {

  @Autowired private BusRepository busRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private TripService tripService;

  @Autowired private MapperFacade mapper;

  public List<BusData> listBusGeneralData() {
    return StreamSupport.stream(busRepository.findAll().spliterator(), false)
        .map(
            bus -> {
              BusData data = mapper.map(bus, BusData.class);

              DriverData driver1 = null;
              DriverData driver2 = null;

              if (bus.getDriver1() != null) {
                StringBuilder builder = new StringBuilder();

                if (bus.getDriver1().getName() != null) {
                  builder.append(bus.getDriver1().getName());
                }

                if (bus.getDriver1().getLastName() != null) {
                  builder.append(" ");
                  builder.append(bus.getDriver1().getLastName());
                }

                driver1 = new DriverData();
                driver1.setId(bus.getDriver1().getId());
                driver1.setName(builder.toString());
              }

              if (bus.getDriver2() != null) {
                StringBuilder builder = new StringBuilder();

                if (bus.getDriver2().getName() != null) {
                  builder.append(bus.getDriver2().getName());
                }

                if (bus.getDriver2().getLastName() != null) {
                  builder.append(" ");
                  builder.append(bus.getDriver2().getLastName());
                }
                driver2 = new DriverData();
                driver2.setId(bus.getDriver2().getId());
                driver2.setName(builder.toString());
              }

              data.setDriver1(driver1);
              data.setDriver2(driver2);

              return data;
            })
        .collect(Collectors.toList());
  }

  public void assignDrivers(BusData busData) {
    Bus bus = busRepository.findOne(busData.getId());

    if (!StringUtils.isEmpty(busData.getStatus())) {
      bus.setStatus(BusStatus.valueOf(busData.getStatus()));
    }

    if (busData.getServiceType() != null) {}

    if (busData.getDriver1() != null) {
      Employee driver = employeeRepository.findOne(busData.getDriver1().getId());
      bus.setDriver1(driver);
    }

    if (busData.getDriver2() != null) {
      Employee driver = employeeRepository.findOne(busData.getDriver1().getId());
      bus.setDriver2(driver);
    }

    busRepository.save(bus);
  }

  public void assignBus(Map<String, Object> data) {
    String busId = (String) data.get("busId");
    String runId = (String) data.get("runId");
    String tripId = (String) data.get("tripId");
    String serviceTypeId = (String) data.get("serviceTypeId");

    String departureDateString = (String) data.get("departureDate");
    Boolean reverse = (Boolean) data.get("reverse");
    Date runDate = new DateTime(departureDateString).toDate();

    // log.info("Creando autobus {} {}", departureDateString, runDate);

    Trip trip;

    if (tripId == null) {
      trip = tripService.createTrip(runId, serviceTypeId, runDate, reverse);
    } else {
      trip = tripRepository.findOne(tripId);
    }

    trip.setBus(busRepository.findOne(busId));
    tripRepository.save(trip);
  }
}
