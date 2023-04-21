package justcloud.tickets.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import justcloud.tickets.domain.*;
import justcloud.tickets.domain.EmployeeLoan.PaymentType;
import justcloud.tickets.domain.repository.*;
import justcloud.tickets.domain.sales.JoinedPayment;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.domain.tickets.TripExpense;
import justcloud.tickets.dto.*;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.util.TimeZoneUtils;
import ma.glasnost.orika.MapperFacade;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TripService {

  private Logger log = LoggerFactory.getLogger(TripService.class);

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private CancelReservationRepository cancelReservationRepository;

  @Autowired private JoinedPaymentRepository joinedPaymentRepository;

  @Autowired private SalesTerminalRepository salesTerminalRepository;

  @Autowired private UserRepository userRepository;
  @Autowired private OfficeLocationRepository officeLocationRepository;

  @Autowired private EmployeePeriodicPaymentRepository employeePeriodicPaymentRepository;

  @Autowired private UserService userService;

  @Autowired private PaymentShiftRepository paymentShiftRepository;

  @Autowired private JasperService jasperService;

  @Autowired private PackageTicketRepository packageTicketRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private RunRepository runRepository;

  @Autowired private TripAdvanceRepository tripAdvanceRepository;

  @Autowired private TripStopControlRepository tripStopControlRepository;

  @Autowired private DefaultBusRepository defaultBusRepository;

  @Autowired private TripSeatRepository tripSeatRepository;

  @Autowired private TripExpenseRepository tripExpenseRepository;

  @Autowired private SegmentVarRepository segmentVarRepository;

  @Autowired private EmployeeLoanRepository employeeLoanRepository;

  @Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;

  @Autowired private EmployeeAccountRepository employeeAccountRepository;

  @Autowired private StopOffRepository stopOffRepository;

  @Autowired private MapperFacade mapper;

  public List<StopOffData> listStops() {
    return StreamSupport.stream(stopOffRepository.findAll().spliterator(), false)
        .sorted((stop1, stop2) -> stop1.getName().compareTo(stop2.getName()))
        .map(stop -> stop.getName())
        .distinct()
        .map(
            stopName -> {
              StopOffData stop = new StopOffData();
              stop.setName(stopName);
              return stop;
            })
        .collect(Collectors.toList());
  }

  public List<TripBusData> listTrips(Date startDate, Date endingDate) {
    return tripRepository.findAllTripsByDepartureDateBetween(startDate, endingDate).stream()
        .sorted((trip1, trip2) -> trip2.getDepartureDate().compareTo(trip1.getDepartureDate()))
        .map(
            trip -> {
              TripBusData data = new TripBusData();

              data.setId(trip.getId());
              data.setRoute(mapper.map(trip.getRun().getRoute(), RouteData.class));
              data.setServiceType(mapper.map(trip.getServiceLevelType(), ServiceTypeData.class));
              data.setRunId(trip.getRun().getId());
              data.setOriginId(trip.getRun().getBeginning().getId());
              data.setOriginName(trip.getRun().getBeginning().getName());
              data.setDestinationId(trip.getRun().getDestination().getId());
              data.setDestinationName(trip.getRun().getDestination().getName());
              data.setReverse(trip.getReverse());
              data.setBusNumber(
                  Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(""));

              data.setDepartureDate(trip.getDepartureDate());

              return data;
            })
        .collect(Collectors.toList());
  }

  public InputStream generatePaymentReport(String tripId, String timeZone) throws Exception {
    Trip trip = tripRepository.findOne(tripId);
    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    JoinedPayment joinedPayment = trip.getJoinedPayment();
    PaymentShift shift = joinedPayment.getPaymentShift();

    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.fullDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);

    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> params = new HashMap<>();
    params.put("fecha", dateTimeFormatter.print(new DateTime(trip.getLastUpdated())));
    params.put("noLiquidacion", joinedPayment.getPaymentNumber());
    params.put("unidad", Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(""));
    params.put("origen", trip.getRun().getBeginning().getName());
    params.put("destino", trip.getRun().getDestination().getName());
    params.put("fechaInicioViaje", dateTimeFormatter.print(new DateTime(trip.getDepartureDate())));
    params.put("fechaFinViaje", dateTimeFormatter.print(new DateTime(trip.getEstimatedArrival())));
    params.put(
        "liquidador",
        Optional.ofNullable(shift.getUser())
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));
    params.put(
        "operador1",
        Optional.ofNullable(trip.getDriver1())
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));
    params.put(
        "operador2",
        Optional.ofNullable(trip.getDriver2())
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));
    params.put("dieselCredito", format.format(findExpense(trip, "Diesel (crédito)")));
    params.put("dieselContado", format.format(findExpense(trip, "Diesel (contado)")));
    params.put("reparacion", format.format(findExpense(trip, "Mano de obra reparación")));
    params.put("estacionamiento", format.format(findExpense(trip, "Estacionamiento")));
    params.put("pistaCredito", format.format(findExpense(trip, "Autopistas (crédito)")));
    params.put("pistaContado", format.format(findExpense(trip, "Autopistas (contado)")));
    params.put("artLimpieza", format.format(findExpense(trip, "Artículos de limpieza")));
    params.put("gastosHotel", format.format(findExpense(trip, "Pensión / Hotel")));
    params.put("gastosConferencia", format.format(findExpense(trip, "Conferencia telefónica")));
    params.put("lavada", format.format(findExpense(trip, "Engrasado / Lavado")));
    params.put("refacciones", format.format(findExpense(trip, "Refacciones y accesorios")));
    params.put("otro", format.format(findExpense(trip, "Otros gastos")));
    params.put(
        "dieselTotal",
        String.valueOf(Optional.ofNullable(trip.getDieselLiters()).orElse(BigDecimal.ZERO)));

    BigDecimal subtotal1 =
        Optional.ofNullable(joinedPayment.getDriver1ExpensesAdvance())
            .orElse(BigDecimal.ZERO)
            .subtract(
                Optional.ofNullable(joinedPayment.getDriver1Expenses()).orElse(BigDecimal.ZERO));
    BigDecimal subtotal2 =
        Optional.ofNullable(joinedPayment.getDriver2ExpensesAdvance())
            .orElse(BigDecimal.ZERO)
            .subtract(
                Optional.ofNullable(joinedPayment.getDriver2Expenses()).orElse(BigDecimal.ZERO));

    BigDecimal operator1Total =
        subtotal1
            .add(Optional.ofNullable(joinedPayment.getDriver1Nominal()).orElse(BigDecimal.ZERO))
            .add(Optional.ofNullable(joinedPayment.getDriver1Insurance()).orElse(BigDecimal.ZERO))
            .add(Optional.ofNullable(joinedPayment.getDriver1Loans()).orElse(BigDecimal.ZERO));

    BigDecimal operator2Total =
        subtotal2
            .add(Optional.ofNullable(joinedPayment.getDriver2Nominal()).orElse(BigDecimal.ZERO))
            .add(Optional.ofNullable(joinedPayment.getDriver2Insurance()).orElse(BigDecimal.ZERO))
            .add(Optional.ofNullable(joinedPayment.getDriver2Loans()).orElse(BigDecimal.ZERO));

    params.put("ruta", trip.getRun().getName() + " - " + trip.getServiceLevelType().getName());
    params.put(
        "totalGastosOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1Expenses()).orElse(BigDecimal.ZERO)));
    params.put(
        "totalGastosOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2Expenses()).orElse(BigDecimal.ZERO)));

    params.put(
        "totalDescuentosOp1", format.format(operator1Total.multiply(BigDecimal.valueOf(-1))));
    params.put(
        "totalDescuentosOp2", format.format(operator2Total.multiply(BigDecimal.valueOf(-1))));

    params.put(
        "anticipoGastosOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1ExpensesAdvance())
                .orElse(BigDecimal.ZERO)));
    params.put(
        "anticipoGastosOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2ExpensesAdvance())
                .orElse(BigDecimal.ZERO)));

    params.put("subTotalOp1", format.format(subtotal1.multiply(BigDecimal.valueOf(-1))));
    params.put("subTotalOp2", format.format(subtotal2.multiply(BigDecimal.valueOf(-1))));

    params.put(
        "facAdminOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1Earnings()).orElse(BigDecimal.ZERO)));
    params.put(
        "facAdminOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2Earnings()).orElse(BigDecimal.ZERO)));

    params.put(
        "otrasRetencionesOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1Loans())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));
    params.put(
        "otrasRetencionesOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2Loans())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));

    params.put(
        "impNominaOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1Nominal())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));
    params.put(
        "impNominaOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2Nominal())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));

    params.put(
        "seguroOp1",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver1Insurance())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));
    params.put(
        "seguroOp2",
        format.format(
            Optional.ofNullable(joinedPayment.getDriver2Insurance())
                .orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(-1))));

    params.put(
        "NoNominaOp1", Optional.ofNullable(joinedPayment.getDriver1NominalWeek()).orElse(""));
    params.put(
        "NoSeguroOp1", Optional.ofNullable(joinedPayment.getDriver1InsuranceWeek()).orElse(""));

    params.put(
        "NoNominaOp2", Optional.ofNullable(joinedPayment.getDriver2NominalWeek()).orElse(""));
    params.put(
        "NoSeguroOp2", Optional.ofNullable(joinedPayment.getDriver2InsuranceWeek()).orElse(""));

    params.put("totalOp1", format.format(joinedPayment.getDriver1Amount()));
    params.put("totalOp2", format.format(joinedPayment.getDriver2Amount()));

    return new ByteArrayInputStream(jasperService.getPdfBytes("Liquidaciones", params));
  }

  public BigDecimal findExpense(Trip trip, String name) {
    return Optional.ofNullable(trip).map(Trip::getJoinedPayment).map(JoinedPayment::getExpenses)
        .map(Function.identity()).orElse(Arrays.asList()).stream()
        .filter(tripExpense -> name.equals(tripExpense.getType()))
        .map(justcloud.tickets.domain.tickets.TripExpense::getAmount)
        .reduce(
            BigDecimal.ZERO,
            (acc, amount) -> {
              return acc.add(amount);
            });
  }

  public InputStream generateLeavingPackage(String guideId, String timeZone) throws Exception {

    Trip trip = tripRepository.findOne(guideId);
    BigDecimal amount =
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::getAdvances)
            .map(List::stream)
            .orElse(Stream.empty())
            .map(TripAdvance::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String userId =
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::getPaymentShift)
            .map(PaymentShift::getUser)
            .map(User::getId)
            .orElse(null);

    PDFMergerUtility merger = new PDFMergerUtility();
    merger.addSource(generateTripGuide(guideId, userId, amount, timeZone));
    merger.addSource(generateBoardingList(guideId, timeZone));
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    merger.setDestinationStream(out);

    merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

    return new ByteArrayInputStream(out.toByteArray());
  }

  public InputStream generateLeavingPackage(
      String guideId, String userId, BigDecimal amount, String timeZone) throws Exception {
    PDFMergerUtility merger = new PDFMergerUtility();
    merger.addSource(generateTripGuide(guideId, userId, amount, timeZone));
    merger.addSource(generateBoardingList(guideId, timeZone));
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    merger.setDestinationStream(out);

    merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

    return new ByteArrayInputStream(out.toByteArray());
  }

  public InputStream generateBoardingList(String guideId, String timeZone) throws Exception {
    Trip trip = tripRepository.findOne(guideId);
    Map<String, Object> data = new HashMap<>();

    List<TripSeat> seats =
        tripSeatRepository.findAllByTrip(trip).stream().collect(Collectors.toList());

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);

    data.put("oficina", trip.getRun().getBeginning().getName());
    data.put("destino", trip.getRun().getDestination().getName());
    data.put("fecha", dateFormatter.print(new DateTime()));
    data.put("scaneados", "" + seats.size());
    data.put("horaSalida", dateFormatter.print(new DateTime(trip.getDepartureDate())));

    data.put("noAutobus", Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(""));

    List<Map<String, ?>> people =
        seats.stream()
            .sorted(
                (seat1, seat2) -> {
                  if (trip.getReverse()) {
                    return seat2
                        .getStartingStop()
                        .getOrderIndex()
                        .compareTo(seat1.getStartingStop().getOrderIndex());
                  } else {
                    return seat1
                        .getStartingStop()
                        .getOrderIndex()
                        .compareTo(seat2.getStartingStop().getOrderIndex());
                  }
                })
            .map(
                seat -> {
                  String seatChecked =
                      Optional.ofNullable(seat.getStatus())
                          .map(
                              status -> {
                                return status == SeatStatus.USED ? "Escaneado" : "No escaneado";
                              })
                          .orElse("");
                  Map<String, Object> seatData = new HashMap<>();
                  seatData.put("asiento", seat.getSeatName());
                  seatData.put("nombre", seat.getPassengerName());
                  seatData.put("noBoleto", seat.getTicketId());
                  seatData.put(
                      "observaciones",
                      getHumanReadablePassengerType(seat.getPassengerType())
                          + " "
                          + Optional.ofNullable(seat.getComments()).orElse(""));
                  seatData.put("SC", seatChecked);
                  seatData.put(
                      "origenPasajero",
                      Optional.ofNullable(seat.getStartingStop()).map(StopOff::getName).orElse(""));
                  seatData.put(
                      "destinoPasajero",
                      Optional.ofNullable(seat.getEndingStop()).map(StopOff::getName).orElse(""));
                  return seatData;
                })
            .collect(Collectors.toList());

    return new ByteArrayInputStream(
        jasperService.getPdfBytes("ListaAbordar", data, new JRMapCollectionDataSource(people)));
  }

  private String getHumanReadablePassengerType(PassengerType passengerType) {
    if (passengerType == PassengerType.OLDER_ADULT) {
      return "INAPAM";
    } else if (passengerType == PassengerType.STUDENT) {
      return "Estudiante";
    } else if (passengerType == PassengerType.INFANT) {
      return "Infante";
    } else if (passengerType == PassengerType.CHILD) {
      return "Nino";
    }
    return "Adulto";
  }

  public InputStream generateTripGuide(
      String guideId, String userId, BigDecimal amount, String timeZone) throws Exception {
    Trip trip = tripRepository.findOne(guideId);
    Map<String, Object> data = new HashMap<>();

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);

    User user = userRepository.findByUsername(userId);

    DateTime departureTime = new DateTime(trip.getDepartureDate());
    DateTime arrivalTime = new DateTime(trip.getEstimatedArrival());

    if (trip.getBus() != null) {
      if (trip.getDriver1() == null) {
        trip.setDriver1(trip.getBus().getDriver1());
      }
      if (trip.getDriver2() == null) {
        trip.setDriver2(trip.getBus().getDriver2());
      }
      tripRepository.save(trip);
    }

    data.put("noRuta", trip.getRun().getName());
    data.put("fecha", dateFormatter.print(new DateTime()));
    data.put("horaSalida", dateFormatter.print(departureTime));
    data.put("origen", trip.getRun().getBeginning().getName());
    data.put("destino", trip.getRun().getDestination().getName());
    data.put(
        "jefeOficina",
        Optional.ofNullable(user)
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));

    data.put(
        "operador1",
        Optional.ofNullable(trip.getDriver1())
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));

    data.put(
        "operador2",
        Optional.ofNullable(trip.getDriver2())
            .map(
                driver ->
                    new StringBuilder()
                        .append(Optional.ofNullable(driver.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                        .append(" ")
                        .toString())
            .orElse(""));

    data.put("noAutobus", Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(""));

    DecimalFormat format = new DecimalFormat("#,###.##");

    data.put("anticipo", "$" + format.format(amount));

    DateTime movingDate = new DateTime(trip.getDepartureDate());

    int width = 100;
    int height = 60;

    Map<String, DateTime> wrapper = new HashMap<>();
    wrapper.put("movingDate", movingDate);

    Collection<Map<String, ?>> stops =
        trip.getRun().getStops().stream()
            .sorted(
                (stop1, stop2) -> {
                  if (trip.getReverse()) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .filter(
                stop -> {
                  return !stop.getCheckpoint();
                })
            .map(
                stop -> {
                  Map<String, Object> objectData = new HashMap<>();
                  DateTime mv = wrapper.get("movingDate");
                  mv = mv.plusMinutes(stop.getWaitingMinutes().intValue());
                  /*
                  ByteArrayOutputStream out = new ByteArrayOutputStream();
                  try {
                    BitMatrix bitMatrix = new Code128Writer().encode(stop.getId() + "@" + trip.getId(), BarcodeFormat.CODE_128, width, height);
                    MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                  } catch (Exception e) {
                    log.error("Cannot generate file", e);
                  }
                  */
                  TripStopControl control =
                      tripStopControlRepository.findByTripAndStopOff(trip, stop);
                  if (control == null) {
                    control = new TripStopControl();
                    control.setStopOff(stop);
                    control.setTrip(trip);
                    control.setVisited(false);
                    tripStopControlRepository.save(control);
                  }
                  ByteArrayOutputStream qrBytes =
                      QRCode.from(control.getId()).to(ImageType.PNG).stream();
                  ByteArrayInputStream qrStream = new ByteArrayInputStream(qrBytes.toByteArray());
                  // ByteArrayInputStream qrStream = new ByteArrayInputStream(out.toByteArray());
                  objectData.put("oficina", stop.getName());
                  objectData.put("qr", qrStream);
                  mv = mv.plusMinutes(stop.getTravelMinutes().intValue());
                  objectData.put("horaPasada", timeFormatter.print(mv));
                  wrapper.put("movingDate", mv);
                  return objectData;
                })
            .collect(Collectors.toList());

    data.put("horaLlegada", dateFormatter.print(wrapper.get("movingDate")));

    return new ByteArrayInputStream(
        jasperService.getPdfBytes("GuiaViaje", data, new JRMapCollectionDataSource(stops)));
  }

  public TripStopData markVisitedStop(String id, String currentStop) {
    TripStopControl stopControl = tripStopControlRepository.findOne(id);
    if (stopControl.getStopOff().getName().equals(currentStop)) {
      stopControl.setVisited(true);
      stopControl.setVisitedTime(new Date());
      tripStopControlRepository.save(stopControl);
    }
    Trip trip = stopControl.getTrip();
    TripStopData stopData = new TripStopData();

    Date startDate = trip.getDepartureDate();

    stopData.setVisited(stopControl.getVisited());
    stopData.setId(stopControl.getId());
    stopData.setExpectedStop(startDate);
    stopData.setBeginning(mapper.map(trip.getRun().getBeginning(), StopOffData.class));
    stopData.setEnding(mapper.map(trip.getRun().getDestination(), StopOffData.class));
    stopData.setStop(mapper.map(stopControl.getStopOff(), StopOffData.class));
    stopData.setTrip(mapper.map(trip, TripData.class));
    stopData.setRunName(trip.getRun().getName());
    stopData.setDepartureDate(trip.getDepartureDate());

    if (trip.getDriver1() != null) {
      stopData.setDriver1(trip.getDriver1().getName() + " " + trip.getDriver1().getLastName());
    }

    if (trip.getBus() != null) {
      stopData.setBusNumber(trip.getBus().getBusNumber());
    }

    return stopData;
  }

  public TicketData markTicketUsed(String id) {
    TripSeat seat = tripSeatRepository.findByTicketId(id);

    TicketData data = mapper.map(seat, TicketData.class);

    if (seat.equals(SeatStatus.USED)) {
      data.setStatus("ALREADY_USED");
    } else {
      seat.setStatus(SeatStatus.USED);
      data.setStatus("USED");
      tripSeatRepository.save(seat);
    }
    return data;
  }

  public List<TripStopData> listStops(String stopOffName) {
    return tripStopControlRepository.findAllByStopOffNameAndVisited(stopOffName, false).stream()
        .map(
            stopControl -> {
              TripStopData stopData = new TripStopData();
              Trip trip = stopControl.getTrip();

              List<StopOff> stops =
                  trip.getRun().getStops().stream()
                      .sorted(
                          (stop1, stop2) -> stop1.getOrderIndex().compareTo(stop2.getOrderIndex()))
                      .collect(Collectors.toList());

              if (trip.getReverse()) {
                Collections.reverse(stops);
              }

              Date startDate = trip.getDepartureDate();

              for (StopOff stop : stops) {
                if (stop.getName().equals(stopOffName)) {
                  break;
                }
                startDate =
                    new Date(
                        startDate.getTime()
                            + (stop.getWaitingMinutes() + stop.getTravelMinutes()) * 60 * 1000);
              }

              stopData.setId(stopControl.getId());
              stopData.setExpectedStop(startDate);
              stopData.setBeginning(mapper.map(trip.getRun().getBeginning(), StopOffData.class));
              stopData.setEnding(mapper.map(trip.getRun().getDestination(), StopOffData.class));
              stopData.setStop(mapper.map(stopControl.getStopOff(), StopOffData.class));
              stopData.setTrip(mapper.map(trip, TripData.class));
              stopData.setRunName(trip.getRun().getName());
              stopData.setDepartureDate(trip.getDepartureDate());

              if (trip.getDriver1() != null) {
                stopData.setDriver1(
                    trip.getDriver1().getName() + " " + trip.getDriver1().getLastName());
              }

              if (trip.getBus() != null) {
                stopData.setBusNumber(trip.getBus().getBusNumber());
              }

              return stopData;
            })
        .collect(Collectors.toList());
  }

  public Map<String, Object> savePayment(Map<String, Object> data, boolean payed) {
    User user = userService.getCurrentUser();
    PaymentShift paymentShift = paymentShiftRepository.findByUserAndFinished(user, false);

    Trip trip = tripRepository.findOne((String) data.get("tripId"));
    if (trip.getJoinedPayment() == null) {
      trip.setJoinedPayment(new JoinedPayment());
      trip.getJoinedPayment().setTrips(new ArrayList<>(Arrays.asList(trip)));
    }
    if (trip.getJoinedPayment().isPayed()) {
      payed = false;
    } else {
      trip.getJoinedPayment().setPayed(payed);
      if (payed) {
        OfficeLocation officeLocation = paymentShift.getSalesTerminal().getOfficeLocation();
        long currentPayment = officeLocation.getCurrentPayment() + 1;
        trip.getJoinedPayment().setPaymentNumber(officeLocation.getShiftPrefix() + currentPayment);
        officeLocation.setCurrentPayment(currentPayment);
        officeLocationRepository.save(officeLocation);
        trip.getJoinedPayment().setPayDate(new Date());
      }
    }
    joinedPaymentRepository.save(trip.getJoinedPayment());

    /*
    Employee driver1 = employeeRepository.findOne((String)data.get("driver1Id"));
    Employee driver2 = employeeRepository.findOne((String)data.get("driver2Id"));

    if(driver1 != null) {
      trip.setDriver1(driver1);
    }
    if(driver2 != null) {
      trip.setDriver2(driver2);
    }
    */

    List<Map<String, Object>> trips = (List<Map<String, Object>>) data.get("trips");
    Map<String, BigDecimal> dieselLiters =
        trips.stream()
            .collect(
                Collectors.toMap(
                    tripDiesel -> {
                      return tripDiesel.get("tripId").toString();
                    },
                    tripDiesel -> {
                      try {
                        return new BigDecimal(
                            String.valueOf(tripDiesel.getOrDefault("dieselLiters", 0)));
                      } catch (Exception ex) {
                        return BigDecimal.ZERO;
                      }
                    }));

    trip.getJoinedPayment().setPaymentShift(paymentShift);

    BigDecimal driver1Amount = new BigDecimal(String.valueOf(data.get("driver1Amount")));
    BigDecimal driver2Amount = new BigDecimal(String.valueOf(data.get("driver2Amount")));
    BigDecimal driver1Earnings = new BigDecimal(String.valueOf(data.get("driver1Earnings")));
    BigDecimal driver2Earnings = new BigDecimal(String.valueOf(data.get("driver2Earnings")));
    BigDecimal driver1ExpensesAdvance =
        new BigDecimal(String.valueOf(data.get("driver1ExpensesAdvance")));
    BigDecimal driver2ExpensesAdvance =
        new BigDecimal(String.valueOf(data.get("driver2ExpensesAdvance")));
    BigDecimal driver1Expenses = new BigDecimal(String.valueOf(data.get("driver1Expenses")));
    BigDecimal driver2Expenses = new BigDecimal(String.valueOf(data.get("driver2Expenses")));
    BigDecimal driver1Loans = new BigDecimal(String.valueOf(data.get("driver1Loans")));
    BigDecimal driver2Loans = new BigDecimal(String.valueOf(data.get("driver2Loans")));
    BigDecimal ds;
    try {
      ds = new BigDecimal(String.valueOf(Optional.ofNullable(data.get("dieselCost")).orElse("0")));
    } catch (Exception ex) {
      ds = BigDecimal.ZERO;
    }
    BigDecimal dieselCost = ds;

    BigDecimal newDebt1 = driver1Amount.multiply(BigDecimal.valueOf(-1));
    BigDecimal newDebt2 = driver2Amount.multiply(BigDecimal.valueOf(-1));

    log.info("Driver 1 amount {}", driver1Amount);
    log.info("Driver 2 amount {}", driver2Amount);

    final DateTimeZone zone = DateTimeZone.forID("America/Mexico_City");
    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);

    trip.getJoinedPayment()
        .getTrips()
        .forEach(
            t -> {
              t.setDieselLiters(dieselLiters.getOrDefault(t.getId(), BigDecimal.ZERO));
              tripRepository.save(t);
            });

    trip.getJoinedPayment()
        .getTrips()
        .forEach(
            t -> {
              t.setDieselCost(Optional.ofNullable(dieselCost).orElse(BigDecimal.ZERO));
              tripRepository.save(t);
            });

    trip.setDieselCost(Optional.ofNullable(dieselCost).orElse(BigDecimal.ZERO));
    trip.getJoinedPayment().setDriver1Amount(driver1Amount);
    trip.getJoinedPayment().setDriver2Amount(driver2Amount);
    trip.getJoinedPayment().setDriver1Earnings(driver1Earnings);
    trip.getJoinedPayment().setDriver2Earnings(driver2Earnings);
    trip.getJoinedPayment().setDriver1ExpensesAdvance(driver1ExpensesAdvance);
    trip.getJoinedPayment().setDriver2ExpensesAdvance(driver2ExpensesAdvance);
    trip.getJoinedPayment().setDriver1Loans(driver1Loans);
    trip.getJoinedPayment().setDriver2Loans(driver2Loans);
    trip.getJoinedPayment().setDriver1Expenses(driver1Expenses);
    trip.getJoinedPayment().setDriver2Expenses(driver2Expenses);

    driver1Amount = Optional.ofNullable(driver1Amount).orElse(BigDecimal.ZERO);

    driver2Amount = Optional.ofNullable(driver2Amount).orElse(BigDecimal.ZERO);

    BigDecimal total = driver1Amount.add(driver2Amount);

    // paymentShift.getSalesTerminal().setPaymentAvailableAmount(total);
    // salesTerminalRepository.save(paymentShift.getSalesTerminal());

    if (payed) {
      if (trip.getDriver1() != null) {
        EmployeePeriodicPayment advancePayment1 =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
        EmployeePeriodicPayment insurancePayment1 =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
        if (advancePayment1 != null) {
          advancePayment1.setDiscountedDate(new Date());
          advancePayment1.setPayedTrip(trip);
          trip.getJoinedPayment().setDriver1Nominal(advancePayment1.getPayedAmount());
          trip.getJoinedPayment().setDriver1NominalWeek(advancePayment1.getDiscountWeek());
          employeePeriodicPaymentRepository.save(advancePayment1);
        }
        if (insurancePayment1 != null) {
          insurancePayment1.setDiscountedDate(new Date());
          insurancePayment1.setPayedTrip(trip);
          trip.getJoinedPayment().setDriver1Insurance(insurancePayment1.getPayedAmount());
          trip.getJoinedPayment().setDriver1InsuranceWeek(insurancePayment1.getDiscountWeek());
          employeePeriodicPaymentRepository.save(insurancePayment1);
        }
      }

      if (trip.getDriver2() != null) {
        EmployeePeriodicPayment advancePayment2 =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
        EmployeePeriodicPayment insurancePayment2 =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
        if (advancePayment2 != null) {
          trip.getJoinedPayment().setDriver2Nominal(advancePayment2.getPayedAmount());
          trip.getJoinedPayment().setDriver2NominalWeek(advancePayment2.getDiscountWeek());
          advancePayment2.setDiscountedDate(new Date());
          advancePayment2.setPayedTrip(trip);
          employeePeriodicPaymentRepository.save(advancePayment2);
        }
        if (insurancePayment2 != null) {
          trip.getJoinedPayment().setDriver2Insurance(insurancePayment2.getPayedAmount());
          trip.getJoinedPayment().setDriver2InsuranceWeek(insurancePayment2.getDiscountWeek());
          insurancePayment2.setDiscountedDate(new Date());
          insurancePayment2.setPayedTrip(trip);
          employeePeriodicPaymentRepository.save(insurancePayment2);
        }
      }
    }

    List<TripExpense> oldExpenses =
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::getExpenses)
            .orElse(new ArrayList<>());
    trip.getJoinedPayment().setExpenses(new ArrayList<>());
    tripExpenseRepository.delete(oldExpenses);
    tripRepository.save(trip);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> rawExpenses = (List<Map<String, Object>>) data.get("expenses");

    List<TripExpense> expenses =
        rawExpenses.stream()
            .map(
                raw -> {
                  justcloud.tickets.domain.tickets.TripExpense expense =
                      new justcloud.tickets.domain.tickets.TripExpense();
                  if (raw.get("amount") instanceof String) {
                    expense.setAmount(new BigDecimal((String) raw.get("amount")));
                  } else if (raw.get("amount") instanceof Double) {
                    expense.setAmount(new BigDecimal((Double) raw.get("amount")));
                  } else if (raw.get("amount") instanceof Integer) {
                    expense.setAmount(new BigDecimal((Integer) raw.get("amount")));
                  } else if (raw.get("amount") instanceof Float) {
                    expense.setAmount(new BigDecimal((Float) raw.get("amount")));
                  } else {
                    expense.setAmount(BigDecimal.ZERO);
                  }
                  expense.setComments((String) raw.get("comments"));
                  expense.setType((String) raw.get("type"));
                  if ((boolean) raw.get("receipt")) {
                    expense.setExpenseType(
                        justcloud.tickets.domain.tickets.TripExpense.ExpenseType.WITH_RECEIPT);
                  } else {
                    expense.setExpenseType(
                        justcloud.tickets.domain.tickets.TripExpense.ExpenseType.NO_RECEIPT);
                  }
                  return expense;
                })
            .collect(Collectors.toList());

    @SuppressWarnings("unchecked")
    Map<String, Object> checklist = (Map<String, Object>) data.get("checklist");

    if (checklist != null) {
      trip.setHasAllPlaces((boolean) checklist.get("hasAllPlaces"));
      trip.setHasAllStamps((boolean) checklist.get("hasAllStamps"));
      trip.setPackageArrived((boolean) checklist.get("packageArrived"));
    }

    if (payed) {
      if (trip.getDriver1() != null) {
        List<EmployeeLoan> loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver1().getId(), false);
        loans.stream()
            .forEach(
                loan -> {
                  BigDecimal previousAmount = loan.getMissingAmount();
                  BigDecimal discount = BigDecimal.ZERO;
                  if (loan.getType() == EmployeeLoan.PaymentType.NUMBER) {
                    discount = loan.getNumber();
                  } else {
                    discount =
                        loan.getLoanAmount()
                            .multiply(loan.getNumber())
                            .divide(BigDecimal.valueOf(100));
                  }
                  if (discount.compareTo(loan.getMissingAmount()) >= 0) {
                    discount = loan.getMissingAmount();
                    loan.setMissingAmount(BigDecimal.ZERO);
                    loan.setPayed(true);
                    loan.setPayedTrip(trip);
                  } else {
                    loan.setPayed(false);
                    loan.setMissingAmount(loan.getMissingAmount().subtract(discount));
                  }
                  EmployeeLoanPayment loanPayment = new EmployeeLoanPayment();
                  loanPayment.setOriginalAmount(loan.getLoanAmount());
                  loanPayment.setPreviousAmount(previousAmount);
                  loanPayment.setTrip(trip);
                  loanPayment.setLoan(loan);
                  loanPayment.setAccount(loan.getAccount());
                  loanPayment.setAmountPayed(discount);
                  employeeLoanPaymentRepository.save(loanPayment);
                });
        employeeLoanRepository.save(loans);
      }

      if (trip.getDriver2() != null) {
        List<EmployeeLoan> loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver2().getId(), false);
        loans.stream()
            .forEach(
                loan -> {
                  BigDecimal previousAmount = loan.getMissingAmount();
                  BigDecimal discount = BigDecimal.ZERO;
                  if (loan.getType() == EmployeeLoan.PaymentType.NUMBER) {
                    discount = loan.getNumber();
                  } else {
                    discount =
                        loan.getLoanAmount()
                            .multiply(loan.getNumber())
                            .divide(BigDecimal.valueOf(100));
                  }
                  if (discount.compareTo(loan.getMissingAmount()) >= 0) {
                    discount = loan.getMissingAmount();
                    loan.setMissingAmount(BigDecimal.ZERO);
                    loan.setPayed(true);
                    loan.setPayedTrip(trip);
                  } else {
                    loan.setPayed(false);
                    loan.setMissingAmount(loan.getMissingAmount().subtract(discount));
                  }
                  if (discount.equals(BigDecimal.ZERO)) {
                    throw new RuntimeException("Cannot pay 0");
                  }
                  EmployeeLoanPayment loanPayment = new EmployeeLoanPayment();
                  loanPayment.setOriginalAmount(loan.getLoanAmount());
                  loanPayment.setPreviousAmount(previousAmount);
                  loanPayment.setTrip(trip);
                  loanPayment.setLoan(loan);
                  loanPayment.setAccount(loan.getAccount());
                  loanPayment.setAmountPayed(discount);
                  employeeLoanPaymentRepository.save(loanPayment);
                });
        employeeLoanRepository.save(loans);
      }
    }

    log.debug("Expense count {}", expenses.size());

    if (payed) {
      if (newDebt1.compareTo(BigDecimal.ZERO) > 0) {
        EmployeeAccount existingAccount =
            employeeAccountRepository.findByEmployeeId(trip.getDriver1().getId());
        if (existingAccount != null) {
          existingAccount.setCurrentBalance(existingAccount.getCurrentBalance().add(newDebt1));
          employeeAccountRepository.save(existingAccount);
        }
        EmployeeLoan loan1 = new EmployeeLoan();
        loan1.setConcept("Saldos negativos " + dateFormatter.print(new DateTime()));
        loan1.setAccount(
            Optional.ofNullable(existingAccount)
                .orElseGet(
                    () -> {
                      EmployeeAccount account = new EmployeeAccount();
                      account.setEmployee(trip.getDriver1());
                      account.setCurrentBalance(newDebt1);
                      account.setLoans(Arrays.asList(loan1));
                      account.setDiscounts(Collections.emptyList());
                      employeeAccountRepository.save(account);
                      return account;
                    }));
        loan1.setLoanAmount(newDebt1);
        loan1.setMissingAmount(newDebt1);
        loan1.setEmployee(trip.getDriver1());
        loan1.setNumber(newDebt1);
        loan1.setType(PaymentType.NUMBER);
        loan1.setPayed(false);
        loan1.setTrip(trip);
        employeeLoanRepository.save(loan1);
      }
      if (newDebt2.compareTo(BigDecimal.ZERO) > 0) {
        EmployeeAccount existingAccount =
            employeeAccountRepository.findByEmployeeId(trip.getDriver2().getId());
        if (existingAccount != null) {
          existingAccount.setCurrentBalance(existingAccount.getCurrentBalance().add(newDebt2));
          employeeAccountRepository.save(existingAccount);
        }
        final EmployeeLoan loan2 = new EmployeeLoan();
        loan2.setConcept("Saldos negativos " + dateFormatter.print(new DateTime()));
        loan2.setAccount(
            Optional.ofNullable(
                    employeeAccountRepository.findByEmployeeId(trip.getDriver2().getId()))
                .orElseGet(
                    () -> {
                      EmployeeAccount account = new EmployeeAccount();
                      account.setEmployee(trip.getDriver2());
                      account.setCurrentBalance(newDebt2);
                      account.setLoans(Arrays.asList(loan2));
                      account.setDiscounts(Collections.emptyList());
                      employeeAccountRepository.save(account);
                      return account;
                    }));
        loan2.setLoanAmount(newDebt2);
        loan2.setMissingAmount(newDebt2);
        loan2.setEmployee(trip.getDriver2());
        loan2.setNumber(newDebt2);
        loan2.setType(PaymentType.NUMBER);
        loan2.setPayed(false);
        loan2.setTrip(trip);
        employeeLoanRepository.save(loan2);
      }
    }

    tripExpenseRepository.save(expenses);
    trip.getJoinedPayment().setExpenses(expenses);

    joinedPaymentRepository.save(trip.getJoinedPayment());
    tripRepository.save(trip);

    /*
    List<EmployeeLoan> newLoans = expenses.stream()
      .filter(expense -> Arrays.asList("Daño a unidad", "Pasajero olvidado", "Maleta Extraviada").contains(expense.getType()))
      .map(expense -> {
          EmployeeLoan loan = new EmployeeLoan();
          EmployeeAccount account = employeeAccountRepository.findByEmployeeId(trip.getDriver1().getId());

          if(account == null) {
            account = new EmployeeAccount();
            account.setEmployee(trip.getDriver1());
            account.setCurrentBalance(BigDecimal.ZERO);
            account.setLoans(new ArrayList<>());
          }

          loan.setConcept(expense.getType());
          loan.setLoanAmount(expense.getAmount());
          loan.setEmployee(trip.getDriver1());
          loan.setType(PaymentType.PERCENTAGE);
          loan.setNumber(BigDecimal.valueOf(10l));
          loan.setMissingAmount(expense.getAmount());
          loan.setPayed(false);
          loan.setAccount(account);
          loan.setTrip(trip);

          account.setCurrentBalance(account.getCurrentBalance().add(expense.getAmount()));
          account.getLoans().add(loan);
          employeeAccountRepository.save(account);

          return loan;
      })
      .collect(Collectors.toList());

    employeeLoanRepository.save(newLoans);
    */

    return data;
  }

  public Map<String, Object> generateGuide(Map<String, Object> data) {
    Trip trip = tripRepository.findOne((String) data.get("tripId"));
    trip.setPackages((String) data.get("description"));
    trip.setGuideGenerated(true);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> rawAdvances = (List<Map<String, Object>>) data.get("advances");

    DateTime movingDate = new DateTime(trip.getDepartureDate());
    Map<String, DateTime> wrapper = new HashMap<>();
    wrapper.put("movingDate", movingDate);

    Collection<Map<String, ?>> stops =
        trip.getRun().getStops().stream()
            .sorted(
                (stop1, stop2) -> {
                  if (trip.getReverse()) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .filter(
                stop -> {
                  return !stop.getCheckpoint();
                })
            .map(
                stop -> {
                  Map<String, Object> objectData = new HashMap<>();
                  DateTime mv = wrapper.get("movingDate");
                  mv = mv.plusMinutes(stop.getWaitingMinutes().intValue());
                  /*
                  ByteArrayOutputStream out = new ByteArrayOutputStream();
                  try {
                    BitMatrix bitMatrix = new Code128Writer().encode(stop.getId() + "@" + trip.getId(), BarcodeFormat.CODE_128, width, height);
                    MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                  } catch (Exception e) {
                    log.error("Cannot generate file", e);
                  }
                  */
                  TripStopControl control =
                      tripStopControlRepository.findByTripAndStopOff(trip, stop);
                  if (control == null) {
                    control = new TripStopControl();
                    control.setStopOff(stop);
                    control.setTrip(trip);
                    control.setVisited(false);
                    tripStopControlRepository.save(control);
                  }
                  ByteArrayOutputStream qrBytes =
                      QRCode.from(control.getId()).to(ImageType.PNG).stream();
                  ByteArrayInputStream qrStream = new ByteArrayInputStream(qrBytes.toByteArray());
                  // ByteArrayInputStream qrStream = new ByteArrayInputStream(out.toByteArray());
                  objectData.put("oficina", stop.getName());
                  objectData.put("qr", qrStream);
                  mv = mv.plusMinutes(stop.getTravelMinutes().intValue());
                  wrapper.put("movingDate", mv);
                  return objectData;
                })
            .collect(Collectors.toList());

    log.debug("Trip has {} stops", stops.size());

    List<TripAdvance> advances =
        rawAdvances.stream()
            .map(
                raw -> {
                  TripAdvance advance = new TripAdvance();
                  Object amountObject = raw.get("amount");

                  if (amountObject instanceof String) {
                    advance.setAmount(new BigDecimal((String) amountObject));
                  } else {
                    advance.setAmount(new BigDecimal((Integer) amountObject));
                  }

                  advance.setComments((String) raw.get("description"));
                  return advance;
                })
            .collect(Collectors.toList());

    if (trip.getJoinedPayment() != null) {
      trip.setJoinedPayment(new JoinedPayment());
      trip.getJoinedPayment().setTrips(new ArrayList<>(Arrays.asList(trip)));
      joinedPaymentRepository.save(trip.getJoinedPayment());
    }

    tripAdvanceRepository.save(advances);
    trip.getJoinedPayment().setAdvances(advances);
    tripRepository.save(trip);

    return data;
  }

  private FullGroupData convertGroup(Trip trip) {
    FullGroupData fullTripData = new FullGroupData();

    List<TripStopControl> stopControls =
        Optional.ofNullable(trip.getJoinedPayment()).map(JoinedPayment::getTrips)
            .orElse(new ArrayList<>(Arrays.asList(trip))).stream()
            .map(tripStopControlRepository::findAllByTrip)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    List<BaggageData> baggageData =
        Optional.ofNullable(trip.getJoinedPayment()).map(JoinedPayment::getTrips)
            .orElse(new ArrayList<>(Arrays.asList(trip))).stream()
            .map(packageTicketRepository::findAllByTrip)
            .flatMap(List::stream)
            .map(p -> mapper.map(p, BaggageData.class))
            .collect(Collectors.toList());

    if (trip.getJoinedPayment() == null) {
      trip.setJoinedPayment(new JoinedPayment());
      trip.getJoinedPayment().setTrips(new ArrayList<>(Arrays.asList(trip)));
      joinedPaymentRepository.save(trip.getJoinedPayment());
    }

    fullTripData.setTrips(
        Optional.ofNullable(trip.getJoinedPayment()).map(JoinedPayment::getTrips)
            .orElse(Arrays.asList(trip)).stream()
            .map(
                t -> {
                  SimpleTripData simpleTripData = new SimpleTripData();
                  simpleTripData.setTripId(t.getId());
                  simpleTripData.setDieselLiters(t.getDieselLiters());
                  simpleTripData.setStartingStop(
                      mapper.map(t.getRun().getBeginning(), StopOffData.class));
                  simpleTripData.setEndingStop(
                      mapper.map(t.getRun().getDestination(), StopOffData.class));
                  return simpleTripData;
                })
            .collect(Collectors.toList()));

    BigDecimal adv =
        Optional.ofNullable(trip.getJoinedPayment().getTrips()).orElse(new ArrayList<>()).stream()
            .map(Trip::getRun)
            .map(Run::getRoute)
            .map(Route::getAdvance)
            .filter(a -> a != null)
            .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));

    fullTripData.setAdvance(adv);
    fullTripData.setBaggageData(baggageData);
    fullTripData.setId(trip.getId());
    fullTripData.setDepartureDate(trip.getDepartureDate());
    fullTripData.setEstimatedArrivalDate(trip.getEstimatedArrival());
    fullTripData.setPayed(
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::isPayed)
            .orElse(false));
    fullTripData.setDieselCost(trip.getDieselCost());
    fullTripData.setHasAllPlaces(trip.getHasAllPlaces());
    fullTripData.setHasAllStamps(trip.getHasAllStamps());
    fullTripData.setPackages(trip.getPackages());

    fullTripData.setExpenses(
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::getExpenses)
            .map(
                tripExpenses ->
                    tripExpenses.stream()
                        .map(
                            expense -> mapper.map(expense, justcloud.tickets.dto.TripExpense.class))
                        .collect(Collectors.toList()))
            .orElse(new ArrayList<>()));

    if (stopControls != null) {
      List<TripStopData> stopDatas =
          stopControls.stream()
              .sorted(
                  (stop1, stop2) -> {
                    if (trip.getReverse()) {
                      return stop2
                          .getStopOff()
                          .getOrderIndex()
                          .compareTo(stop1.getStopOff().getOrderIndex());
                    } else {
                      return stop1
                          .getStopOff()
                          .getOrderIndex()
                          .compareTo(stop2.getStopOff().getOrderIndex());
                    }
                  })
              .filter(
                  stop -> {
                    return !stop.getStopOff().getCheckpoint()
                        && !stop.getStopOff().getNotNecessary();
                  })
              .map(
                  (stopControl) -> {
                    TripStopData stopData = new TripStopData();

                    stopData.setId(stopControl.getId());
                    stopData.setBeginning(
                        mapper.map(trip.getRun().getBeginning(), StopOffData.class));
                    stopData.setEnding(
                        mapper.map(trip.getRun().getDestination(), StopOffData.class));
                    stopData.setStop(mapper.map(stopControl.getStopOff(), StopOffData.class));
                    stopData.setTrip(mapper.map(trip, TripData.class));
                    stopData.setRunName(trip.getRun().getName());
                    stopData.setDepartureDate(trip.getDepartureDate());
                    stopData.setVisited(stopControl.getVisited());

                    return stopData;
                  })
              .collect(Collectors.toList());

      fullTripData.setStopControlData(stopDatas);
    }

    fullTripData.setExpenses(
        Optional.ofNullable(trip.getJoinedPayment())
            .map(JoinedPayment::getExpenses)
            .map(List::stream)
            .orElse(Stream.empty())
            .map(expense -> mapper.map(expense, justcloud.tickets.dto.TripExpense.class))
            .collect(Collectors.toList()));

    List<TripAdvance> advances =
        Optional.ofNullable(trip.getJoinedPayment())
            .map(JoinedPayment::getAdvances)
            .orElse(new ArrayList<>());

    if (trip.getJoinedPayment() == null) {
      trip.setJoinedPayment(new JoinedPayment());
      trip.getJoinedPayment().setTrips(new ArrayList<>(Arrays.asList(trip)));
    }

    if (advances.size() == 0) {
      Optional.ofNullable(trip.getJoinedPayment())
          .map(JoinedPayment::getTrips)
          .orElse(Collections.emptyList())
          .forEach(
              t -> {
                BigDecimal ta =
                    Optional.ofNullable(t.getRun().getRoute().getAdvance()).orElse(BigDecimal.ZERO);
                if (ta.compareTo(BigDecimal.ZERO) > 0) {
                  TripAdvance tripAdvance = new TripAdvance();
                  tripAdvance.setAmount(ta);
                  tripAdvance.setComments("Anticipo para " + t.getRun().getName());
                  tripAdvanceRepository.save(tripAdvance);
                  advances.add(tripAdvance);
                }
              });
    }

    trip.getJoinedPayment().setAdvances(advances);
    joinedPaymentRepository.save(trip.getJoinedPayment());
    tripRepository.save(trip);

    fullTripData.setAdvances(
        advances.stream()
            .map(
                advance -> {
                  TripAdvanceData a = new TripAdvanceData();
                  a.setAmount(advance.getAmount());
                  a.setComments(advance.getComments());
                  return a;
                })
            .collect(Collectors.toList()));

    List<RouteSegment.StopData> tripStops =
        trip.getRun().getStops().stream()
            .sorted(
                (stop1, stop2) -> {
                  if (trip.getReverse()) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .filter(
                stop -> {
                  return !stop.getCheckpoint() && !stop.getNotNecessary();
                })
            .map(
                (stop) -> {
                  return mapper.map(stop, RouteSegment.StopData.class);
                })
            .collect(Collectors.toList());

    List<TicketData> tickets =
        trip.getJoinedPayment().getTrips().stream()
            .flatMap(t -> tripSeatRepository.findAllByTrip(t).stream())
            .map(
                seat -> {
                  TicketData ticket = mapper.map(seat, TicketData.class);
                  if (seat.getPayedPrice() == null) {
                    SegmentVar paymentPrice =
                        segmentVarRepository
                            .findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
                                seat.getStartingStop().getId(),
                                seat.getEndingStop().getId(),
                                "payment",
                                trip.getServiceLevelType());
                    if (paymentPrice != null) {
                      seat.setPayedPrice(paymentPrice.getNumericVar());
                      ticket.setPayment(paymentPrice.getNumericVar());
                      tripSeatRepository.save(seat);
                    }
                  } else {
                    ticket.setPayment(seat.getPayedPrice());
                  }
                  return ticket;
                })
            .collect(Collectors.toList());

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

    Map<String, List<TripSeat>> tripSeats =
        tripSeatRepository.findAllByTrip(trip).stream()
            .collect(Collectors.groupingBy(TripSeat::getSeatName));
    BusPositionData[][][] positions = new BusPositionData[maxFloor][maxRow + 1][maxColumn + 1];

    busPositions.forEach(
        (position) -> {
          int floor = position.getFloor() - 1;
          int row = position.getRow();
          int column = position.getColumn();

          positions[floor][row][column] = mapper.map(position, BusPositionData.class);

          if (tripSeats.containsKey(position.getName())) {
            // List<TripSeat> seats = tripSeats.get(position.getName());
            positions[floor][row][column].setReserved(true);
            positions[floor][row][column].setPartiallyReserved(true);
          }
        });

    TripData tripData = new TripData();

    tripData.setTickets(tickets);

    tripData.setReverse(trip.getReverse());
    tripData.setStops(tripStops);
    tripData.setPositions(positions);

    fullTripData.setRouteName(trip.getRun().getName());
    fullTripData.setTripData(tripData);

    EmployeeData driver1 = null;
    EmployeeData driver2 = null;

    if (trip.getDriver1() != null) {
      List<EmployeeLoan> loans;
      if (trip.getJoinedPayment().isPayed()) {
        EmployeeAccount account =
            employeeAccountRepository.findByEmployeeId(trip.getDriver1().getId());
        loans =
            trip.getJoinedPayment().getTrips().stream()
                .flatMap(
                    t -> employeeLoanPaymentRepository.findAllByAccountAndTrip(account, t).stream())
                .map(
                    loanPayment -> {
                      EmployeeLoan loan = new EmployeeLoan();
                      loan.setId(loanPayment.getLoan().getId());
                      loan.setPayed(false);
                      loan.setMissingAmount(loanPayment.getPreviousAmount());
                      loan.setAccount(account);
                      loan.setConcept(loanPayment.getLoan().getConcept());
                      loan.setEmployee(trip.getDriver1());
                      loan.setLoanAmount(loanPayment.getOriginalAmount());
                      loan.setTrip(trip);
                      loan.setType(loanPayment.getLoan().getType());
                      loan.setDateCreated(loanPayment.getLoan().getDateCreated());
                      loan.setLastUpdated(loanPayment.getLoan().getLastUpdated());
                      loan.setNumber(loanPayment.getLoan().getNumber());
                      loan.setObservations(loanPayment.getLoan().getObservations());
                      return loan;
                    })
                .collect(Collectors.toList());
      } else {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver1().getId(), false);
      }
      driver1 = new EmployeeData();
      driver1.setId(trip.getDriver1().getId());
      driver1.setName(trip.getDriver1().getName());
      driver1.setLastName(trip.getDriver1().getLastName());
      driver1.setSecondLastName(trip.getDriver1().getSecondLastName());
      driver1.setLoans(
          loans.stream()
              .map(
                  loan -> {
                    EmployeeLoanData data = mapper.map(loan, EmployeeLoanData.class);
                    List<EmployeeLoanPayment> payments =
                        employeeLoanPaymentRepository.findAllByLoanId(loan.getId());
                    data.setPayments(
                        payments.stream()
                            .map(payment -> mapper.map(payment, EmployeePaymentData.class))
                            .collect(Collectors.toList()));
                    return data;
                  })
              .collect(Collectors.toList()));

      EmployeePeriodicPayment advancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        advancePayment =
            trip.getJoinedPayment().getTrips().stream()
                .map(
                    t ->
                        employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                            trip.getDriver1(), t, "Anticipo"))
                .filter(p -> p != null)
                .findAny()
                .orElse(null);
      } else {
        advancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }
      EmployeePeriodicPayment insurancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        insurancePayment =
            trip.getJoinedPayment().getTrips().stream()
                .map(
                    t ->
                        employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                            trip.getDriver1(), t, "Seguro"))
                .filter(p -> p != null)
                .findAny()
                .orElse(null);
      } else {
        insurancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }

      if (advancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(advancePayment.getDiscountWeek());
        discount.setWeekAmount(advancePayment.getPayedAmount());
        fullTripData.setDriver1NominalDiscount(discount);
      }

      if (insurancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(insurancePayment.getDiscountWeek());
        discount.setWeekAmount(insurancePayment.getPayedAmount());
        fullTripData.setDriver1InsuranceDiscount(discount);
      }
    }

    if (trip.getDriver2() != null) {
      List<EmployeeLoan> loans;
      if (trip.getJoinedPayment().isPayed()) {
        EmployeeAccount account =
            employeeAccountRepository.findByEmployeeId(trip.getDriver2().getId());
        loans =
            trip.getJoinedPayment().getTrips().stream()
                .flatMap(
                    t -> employeeLoanPaymentRepository.findAllByAccountAndTrip(account, t).stream())
                .map(
                    loanPayment -> {
                      EmployeeLoan loan = new EmployeeLoan();
                      loan.setId(loanPayment.getLoan().getId());
                      loan.setPayed(false);
                      loan.setMissingAmount(loanPayment.getPreviousAmount());
                      loan.setAccount(account);
                      loan.setConcept(loanPayment.getLoan().getConcept());
                      loan.setEmployee(trip.getDriver2());
                      loan.setLoanAmount(loanPayment.getOriginalAmount());
                      loan.setTrip(trip);
                      loan.setType(loanPayment.getLoan().getType());
                      loan.setNumber(loanPayment.getLoan().getNumber());
                      loan.setObservations(loanPayment.getLoan().getObservations());
                      loan.setDateCreated(loanPayment.getLoan().getDateCreated());
                      loan.setLastUpdated(loanPayment.getLoan().getLastUpdated());
                      return loan;
                    })
                .collect(Collectors.toList());
      } else {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver2().getId(), false);
      }
      driver2 = new EmployeeData();
      driver2.setId(trip.getDriver2().getId());
      driver2.setId(trip.getDriver2().getId());
      driver2.setName(trip.getDriver2().getName());
      driver2.setLastName(trip.getDriver2().getLastName());
      driver2.setSecondLastName(trip.getDriver2().getSecondLastName());
      driver2.setLoans(
          loans.stream()
              .map(
                  loan -> {
                    EmployeeLoanData data = mapper.map(loan, EmployeeLoanData.class);
                    List<EmployeeLoanPayment> payments =
                        employeeLoanPaymentRepository.findAllByLoanId(loan.getId());
                    data.setPayments(
                        payments.stream()
                            .map(payment -> mapper.map(payment, EmployeePaymentData.class))
                            .collect(Collectors.toList()));
                    return data;
                  })
              .collect(Collectors.toList()));

      EmployeePeriodicPayment advancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        advancePayment =
            trip.getJoinedPayment().getTrips().stream()
                .map(
                    t ->
                        employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                            trip.getDriver2(), t, "Anticipo"))
                .filter(p -> p != null)
                .findAny()
                .orElse(null);
      } else {
        advancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }
      EmployeePeriodicPayment insurancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        insurancePayment =
            trip.getJoinedPayment().getTrips().stream()
                .map(
                    t ->
                        employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                            trip.getDriver2(), t, "Seguro"))
                .filter(p -> p != null)
                .findAny()
                .orElse(null);
      } else {
        insurancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }

      if (advancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(advancePayment.getDiscountWeek());
        discount.setWeekAmount(advancePayment.getPayedAmount());
        fullTripData.setDriver2NominalDiscount(discount);
      }

      if (insurancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(insurancePayment.getDiscountWeek());
        discount.setWeekAmount(insurancePayment.getPayedAmount());
        fullTripData.setDriver2InsuranceDiscount(discount);
      }
    }

    if (trip.getBus() != null) {
      fullTripData.setBusName(trip.getBus().getBusNumber());
    }

    fullTripData.setDriver1(driver1);
    fullTripData.setDriver2(driver2);
    return fullTripData;
  }

  private FullTripData convertTrip(Trip trip) {
    FullTripData fullTripData = new FullTripData();

    List<TripStopControl> stopControls = tripStopControlRepository.findAllByTrip(trip);
    List<BaggageData> baggageData =
        packageTicketRepository.findAllByTrip(trip).stream()
            .map(p -> mapper.map(p, BaggageData.class))
            .collect(Collectors.toList());

    if (trip.getJoinedPayment() == null) {
      trip.setJoinedPayment(new JoinedPayment());
      trip.getJoinedPayment().setTrips(new ArrayList<>(Arrays.asList(trip)));
      joinedPaymentRepository.save(trip.getJoinedPayment());
      tripRepository.save(trip);
    }

    fullTripData.setAdvance(
        Optional.ofNullable(trip.getRun().getRoute().getAdvance()).orElse(BigDecimal.ZERO));
    fullTripData.setBaggageData(baggageData);
    fullTripData.setId(trip.getId());
    fullTripData.setDepartureDate(trip.getDepartureDate());
    fullTripData.setEstimatedArrivalDate(trip.getEstimatedArrival());
    fullTripData.setPayed(
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::isPayed)
            .orElse(false));
    fullTripData.setDieselLiters(trip.getDieselLiters());
    fullTripData.setDieselCost(trip.getDieselCost());
    fullTripData.setHasAllPlaces(trip.getHasAllPlaces());
    fullTripData.setHasAllStamps(trip.getHasAllStamps());
    fullTripData.setPackages(trip.getPackages());

    fullTripData.setExpenses(
        Optional.ofNullable(trip)
            .map(Trip::getJoinedPayment)
            .map(JoinedPayment::getExpenses)
            .map(
                tripExpenses ->
                    tripExpenses.stream()
                        .map(
                            expense -> mapper.map(expense, justcloud.tickets.dto.TripExpense.class))
                        .collect(Collectors.toList()))
            .orElse(new ArrayList<>()));

    if (stopControls != null) {
      List<TripStopData> stopDatas =
          stopControls.stream()
              .sorted(
                  (stop1, stop2) -> {
                    if (trip.getReverse()) {
                      return stop2
                          .getStopOff()
                          .getOrderIndex()
                          .compareTo(stop1.getStopOff().getOrderIndex());
                    } else {
                      return stop1
                          .getStopOff()
                          .getOrderIndex()
                          .compareTo(stop2.getStopOff().getOrderIndex());
                    }
                  })
              .filter(
                  stop -> {
                    return !stop.getStopOff().getCheckpoint()
                        && !stop.getStopOff().getNotNecessary();
                  })
              .map(
                  (stopControl) -> {
                    TripStopData stopData = new TripStopData();

                    stopData.setId(stopControl.getId());
                    stopData.setBeginning(
                        mapper.map(trip.getRun().getBeginning(), StopOffData.class));
                    stopData.setEnding(
                        mapper.map(trip.getRun().getDestination(), StopOffData.class));
                    stopData.setStop(mapper.map(stopControl.getStopOff(), StopOffData.class));
                    stopData.setTrip(mapper.map(trip, TripData.class));
                    stopData.setRunName(trip.getRun().getName());
                    stopData.setDepartureDate(trip.getDepartureDate());
                    stopData.setVisited(stopControl.getVisited());

                    return stopData;
                  })
              .collect(Collectors.toList());

      fullTripData.setStopControlData(stopDatas);
    }

    fullTripData.setExpenses(
        Optional.ofNullable(trip.getJoinedPayment())
            .map(JoinedPayment::getExpenses)
            .map(List::stream)
            .orElse(Stream.empty())
            .map(expense -> mapper.map(expense, justcloud.tickets.dto.TripExpense.class))
            .collect(Collectors.toList()));

    fullTripData.setAdvances(
        Optional.ofNullable(trip.getJoinedPayment())
            .map(JoinedPayment::getAdvances)
            .map(List::stream)
            .orElse(Stream.empty())
            .map(
                advance -> {
                  TripAdvanceData a = new TripAdvanceData();
                  a.setAmount(advance.getAmount());
                  a.setComments(advance.getComments());
                  return a;
                })
            .collect(Collectors.toList()));

    List<RouteSegment.StopData> tripStops =
        trip.getRun().getStops().stream()
            .sorted(
                (stop1, stop2) -> {
                  if (trip.getReverse()) {
                    return stop2.getOrderIndex().compareTo(stop1.getOrderIndex());
                  } else {
                    return stop1.getOrderIndex().compareTo(stop2.getOrderIndex());
                  }
                })
            .filter(
                stop -> {
                  return !stop.getCheckpoint() && !stop.getNotNecessary();
                })
            .map(
                (stop) -> {
                  return mapper.map(stop, RouteSegment.StopData.class);
                })
            .collect(Collectors.toList());

    List<TicketData> tickets =
        tripSeatRepository.findAllByTrip(trip).stream()
            .map(
                seat -> {
                  TicketData ticket = mapper.map(seat, TicketData.class);
                  if (seat.getPayedPrice() == null) {
                    SegmentVar paymentPrice =
                        segmentVarRepository
                            .findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
                                seat.getStartingStop().getId(),
                                seat.getEndingStop().getId(),
                                "payment",
                                trip.getServiceLevelType());
                    if (paymentPrice != null) {
                      seat.setPayedPrice(paymentPrice.getNumericVar());
                      ticket.setPayment(paymentPrice.getNumericVar());
                      tripSeatRepository.save(seat);
                    }
                  } else {
                    ticket.setPayment(seat.getPayedPrice());
                  }
                  return ticket;
                })
            .collect(Collectors.toList());

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

    Map<String, List<TripSeat>> tripSeats =
        tripSeatRepository.findAllByTrip(trip).stream()
            .collect(Collectors.groupingBy(TripSeat::getSeatName));
    BusPositionData[][][] positions = new BusPositionData[maxFloor][maxRow + 1][maxColumn + 1];

    busPositions.forEach(
        (position) -> {
          int floor = position.getFloor() - 1;
          int row = position.getRow();
          int column = position.getColumn();

          positions[floor][row][column] = mapper.map(position, BusPositionData.class);

          if (tripSeats.containsKey(position.getName())) {
            // List<TripSeat> seats = tripSeats.get(position.getName());
            positions[floor][row][column].setReserved(true);
            positions[floor][row][column].setPartiallyReserved(true);
          }
        });

    TripData tripData = new TripData();

    tripData.setTickets(tickets);

    tripData.setReverse(trip.getReverse());
    tripData.setStops(tripStops);
    tripData.setPositions(positions);

    fullTripData.setRouteName(trip.getRun().getName());
    fullTripData.setTripData(tripData);

    EmployeeData driver1 = null;
    EmployeeData driver2 = null;

    if (trip.getDriver1() != null) {
      List<EmployeeLoan> loans;
      if (trip.getJoinedPayment().isPayed()) {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayedTrip(trip.getDriver1().getId(), trip);
      } else {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver1().getId(), false);
      }
      driver1 = new EmployeeData();
      driver1.setId(trip.getDriver1().getId());
      driver1.setName(trip.getDriver1().getName());
      driver1.setLastName(trip.getDriver1().getLastName());
      driver1.setSecondLastName(trip.getDriver1().getSecondLastName());
      driver1.setLoans(
          loans.stream()
              .map(
                  loan -> {
                    EmployeeLoanData data = mapper.map(loan, EmployeeLoanData.class);
                    List<EmployeeLoanPayment> payments =
                        employeeLoanPaymentRepository.findAllByLoan(loan);
                    data.setPayments(
                        payments.stream()
                            .map(payment -> mapper.map(payment, EmployeePaymentData.class))
                            .collect(Collectors.toList()));
                    return data;
                  })
              .collect(Collectors.toList()));

      EmployeePeriodicPayment advancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        advancePayment =
            employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                trip.getDriver1(), trip, "Anticipo");
      } else {
        advancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }
      EmployeePeriodicPayment insurancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        insurancePayment =
            employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                trip.getDriver1(), trip, "Seguro");
      } else {
        insurancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver1(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }

      if (advancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(advancePayment.getDiscountWeek());
        discount.setWeekAmount(advancePayment.getPayedAmount());
        fullTripData.setDriver1NominalDiscount(discount);
      }

      if (insurancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(insurancePayment.getDiscountWeek());
        discount.setWeekAmount(insurancePayment.getPayedAmount());
        fullTripData.setDriver1InsuranceDiscount(discount);
      }
    }

    if (trip.getDriver2() != null) {
      List<EmployeeLoan> loans;
      if (trip.getJoinedPayment().isPayed()) {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayedTrip(trip.getDriver2().getId(), trip);
      } else {
        loans =
            employeeLoanRepository.findAllByEmployeeIdAndPayed(trip.getDriver2().getId(), false);
      }
      driver2 = new EmployeeData();
      driver2.setId(trip.getDriver2().getId());
      driver2.setId(trip.getDriver2().getId());
      driver2.setName(trip.getDriver2().getName());
      driver2.setLastName(trip.getDriver2().getLastName());
      driver2.setSecondLastName(trip.getDriver2().getSecondLastName());
      driver2.setLoans(
          loans.stream()
              .map(
                  loan -> {
                    EmployeeLoanData data = mapper.map(loan, EmployeeLoanData.class);
                    List<EmployeeLoanPayment> payments =
                        employeeLoanPaymentRepository.findAllByLoan(loan);
                    data.setPayments(
                        payments.stream()
                            .map(payment -> mapper.map(payment, EmployeePaymentData.class))
                            .collect(Collectors.toList()));
                    return data;
                  })
              .collect(Collectors.toList()));

      EmployeePeriodicPayment advancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        advancePayment =
            employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                trip.getDriver2(), trip, "Anticipo");
      } else {
        advancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Anticipo")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }
      EmployeePeriodicPayment insurancePayment;
      if (trip.getJoinedPayment().isPayed()) {
        insurancePayment =
            employeePeriodicPaymentRepository.findByEmployeeAndPayedTripAndType(
                trip.getDriver2(), trip, "Seguro");
      } else {
        insurancePayment =
            employeePeriodicPaymentRepository
                .findAllByEmployeeAndTypeAndDiscountedDateIsNull(trip.getDriver2(), "Seguro")
                .stream()
                .min(
                    (payment1, payment2) -> {
                      return payment1.getDateCreated().compareTo(payment2.getDateCreated());
                    })
                .orElse(null);
      }

      if (advancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(advancePayment.getDiscountWeek());
        discount.setWeekAmount(advancePayment.getPayedAmount());
        fullTripData.setDriver2NominalDiscount(discount);
      }

      if (insurancePayment != null) {
        EmployeeWeekDiscount discount = new EmployeeWeekDiscount();
        discount.setWeekName(insurancePayment.getDiscountWeek());
        discount.setWeekAmount(insurancePayment.getPayedAmount());
        fullTripData.setDriver2InsuranceDiscount(discount);
      }
    }

    if (trip.getBus() != null) {
      fullTripData.setBusName(trip.getBus().getBusNumber());
    }

    fullTripData.setDriver1(driver1);
    fullTripData.setDriver2(driver2);
    return fullTripData;
  }

  public FullTripData findTripData(String id) {
    Trip trip = tripRepository.findOne(id);

    return convertTrip(trip);
  }

  public FullGroupData findGroupData(String id) {
    Trip trip = tripRepository.findOne(id);

    return convertGroup(trip);
  }

  public List<StartTripData> findStartingTrips(String stopOffName, Date tripDate) {
    DateTimeZone zone = DateTimeZone.forID("America/Mexico_City");

    Date startTime =
        new DateTime(new Date(tripDate.getTime()))
            .withZone(zone)
            .minusDays(5)
            .withTimeAtStartOfDay()
            .toDate();

    Date endTime =
        new DateTime(new Date(tripDate.getTime()))
            .withZone(zone)
            .withTimeAtStartOfDay()
            .plusDays(1)
            .toDate();

    return tripRepository
        .findAllByRunBeginningNameAndGuideGeneratedAndDepartureDateBetween(
            stopOffName, false, startTime, endTime)
        .stream()
        .map(
            trip -> {
              StartTripData tripData = new StartTripData();
              tripData.setId(trip.getId());
              tripData.setAdvance(
                  Optional.ofNullable(trip.getRun().getRoute().getAdvance())
                      .orElse(BigDecimal.ZERO));
              tripData.setDepartureDate(trip.getDepartureDate());
              tripData.setEstimatedArrivalDate(trip.getEstimatedArrival());
              tripData.setRouteName(trip.getRun().getName());

              if (trip.getBus() != null) {
                tripData.setBusName(trip.getBus().getBusNumber());
              }

              StopOffData startingStop = new StopOffData();
              StopOffData endingStop = new StopOffData();

              startingStop.setName(trip.getRun().getBeginning().getName());
              endingStop.setName(trip.getRun().getDestination().getName());

              tripData.setStartingStop(startingStop);
              tripData.setEndingStop(endingStop);

              DriverData driver1 = null;
              DriverData driver2 = null;

              if (trip.getDriver1() != null) {
                StringBuilder builder = new StringBuilder();

                if (trip.getDriver1().getName() != null) {
                  builder.append(trip.getDriver1().getName());
                }

                if (trip.getDriver1().getLastName() != null) {
                  builder.append(" ");
                  builder.append(trip.getDriver1().getLastName());
                }

                driver1 = new DriverData();
                driver1.setId(trip.getDriver1().getId());
                driver1.setName(builder.toString());
              }

              if (trip.getDriver2() != null) {
                StringBuilder builder = new StringBuilder();

                if (trip.getDriver2().getName() != null) {
                  builder.append(trip.getDriver2().getName());
                }

                if (trip.getDriver2().getLastName() != null) {
                  builder.append(" ");
                  builder.append(trip.getDriver2().getLastName());
                }
                driver2 = new DriverData();
                driver2.setId(trip.getDriver2().getId());
                driver2.setName(builder.toString());
              }

              tripData.setDriver1(driver1);
              tripData.setDriver2(driver2);

              return tripData;
            })
        .collect(Collectors.toList());
  }

  public List<StartTripData> findEndingTrips(String stopOffName, Date tripDate) {
    Date startTime = new Date(tripDate.getTime());
    Date endTime = new Date(tripDate.getTime());

    Calendar startCal = Calendar.getInstance();
    startCal.setTime(startTime);
    startCal.set(Calendar.HOUR_OF_DAY, 0);
    startCal.set(Calendar.MINUTE, 0);
    startCal.set(Calendar.SECOND, 0);

    startTime = startCal.getTime();

    Calendar endCal = Calendar.getInstance();
    endCal.setTime(endTime);
    endCal.set(Calendar.HOUR_OF_DAY, 23);
    endCal.set(Calendar.MINUTE, 59);
    endCal.set(Calendar.SECOND, 59);

    endTime = endCal.getTime();

    return tripRepository.findAllByRunDestinationNameAndGuideGenerated(stopOffName, true).stream()
        .filter(trip -> trip.getJoinedPayment() == null || !trip.getJoinedPayment().isPayed())
        .map(
            trip -> {
              StartTripData tripData = new StartTripData();
              tripData.setId(trip.getId());
              tripData.setAdvance(
                  Optional.ofNullable(trip.getRun().getRoute().getAdvance())
                      .orElse(BigDecimal.ZERO));
              tripData.setDepartureDate(trip.getDepartureDate());
              tripData.setEstimatedArrivalDate(trip.getEstimatedArrival());
              tripData.setRouteName(trip.getRun().getName());

              if (trip.getBus() != null) {
                tripData.setBusName(trip.getBus().getBusNumber());
              }

              StopOffData startingStop = new StopOffData();
              StopOffData endingStop = new StopOffData();

              startingStop.setName(trip.getRun().getBeginning().getName());
              endingStop.setName(trip.getRun().getDestination().getName());

              tripData.setStartingStop(startingStop);
              tripData.setEndingStop(endingStop);

              DriverData driver1 = null;
              DriverData driver2 = null;

              if (trip.getDriver1() != null) {
                StringBuilder builder = new StringBuilder();

                if (trip.getDriver1().getName() != null) {
                  builder.append(trip.getDriver1().getName());
                }

                if (trip.getDriver1().getLastName() != null) {
                  builder.append(" ");
                  builder.append(trip.getDriver1().getLastName());
                }

                driver1 = new DriverData();
                driver1.setId(trip.getDriver1().getId());
                driver1.setName(builder.toString());
              }

              if (trip.getDriver2() != null) {
                StringBuilder builder = new StringBuilder();

                if (trip.getDriver2().getName() != null) {
                  builder.append(trip.getDriver2().getName());
                }

                if (trip.getDriver2().getLastName() != null) {
                  builder.append(" ");
                  builder.append(trip.getDriver2().getLastName());
                }
                driver2 = new DriverData();
                driver2.setId(trip.getDriver2().getId());
                driver2.setName(builder.toString());
              }

              tripData.setDriver1(driver1);
              tripData.setDriver2(driver2);

              return tripData;
            })
        .collect(Collectors.toList());
  }

  public Trip createTrip(String runId, String serviceTypeId, Date tripDate, Boolean reverse) {
    Run run = runRepository.findOne(runId);

    ServiceTypeTime scheduleTime =
        run.getServiceTypeTimes().stream()
            .filter(
                (schedule) -> {
                  LocalDate scheduleDate = new LocalDate(tripDate);
                  DateTime utcDepartingTime =
                      new DateTime(schedule.getDepartureTime(), DateTimeZone.UTC);

                  LocalTime departingTime = utcDepartingTime.toLocalTime();
                  LocalDateTime scheduledDateTime = scheduleDate.toLocalDateTime(departingTime);
                  LocalDateTime askedDateTime = new LocalDateTime(tripDate);

                  return askedDateTime.equals(scheduledDateTime)
                      && schedule.getServiceLevelType().getId().equals(serviceTypeId);
                })
            .findFirst()
            .get();

    Long totalTime =
        run.getRoute().getStops().stream()
            .mapToLong(
                (stop) -> {
                  Long travelMinutes =
                      (stop.getTravelMinutes() != null) ? stop.getTravelMinutes() : 0l;
                  Long missingMinutes =
                      (stop.getMissingMinutes() != null) ? stop.getMissingMinutes() : 0l;
                  return travelMinutes + missingMinutes;
                })
            .sum();

    Date arriveDate = new Date(tripDate.getTime() + totalTime * 60 * 1000);

    Trip trip = new Trip();
    trip.setRun(run);
    trip.setDepartureDate(tripDate);
    trip.setEstimatedArrival(arriveDate);
    trip.setSoldTickets(0);
    trip.setReverse(reverse);
    trip.setServiceLevelType(scheduleTime.getServiceLevelType());
    trip.setTotalTravelMinutes(totalTime);
    trip.setGuideGenerated(false);
    trip.setHasAllStamps(false);
    trip.setHasAllPlaces(false);
    trip.setStatus(TripStatus.PROGRAMMED);

    trip = tripRepository.save(trip);

    final Trip tripReference = trip;
    /* List<TripStopControl> controls = run.getRoute().getStops().stream() */
    /*   .map(stop -> { */
    /*     TripStopControl control = new TripStopControl(); */
    /*     control.setStopOff(stop); */
    /*     control.setTrip(tripReference); */
    /*     control.setVisited(false); */
    /*     return control; */
    /*   }) */
    /*   .collect(Collectors.toList()); */
    /*  */
    /* tripStopControlRepository.save(controls); */

    return tripReference;
  }

  public List<FullTripData> findTrips(String beginningName, String driverName, Date date) {
    Date startTime = new Date(date.getTime());
    Date endTime = new Date(date.getTime());

    Calendar startCal = Calendar.getInstance();
    startCal.setTime(startTime);
    startCal.set(Calendar.HOUR_OF_DAY, 0);
    startCal.set(Calendar.MINUTE, 0);
    startCal.set(Calendar.SECOND, 0);

    startTime = startCal.getTime();

    Calendar endCal = Calendar.getInstance();
    endCal.setTime(endTime);
    endCal.set(Calendar.HOUR_OF_DAY, 23);
    endCal.set(Calendar.MINUTE, 59);
    endCal.set(Calendar.SECOND, 59);

    endTime = endCal.getTime();

    return tripRepository.findTrips(beginningName, driverName, startTime, endTime).stream()
        .map((trip) -> convertTrip(trip))
        .collect(Collectors.toList());
  }

  public List<TripBusData> findTripsByDay(String date, String timeZone) {
    List<Run> runs =
        StreamSupport.stream(runRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTime day = new DateTime(date);
    final DayOfWeek dayOfWeek;

    switch (day.getDayOfWeek()) {
      case DateTimeConstants.MONDAY:
        dayOfWeek = DayOfWeek.MONDAY;
        break;
      case DateTimeConstants.TUESDAY:
        dayOfWeek = DayOfWeek.TUESDAY;
        break;
      case DateTimeConstants.WEDNESDAY:
        dayOfWeek = DayOfWeek.WEDNESDAY;
        break;
      case DateTimeConstants.THURSDAY:
        dayOfWeek = DayOfWeek.THURSDAY;
        break;
      case DateTimeConstants.FRIDAY:
        dayOfWeek = DayOfWeek.FRIDAY;
        break;
      case DateTimeConstants.SATURDAY:
        dayOfWeek = DayOfWeek.SATURDAY;
        break;
      case DateTimeConstants.SUNDAY:
        dayOfWeek = DayOfWeek.SUNDAY;
        break;
      default:
        dayOfWeek = null;
        break;
    }

    Date startDate = new DateTime(day).withZoneRetainFields(zone).withTimeAtStartOfDay().toDate();
    Date endDate =
        new DateTime(day).withZoneRetainFields(zone).plusDays(1).withTimeAtStartOfDay().toDate();

    Map<String, List<Map<String, Object>>> programmedServices =
        runs.stream()
            .filter(
                run -> {
                  Set<DayOfWeek> days =
                      run.getServiceTypeTimes().stream()
                          .flatMap((service) -> service.getDays().stream())
                          .distinct()
                          .collect(Collectors.toSet());
                  return days.contains(dayOfWeek);
                })
            .flatMap(
                run -> {
                  return run.getServiceTypeTimes().stream()
                      .filter(
                          service -> {
                            return service.getDays().contains(dayOfWeek);
                          })
                      .map(
                          service -> {
                            Map<String, Object> runServiceMap = new HashMap<>();
                            runServiceMap.put("run", run);
                            runServiceMap.put("service", service);
                            return runServiceMap;
                          });
                })
            .collect(
                Collectors.groupingBy(
                    service -> {
                      StringBuilder builder = new StringBuilder();

                      Run run = (Run) service.get("run");
                      ServiceTypeTime s = (ServiceTypeTime) service.get("service");
                      LocalTime departureTime = new LocalTime(s.getDepartureTime());
                      builder.append(run.getId());
                      builder.append("-");
                      builder.append(s.getServiceLevelType().getId());
                      builder.append("-");
                      builder.append(
                          departureTime
                              .toDateTime(day)
                              .toLocalTime()
                              .withMillisOfSecond(0)
                              .toString());

                      return builder.toString();
                    }));

    List<Trip> existingTrips =
        tripRepository.findAllTripsByDepartureDateBetween(startDate, endDate);

    Map<String, List<Trip>> existingServicesMap =
        existingTrips.stream()
            .collect(
                Collectors.groupingBy(
                    trip -> {
                      LocalTime localTime = new DateTime(trip.getDepartureDate()).toLocalTime();
                      DateTime dateTime = new DateTime(trip.getDepartureDate());
                      if (trip.getRun().getName().equals("8070")) {
                        log.info("Hola {} {} {}", trip.getId(), dateTime, localTime);
                      }
                      return trip.getRun().getId()
                          + "-"
                          + trip.getServiceLevelType().getId()
                          + "-"
                          + localTime.withMillisOfSecond(0);
                    }));

    log.info("Found programmed keys are {}", programmedServices.keySet());
    log.info("Found existing keys are {}", existingServicesMap.keySet());

    return programmedServices.keySet().stream()
        .map(
            k -> {
              Map<String, Object> v = programmedServices.get(k).get(0);
              Run run = (Run) v.get("run");
              ServiceTypeTime service = (ServiceTypeTime) v.get("service");

              List<Trip> existingServices =
                  existingServicesMap.getOrDefault(k, Collections.emptyList());

              TripBusData data = new TripBusData();

              data.setRoute(mapper.map(run.getRoute(), RouteData.class));
              data.setServiceType(mapper.map(service.getServiceLevelType(), ServiceTypeData.class));
              data.setRunId(run.getId());
              data.setOriginId(run.getBeginning().getId());
              data.setOriginName(run.getBeginning().getName());
              data.setDestinationId(run.getDestination().getId());
              data.setDestinationName(run.getDestination().getName());
              data.setReverse(service.getReverse());

              if (existingServices.size() > 0) {
                Trip existingService = existingServices.get(0);

                data.setId(existingService.getId());
                data.setDepartureDate(existingService.getDepartureDate());

                if (existingService.getBus() != null) {
                  data.setBusId(existingService.getBus().getId());
                  data.setBusPlates(existingService.getBus().getPlates());
                  data.setBusNumber(existingService.getBus().getBusNumber());
                }

              } else {
                data.setDepartureDate(
                    new LocalTime(service.getDepartureTime())
                        .toDateTime(day)
                        .withZoneRetainFields(DateTimeZone.UTC)
                        .withZone(zone)
                        .toDate());
              }

              return data;
            })
        .collect(Collectors.toList());
  }

  private Map<String, List<TripSeat>> groupTripData(Trip trip, List<TripSeat> tickets) {
    Map<String, List<TripSeat>> ticketByStarting =
        tickets.stream().collect(Collectors.groupingBy(t -> t.getStartingStop().getId()));

    List<TripSeat> currentOccupation = new ArrayList<>();
    Map<String, List<TripSeat>> result = new HashMap<>();

    trip.getRun().getStops().stream()
        .sorted(
            (a, b) -> {
              if (trip.getReverse()) {
                return b.getOrderIndex().compareTo(a.getOrderIndex());
              } else {
                return a.getOrderIndex().compareTo(b.getOrderIndex());
              }
            })
        .forEach(
            stop -> {
              List<TripSeat> starting =
                  Optional.ofNullable(ticketByStarting.get(stop.getId()))
                      .orElse(Collections.emptyList());

              currentOccupation.removeIf(t -> t.getEndingStop().getId().equals(stop.getId()));

              currentOccupation.addAll(starting);

              result.put(stop.getId(), new ArrayList<TripSeat>(currentOccupation));
            });

    return result;
  }

  public List<TripTicketDetail> findTripDetail(String id) {
    Trip trip = tripRepository.findOne(id);
    List<TripSeat> tickets = tripSeatRepository.findAllByTrip(trip);

    Map<String, List<TripSeat>> ticketByStarting =
        tickets.stream().collect(Collectors.groupingBy(t -> t.getStartingStop().getId()));

    List<TripSeat> currentOccupation = new ArrayList<>();
    return trip.getRun().getStops().stream()
        .sorted(
            (a, b) -> {
              if (trip.getReverse()) {
                return b.getOrderIndex().compareTo(a.getOrderIndex());
              } else {
                return a.getOrderIndex().compareTo(b.getOrderIndex());
              }
            })
        .map(
            stop -> {
              List<TripSeat> starting =
                  Optional.ofNullable(ticketByStarting.get(stop.getId()))
                      .orElse(Collections.emptyList());

              int downboard = currentOccupation.size();
              currentOccupation.removeIf(t -> t.getEndingStop().getId().equals(stop.getId()));
              downboard = downboard - currentOccupation.size();

              int aboard = starting.size();
              currentOccupation.addAll(starting);

              TripTicketDetail detail = new TripTicketDetail();
              detail.setOnboarding(aboard);
              detail.setDownboarding(downboard);
              detail.setOccupation(currentOccupation.size());

              detail.setStopName(stop.getName());
              detail.setId(stop.getId());

              return detail;
            })
        .collect(Collectors.toList());
  }

  public List<TripLogisticData> findLogisticTripsByDay(String date, String timeZone) {
    List<Run> runs =
        StreamSupport.stream(runRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTime day = new DateTime(date);
    final DayOfWeek dayOfWeek;

    switch (day.getDayOfWeek()) {
      case DateTimeConstants.MONDAY:
        dayOfWeek = DayOfWeek.MONDAY;
        break;
      case DateTimeConstants.TUESDAY:
        dayOfWeek = DayOfWeek.TUESDAY;
        break;
      case DateTimeConstants.WEDNESDAY:
        dayOfWeek = DayOfWeek.WEDNESDAY;
        break;
      case DateTimeConstants.THURSDAY:
        dayOfWeek = DayOfWeek.THURSDAY;
        break;
      case DateTimeConstants.FRIDAY:
        dayOfWeek = DayOfWeek.FRIDAY;
        break;
      case DateTimeConstants.SATURDAY:
        dayOfWeek = DayOfWeek.SATURDAY;
        break;
      case DateTimeConstants.SUNDAY:
        dayOfWeek = DayOfWeek.SUNDAY;
        break;
      default:
        dayOfWeek = null;
        break;
    }

    Date startDate = new DateTime(day).withZoneRetainFields(zone).withTimeAtStartOfDay().toDate();
    Date endDate =
        new DateTime(day).withZoneRetainFields(zone).plusDays(1).withTimeAtStartOfDay().toDate();

    Map<String, List<Map<String, Object>>> programmedServices =
        runs.stream()
            .filter(
                run -> {
                  Set<DayOfWeek> days =
                      run.getServiceTypeTimes().stream()
                          .flatMap((service) -> service.getDays().stream())
                          .distinct()
                          .collect(Collectors.toSet());
                  return days.contains(dayOfWeek);
                })
            .flatMap(
                run -> {
                  return run.getServiceTypeTimes().stream()
                      .filter(
                          service -> {
                            return service.getDays().contains(dayOfWeek);
                          })
                      .map(
                          service -> {
                            Map<String, Object> runServiceMap = new HashMap<>();
                            runServiceMap.put("run", run);
                            runServiceMap.put("service", service);
                            return runServiceMap;
                          });
                })
            .collect(
                Collectors.groupingBy(
                    service -> {
                      StringBuilder builder = new StringBuilder();

                      Run run = (Run) service.get("run");
                      ServiceTypeTime s = (ServiceTypeTime) service.get("service");
                      LocalTime departureTime = new LocalTime(s.getDepartureTime());
                      builder.append(run.getId());
                      builder.append("-");
                      builder.append(
                          departureTime
                              .toDateTime(day)
                              .withZone(DateTimeZone.UTC)
                              .toLocalTime()
                              .withMillisOfSecond(0)
                              .toString());

                      return builder.toString();
                    }));

    List<Trip> existingTrips =
        tripRepository.findAllTripsByDepartureDateBetween(startDate, endDate);

    Map<String, List<Trip>> existingServicesMap =
        existingTrips.stream()
            .collect(
                Collectors.groupingBy(
                    trip -> {
                      LocalTime localTime =
                          new DateTime(trip.getDepartureDate())
                              .withZone(DateTimeZone.getDefault())
                              .toLocalTime();
                      return trip.getRun().getId() + "-" + localTime.withMillisOfSecond(0);
                    }));

    log.debug("Found keys are {}", existingServicesMap.keySet());

    return programmedServices.keySet().stream()
        .map(
            k -> {
              Map<String, Object> v = programmedServices.get(k).get(0);
              Run run = (Run) v.get("run");
              ServiceTypeTime service = (ServiceTypeTime) v.get("service");

              List<Trip> existingServices =
                  existingServicesMap.getOrDefault(k, Collections.emptyList());

              TripLogisticData data = new TripLogisticData();

              data.setRoute(mapper.map(run.getRoute(), RouteData.class));
              data.setRunId(run.getId());
              data.setOriginId(run.getBeginning().getId());
              data.setOriginName(run.getBeginning().getName());
              data.setDestinationId(run.getDestination().getId());
              data.setDestinationName(run.getDestination().getName());
              data.setReverse(service.getReverse());

              if (existingServices.size() > 0) {
                Trip existingService = existingServices.get(0);
                List<TripSeat> tickets = tripSeatRepository.findAllByTrip(existingService);

                Map<String, List<TripSeat>> groupedTickets =
                    groupTripData(existingService, tickets);

                Integer maxSize =
                    groupedTickets.values().stream()
                        .max(
                            (a, b) ->
                                Integer.valueOf(a.size()).compareTo(Integer.valueOf(b.size())))
                        .map(l -> l.size())
                        .orElse(0);

                Integer minSize =
                    groupedTickets.values().stream()
                        .min(
                            (a, b) ->
                                Integer.valueOf(a.size()).compareTo(Integer.valueOf(b.size())))
                        .map(l -> l.size())
                        .orElse(0);

                data.setId(existingService.getId());
                data.setDepartureDate(existingService.getDepartureDate());
                data.setTotalSeats(groupedTickets.size());
                data.setTotalPassengers(tickets.size());

                data.setLargestOccupation(maxSize.intValue());
                data.setShortestOccupation(minSize.intValue());
                data.setAverage(
                    groupedTickets.values().stream()
                        .mapToInt(vals -> vals.size())
                        .average()
                        .orElse(0));

                if (existingService.getBus() != null) {
                  data.setBusId(existingService.getBus().getId());
                  data.setBusPlates(existingService.getBus().getPlates());
                  data.setBusNumber(existingService.getBus().getBusNumber());
                }

              } else {

                data.setAverage(0d);
                data.setShortestOccupation(0);
                data.setLargestOccupation(0);
                data.setDepartureDate(
                    new LocalTime(service.getDepartureTime())
                        .toDateTime(day)
                        .withZoneRetainFields(DateTimeZone.UTC)
                        .withZone(DateTimeZone.getDefault())
                        .toDate());
              }

              return data;
            })
        .sorted((a, b) -> b.getAverage().compareTo(a.getAverage()))
        .collect(Collectors.toList());
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

  public void joinPayments(Map<String, Object> data) {
    List<Map<String, Object>> trips = (List<Map<String, Object>>) data.get("selectedTrips");
    JoinedPayment joinedPayment = new JoinedPayment();

    log.info("Hola {}", data);

    List<Trip> tripEntities =
        trips.stream()
            .map(trip -> (String) trip.get("id"))
            .map(tripRepository::findOne)
            .collect(Collectors.toList());

    joinedPaymentRepository.save(joinedPayment);

    for (Trip t : tripEntities) {
      t.setJoinedPayment(joinedPayment);
      tripRepository.save(t);
    }
  }

  public Map<String, Object> joinTrips(Map<String, Object> data) {
    String originId = (String) data.get("origin");
    String destinationId = (String) data.get("destination");

    Trip originalTrip = tripRepository.findOne(originId);
    Trip destinationTrip = tripRepository.findOne(destinationId);

    Map<String, TripSeat> originSeats =
        tripSeatRepository.findAllByTrip(originalTrip).stream()
            .collect(Collectors.toMap(TripSeat::getSeatName, Function.identity()));

    // Map<String, TripSeat> destinationSeats =
    // tripSeatRepository.findAllByTrip(destinationTrip).stream()
    //   .collect(Collectors.toMap(
    //                             TripSeat::getSeatName,
    //                             Function.identity()
    //                             ));

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> movements = (List<Map<String, Object>>) data.get("movements");

    Map<String, StopOff> newStops =
        destinationTrip.getRun().getStops().stream()
            .collect(Collectors.toMap(StopOff::getName, Function.identity()));

    movements.forEach(
        movement -> {
          String originName = (String) movement.get("originName");
          String destinationName = (String) movement.get("destinationName");

          StopOff endingStop = newStops.get(originSeats.get(originName).getEndingStop().getName());

          if (destinationName != null && endingStop != null) {
            originSeats.get(originName).setTrip(destinationTrip);
            originSeats.get(originName).setSeatName(destinationName);
            originSeats
                .get(originName)
                .setStartingStop(
                    newStops.get(originSeats.get(originName).getStartingStop().getName()));
            originSeats.get(originName).setEndingStop(endingStop);

            originalTrip.getSeats().remove(originSeats.get(originName));
            destinationTrip.getSeats().add(originSeats.get(originName));

            tripSeatRepository.save(originSeats.get(originName));
            tripRepository.save(destinationTrip);
          }
        });

    if (originalTrip.getSeats().size() == movements.size()) {
      originalTrip.getSeats().clear();
      originalTrip.setStatus(TripStatus.CANCELLED);
    }

    tripRepository.save(originalTrip);

    return data;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> blockTrips(Map<String, Object> data) {
    String tripId = (String) data.get("tripId");
    String startingStopId = (String) data.get("startingStopId");
    String endingStopId = (String) data.get("endingStopId");
    List<String> reservations = (List<String>) data.get("reservations");
    List<String> unreservations = (List<String>) data.get("unreservations");

    Trip trip = tripRepository.findOne(tripId);
    StopOff temporaryStartingStop = stopOffRepository.findOne(startingStopId);
    StopOff temporaryEndingStop = stopOffRepository.findOne(endingStopId);
    StopOff startingStop;
    StopOff endingStop;

    startingStop =
        trip.getRun().getStops().stream()
            .filter(stop -> stop.getName().equals(temporaryStartingStop.getName()))
            .findFirst()
            .get();

    endingStop =
        trip.getRun().getStops().stream()
            .filter(stop -> stop.getName().equals(temporaryEndingStop.getName()))
            .findFirst()
            .get();

    reservations.forEach(
        seatName -> {
          TripSeat seat = new TripSeat();
          seat.setPassengerName("Reservado");
          seat.setSeatName(seatName);
          seat.setTrip(trip);
          seat.setPassengerType(PassengerType.ADULT);
          seat.setSoldPrice(BigDecimal.ZERO);
          seat.setStatus(SeatStatus.RESERVED);
          seat.setStartingStop(startingStop);
          seat.setEndingStop(endingStop);
          seat.setTicketId(UUID.randomUUID().toString());
          trip.getSeats().add(seat);
          tripSeatRepository.save(seat);
          tripRepository.save(trip);
        });

    unreservations.forEach(
        seatName -> {
          List<TripSeat> seats = tripSeatRepository.findAllByTripAndSeatName(trip, seatName);

          for (TripSeat seat : seats) {
            if (seat.getStartingStop().getName().equals(startingStop.getName())
                && seat.getStatus() == SeatStatus.RESERVED) {
              trip.getSeats().remove(seat);
              tripRepository.save(trip);
              tripSeatRepository.delete(seat);
            }
          }
        });

    return data;
  }

  public List<TripBusData> findTrackingTripsByDay(String date, String tz) {
    return findTripsByDay(date, tz);
  }

  public TicketData recordUsed(String id) {
    TripSeat tripSeat = tripSeatRepository.findByTicketId(id);

    if (tripSeat.getTrip().getJoinedPayment() == null
        || !tripSeat.getTrip().getJoinedPayment().isPayed()) {
      if (tripSeat.getStatus().equals(SeatStatus.USED)) {
        TicketData result = mapper.map(tripSeat, TicketData.class);
        result.setStatus("ALREADY_USED");
        return result;
      } else {
        tripSeat.setStatus(SeatStatus.USED);
        tripSeatRepository.save(tripSeat);
      }
    }

    return mapper.map(tripSeat, TicketData.class);
  }

  public List<TripStopData> findStops(
      String startDate, String endDate, String lastUpdate, String timeZone) {
    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    Date startDateTime = new DateTime(startDate, zone).toDate();
    Date endDateTime = new DateTime(endDate, zone).toDate();
    Date lastUpdateDate = new DateTime(lastUpdate, zone).toDate();

    return tripRepository.findAllTripsByDepartureDateBetween(startDateTime, endDateTime).stream()
        .flatMap(
            trip -> {
              return tripStopControlRepository
                  .findAllByTripAndLastUpdatedGreaterThanEqual(trip, lastUpdateDate).stream()
                  .map(
                      (stopControl) -> {
                        TripStopData stopData = new TripStopData();

                        stopData.setId(stopControl.getId());
                        stopData.setBeginning(
                            mapper.map(trip.getRun().getBeginning(), StopOffData.class));
                        stopData.setEnding(
                            mapper.map(trip.getRun().getDestination(), StopOffData.class));
                        stopData.setStop(mapper.map(stopControl.getStopOff(), StopOffData.class));
                        stopData.setTrip(mapper.map(trip, TripData.class));
                        stopData.setRunName(trip.getRun().getName());
                        stopData.setDepartureDate(trip.getDepartureDate());

                        if (trip.getDriver1() != null) {
                          stopData.setDriver1(
                              trip.getDriver1().getName() + " " + trip.getDriver1().getLastName());
                        }

                        if (trip.getBus() != null) {
                          stopData.setBusNumber(trip.getBus().getBusNumber());
                        }
                        return stopData;
                      });
            })
        .collect(Collectors.toList());
  }

  public List<TicketData> findTickets(
      String startDate, String endDate, String lastUpdate, String timeZone) {
    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    Date startDateTime = new DateTime(startDate, zone).toDate();
    Date endDateTime = new DateTime(endDate, zone).toDate();
    Date lastUpdateDate = new DateTime(lastUpdate, zone).toDate();

    return tripSeatRepository.findAllByTripAndDates(startDateTime, endDateTime, lastUpdateDate)
        .stream()
        .filter(trip -> trip.getTicketId() != null)
        .map(tripSeat -> mapper.map(tripSeat, TicketData.class))
        .collect(Collectors.toList());
  }

  public InputStream downloadBoardingList(String tripId, String timeZone) throws Exception {
    return generateBoardingList(tripId, timeZone);
  }

  public void cancelTickets(SeatStatus status, int time) {
    List<TripSeat> seats = tripSeatRepository.findAllByStatus(status);
    log.info("Looking into {} tickets", seats.size());
    DateTime now = new DateTime();
    seats.forEach(
        seat -> {
          DateTime departureDate =
              new DateTime(calculateTimeAtStop(seat.getTrip(), seat.getStartingStop()));

          log.debug("Comparing dates {} {}", now, departureDate);

          Seconds seconds = Seconds.secondsBetween(now, departureDate);

          Seconds compareSeconds = Seconds.seconds(time);
          log.debug("Check seconds {} {}", seconds, seconds.isLessThan(compareSeconds));

          if (seconds.isLessThan(compareSeconds)) {
            Trip trip = tripRepository.findOne(seat.getTrip().getId());
            trip.getSeats().remove(seat);
            CancelReservation cancelReservation = new CancelReservation();
            cancelReservation.setStatus(seat.getStatus());
            cancelReservation.setInternetSale(seat.getInternetSale());
            cancelReservation.setSeat(seat.getSeat());
            cancelReservation.setStartingStop(seat.getStartingStop());
            cancelReservation.setEndingStop(seat.getEndingStop());
            cancelReservation.setTrip(seat.getTrip());
            cancelReservation.setUser(seat.getUser());
            cancelReservation.setPassengerType(seat.getPassengerType());
            cancelReservation.setSoldPrice(seat.getSoldPrice());
            cancelReservation.setPayedPrice(seat.getPayedPrice());
            cancelReservation.setSeatName(seat.getSeatName());
            cancelReservation.setPassengerName(seat.getPassengerName());
            cancelReservation.setComments(seat.getComments());
            cancelReservationRepository.save(cancelReservation);
            tripRepository.save(trip);
            tripSeatRepository.delete(seat);
          }
        });
    log.info("Processed tickets {}", seats.size());
  }

  @Scheduled(fixedRate = 60000)
  public void releaseReserved() {
    cancelTickets(SeatStatus.RESERVED, 3600);
  }

  @Scheduled(fixedRate = 600000)
  public void assignTrips() {
    Date yesterday = new DateTime().minusDays(1).toDate();
    Date tomorrow = new DateTime().plusDays(2).toDate();
    tripRepository
        .findAllTripsByDepartureDateBetween(yesterday, tomorrow)
        .forEach(
            trip -> {
              boolean payed =
                  Optional.ofNullable(trip.getJoinedPayment())
                      .map(JoinedPayment::isPayed)
                      .orElse(false);

              if (trip.getBus() != null && !payed) {
                trip.setDriver1(trip.getBus().getDriver1());
                trip.setDriver2(trip.getBus().getDriver2());
                tripRepository.save(trip);
                /*
                final Trip tripReference = trip;
                Integer count = tripStopControlRepository.findAllByTrip(tripReference).size();
                if(count == 0) {
                  List<TripStopControl> controls = trip.getRun().getRoute().getStops().stream()
                    .map(stop -> {
                      TripStopControl control = new TripStopControl();
                      control.setStopOff(stop);
                      control.setTrip(tripReference);
                      control.setVisited(false);
                      return control;
                    })
                    .collect(Collectors.toList());

                  tripStopControlRepository.save(controls);
                }
                */
              }
            });
  }

  public List<TripLogisticData> findLogisticTrips(
      String start, String end, String routeName, String tz) {
    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(tz))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    Date startDate = new DateTime(start).withZoneRetainFields(zone).withTimeAtStartOfDay().toDate();
    Date endDate = new DateTime(end).withZoneRetainFields(zone).withTimeAtStartOfDay().toDate();

    List<Trip> existingTrips =
        tripRepository.findAllTripsByDepartureDateBetweenAndRunName(startDate, endDate, routeName);

    return existingTrips.stream()
        .map(
            trip -> {
              Run run = trip.getRun();

              TripLogisticData data = new TripLogisticData();

              data.setRoute(mapper.map(run.getRoute(), RouteData.class));
              data.setRunId(run.getId());
              data.setOriginId(run.getBeginning().getId());
              data.setOriginName(run.getBeginning().getName());
              data.setDestinationId(run.getDestination().getId());
              data.setDestinationName(run.getDestination().getName());
              data.setReverse(trip.getReverse());

              Trip existingService = trip;
              List<TripSeat> tickets = tripSeatRepository.findAllByTrip(existingService);

              Map<String, List<TripSeat>> groupedTickets = groupTripData(existingService, tickets);

              Integer maxSize =
                  groupedTickets.values().stream()
                      .max((a, b) -> Integer.valueOf(a.size()).compareTo(Integer.valueOf(b.size())))
                      .map(l -> l.size())
                      .orElse(0);

              Integer minSize =
                  groupedTickets.values().stream()
                      .min((a, b) -> Integer.valueOf(a.size()).compareTo(Integer.valueOf(b.size())))
                      .map(l -> l.size())
                      .orElse(0);

              data.setId(existingService.getId());
              data.setDepartureDate(existingService.getDepartureDate());
              data.setTotalSeats(groupedTickets.size());
              data.setTotalPassengers(tickets.size());

              data.setLargestOccupation(maxSize.intValue());
              data.setShortestOccupation(minSize.intValue());
              data.setAverage(
                  groupedTickets.values().stream()
                      .mapToInt(vals -> vals.size())
                      .average()
                      .orElse(0));

              if (existingService.getBus() != null) {
                data.setBusId(existingService.getBus().getId());
                data.setBusPlates(existingService.getBus().getPlates());
                data.setBusNumber(existingService.getBus().getBusNumber());
              }

              return data;
            })
        .sorted((a, b) -> b.getAverage().compareTo(a.getAverage()))
        .collect(Collectors.toList());
  }

  DayOfWeek getDayOfWeek(DateTime day) {
    final DayOfWeek dayOfWeek;

    switch (day.getDayOfWeek()) {
      case DateTimeConstants.MONDAY:
        dayOfWeek = DayOfWeek.MONDAY;
        break;
      case DateTimeConstants.TUESDAY:
        dayOfWeek = DayOfWeek.TUESDAY;
        break;
      case DateTimeConstants.WEDNESDAY:
        dayOfWeek = DayOfWeek.WEDNESDAY;
        break;
      case DateTimeConstants.THURSDAY:
        dayOfWeek = DayOfWeek.THURSDAY;
        break;
      case DateTimeConstants.FRIDAY:
        dayOfWeek = DayOfWeek.FRIDAY;
        break;
      case DateTimeConstants.SATURDAY:
        dayOfWeek = DayOfWeek.SATURDAY;
        break;
      case DateTimeConstants.SUNDAY:
        dayOfWeek = DayOfWeek.SUNDAY;
        break;
      default:
        dayOfWeek = null;
        break;
    }

    return dayOfWeek;
  }

  Date calculateTimeAtStop(Trip trip, StopOff stop) {
    DateTime startingDate = new DateTime(trip.getDepartureDate());
    boolean reverse = trip.getReverse();
    Comparator<StopOff> originalComparator =
        (a, b) -> a.getOrderIndex().compareTo(b.getOrderIndex());
    Comparator<StopOff> reverseComparator =
        (a, b) -> a.getOrderIndex().compareTo(b.getOrderIndex());
    Comparator<StopOff> comparator;
    if (reverse) {
      comparator = reverseComparator;
    } else {
      comparator = originalComparator;
    }
    List<StopOff> stops =
        trip.getRun().getStops().stream().sorted(comparator).collect(Collectors.toList());
    Long minutes = 0l;
    Boolean found = false;
    for (StopOff currentStop : stops) {
      minutes += currentStop.getTravelMinutes();
      if (currentStop.getWaitingMinutes() != null) {
        minutes += currentStop.getWaitingMinutes();
      }
      if (currentStop.getId().equals(stop.getId())) {
        found = true;
        break;
      }
    }

    if (!found) {
      return null;
    }

    return startingDate.plusMinutes(minutes.intValue()).toDate();
  }
}
