package justcloud.tickets.service.sales;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import justcloud.tickets.domain.*;
import justcloud.tickets.domain.repository.*;
import justcloud.tickets.domain.sales.*;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.dto.*;
import justcloud.tickets.service.JasperService;
import justcloud.tickets.service.UserService;
import ma.glasnost.orika.MapperFacade;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentBoothService {

  private Logger logger = LoggerFactory.getLogger(PaymentBoothService.class);

  @Autowired private JoinedPaymentRepository joinedPaymentRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private PaymentShiftRepository paymentShiftRepository;

  @Autowired private PaymentCashCheckpointRepository paymentCashCheckpointRepository;

  @Autowired private SalesTerminalRepository salesTerminalRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private MapperFacade mapper;

  @Autowired private UserService userService;

  @Autowired private JasperService jasperService;

  public List<SalesTerminalData> listTerminalsByOffice(String officeId) {
    return salesTerminalRepository.findAllByOfficeLocationId(officeId).stream()
        .map(terminal -> mapper.map(terminal, SalesTerminalData.class))
        .collect(Collectors.toList());
  }

  public List<SalesBoothData> listBooths(String salesTerminalId) {
    SalesTerminal terminal = salesTerminalRepository.findByTerminalId(salesTerminalId);
    return salesTerminalRepository
        .findAllByOfficeLocationAndPaymentTerminal(terminal.getOfficeLocation(), true).stream()
        .map(this::mapTerminal)
        .collect(Collectors.toList());
  }

  public SalesBoothData startShift(String terminalId, String userEmployeeId) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    User user = userRepository.findByUsername(userEmployeeId);

    PaymentShift oldShift =
        paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);
    Optional.ofNullable(oldShift)
        .ifPresent(
            (shift) -> {
              shift.setFinished(true);
              shift.setFinishDate(new Date());
              // Handle send email
              paymentShiftRepository.save(shift);
            });

    PaymentShift paymentShift = new PaymentShift();
    paymentShift.setChiefUser(Optional.ofNullable(userService.getCurrentUser()).orElse(user));
    paymentShift.setStartingAmount(
        Optional.ofNullable(salesTerminal.getPaymentAvailableAmount()).orElse(BigDecimal.ZERO));
    paymentShift.setUser(user);
    paymentShift.setFinished(false);
    paymentShift.setSalesTerminal(salesTerminal);

    paymentShiftRepository.save(paymentShift);
    salesTerminalRepository.save(salesTerminal);

    return mapTerminal(salesTerminal);
  }

  public InputStream getStartShiftInputStream(String terminalId, String timeZone) throws Exception {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    PaymentShift shift =
        paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    String chiefName =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getChiefUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getChiefUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getChiefUser().getSecondLastName()).orElse(""))
            .toString();

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    DateTimeZone zone =
        Optional.ofNullable(timeZone).map(DateTimeZone::forID).orElse(DateTimeZone.getDefault());

    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> params = new HashMap<>();
    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("NoTaquilla", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateTimeFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("Hora", timeFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("InicioCaja", format.format(salesTerminal.getPaymentAvailableAmount()));
    params.put("Liquidadora", salesman);

    JRMapCollectionDataSource ds = new JRMapCollectionDataSource(new ArrayList<>());

    return new ByteArrayInputStream(jasperService.getPdfBytes("AperturaCaja", params, ds));
  }

  private SalesBoothData mapTerminal(SalesTerminal booth) {
    SalesBoothData data = mapper.map(booth, SalesBoothData.class);
    Optional.ofNullable(paymentShiftRepository.findBySalesTerminalAndFinished(booth, false))
        .ifPresent(
            paymentShift -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(paymentShift.getUser().getName())
                      .append(" ")
                      .append(paymentShift.getUser().getLastName())
                      .append(" ")
                      .append(paymentShift.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(paymentShift.getDateCreated());
            });
    return data;
  }

  public InputStream getRecordSnapshot(String terminalId, String timeZone) throws Exception {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    PaymentShift shift =
        paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    List<PaymentCashCheckpoint> checkpoints =
        paymentCashCheckpointRepository.findAllByPaymentShift(shift);

    DateTimeZone zone =
        Optional.ofNullable(timeZone).map(DateTimeZone::forID).orElse(DateTimeZone.getDefault());

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    PaymentCashCheckpoint latest =
        checkpoints.stream()
            .reduce(
                (curr, checkpoint) -> {
                  if (curr.getDateCreated().compareTo(checkpoint.getDateCreated()) > 0) {
                    return curr;
                  } else {
                    return checkpoint;
                  }
                })
            .get();

    List<JoinedPayment> groups = joinedPaymentRepository.findAllByPaymentCashCheckpoint(latest);

    BigDecimal total =
        groups.stream()
            .map(
                trip -> {
                  BigDecimal driver1Amount =
                      Optional.ofNullable(trip)
                          .map(JoinedPayment::getDriver1Amount)
                          .orElse(BigDecimal.ZERO);
                  BigDecimal driver2Amount =
                      Optional.ofNullable(trip)
                          .map(JoinedPayment::getDriver2Amount)
                          .orElse(BigDecimal.ZERO);

                  return driver1Amount.add(driver2Amount);
                })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    List<Map<String, ?>> dataRows =
        groups.stream()
            .flatMap(
                group -> {
                  List<Map<String, Object>> rows = new ArrayList<>();

                  String operatorName =
                      Optional.ofNullable(group)
                          .map(JoinedPayment::getTrips)
                          .map(List::stream)
                          .flatMap(Stream::findAny)
                          .map(Trip::getDriver1)
                          .map(
                              driver -> {
                                return new StringBuilder()
                                    .append(Optional.ofNullable(driver.getName()).orElse(""))
                                    .append(" ")
                                    .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                                    .append(" ")
                                    .append(
                                        Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                                    .toString();
                              })
                          .orElse("");

                  Map<String, Object> data = new HashMap<>();
                  data.put("Operador", operatorName);
                  data.put(
                      "Liquidado",
                      "$ "
                          + format.format(
                              Optional.ofNullable(group)
                                  .map(JoinedPayment::getDriver1Amount)
                                  .orElse(BigDecimal.ZERO)));

                  rows.add(data);

                  Optional.ofNullable(group)
                      .ifPresent(
                          d -> {
                            String opName =
                                Optional.ofNullable(d)
                                    .map(JoinedPayment::getTrips)
                                    .map(List::stream)
                                    .flatMap(Stream::findAny)
                                    .map(Trip::getDriver1)
                                    .map(
                                        driver -> {
                                          return new StringBuilder()
                                              .append(
                                                  Optional.ofNullable(driver.getName()).orElse(""))
                                              .append(" ")
                                              .append(
                                                  Optional.ofNullable(driver.getLastName())
                                                      .orElse(""))
                                              .append(" ")
                                              .append(
                                                  Optional.ofNullable(driver.getSecondLastName())
                                                      .orElse(""))
                                              .toString();
                                        })
                                    .orElse("");

                            Map<String, Object> da = new HashMap<>();
                            da.put("Operador", opName);
                            da.put(
                                "Liquidado",
                                "$ "
                                    + format.format(
                                        Optional.ofNullable(group)
                                            .map(JoinedPayment::getDriver2Amount)
                                            .orElse(BigDecimal.ZERO)));

                            rows.add(da);
                          });

                  return rows.stream();
                })
            .collect(Collectors.toList());

    String chiefName =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getChiefUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getChiefUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getChiefUser().getSecondLastName()).orElse(""))
            .toString();

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    Map<String, Object> params = new HashMap<>();
    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("TerminalNo", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateTimeFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("Hora", timeFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("Fondo", "$ " + format.format(latest.getPreviousAmount()));
    params.put(
        "Ingreso",
        "$ " + format.format(latest.getNewAmount().subtract(latest.getPreviousAmount())));
    params.put("MontoEnCaja", "$ " + format.format(latest.getNewAmount()));
    params.put("Total", "$ " + format.format(total));
    params.put("JefeLiquidacion", chiefName);
    params.put("Liquidador", salesman);

    JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dataRows);

    return new ByteArrayInputStream(jasperService.getPdfBytes("PrecorteLiquidaciones", params, ds));
  }

  public SalesBoothData recordSnapshot(String terminalId, double amount) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    PaymentShift paymentShift =
        paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    BigDecimal previousAmount =
        Optional.ofNullable(salesTerminal.getPaymentAvailableAmount()).orElse(BigDecimal.ZERO);

    salesTerminal.setPaymentAvailableAmount(previousAmount);

    BigDecimal newAmount = previousAmount.add(BigDecimal.valueOf(amount));

    List<JoinedPayment> payments =
        joinedPaymentRepository.findAllByPaymentShiftAndPaymentCashCheckpointIsNull(paymentShift);

    PaymentCashCheckpoint checkpoint = new PaymentCashCheckpoint();
    checkpoint.setPaymentShift(paymentShift);
    checkpoint.setPreviousAmount(previousAmount);
    checkpoint.setNewAmount(newAmount);

    salesTerminalRepository.save(salesTerminal);
    paymentCashCheckpointRepository.save(checkpoint);

    payments.forEach(
        payment -> {
          payment.setPaymentCashCheckpoint(checkpoint);
          payment.setPaymentTerminal(salesTerminal);
          joinedPaymentRepository.save(payment);
        });

    SalesBoothData data = mapper.map(salesTerminal, SalesBoothData.class);
    Optional.ofNullable(paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false))
        .ifPresent(
            paymentShift1 -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(paymentShift1.getUser().getName())
                      .append(" ")
                      .append(paymentShift1.getUser().getLastName())
                      .append(" ")
                      .append(paymentShift1.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(paymentShift1.getDateCreated());
            });
    return data;
  }

  public InputStream downloadCloseShift(String boothId, String timeZone) throws Exception {
    SalesTerminal terminal = salesTerminalRepository.findByTerminalId(boothId);
    PaymentShift shift = paymentShiftRepository.findLatestBySalesTerminalId(terminal.getId());

    List<PaymentCashCheckpoint> checkpoints =
        paymentCashCheckpointRepository.findAllByPaymentShift(shift);
    List<JoinedPayment> groups = joinedPaymentRepository.findAllByPaymentShift(shift);

    List<BigDecimal> amounts = new ArrayList<>();

    amounts.addAll(
        groups.stream()
            .map(JoinedPayment::getDriver1Amount)
            .filter(amount -> amount != null)
            .collect(Collectors.toList()));

    amounts.addAll(
        groups.stream()
            .map(JoinedPayment::getDriver2Amount)
            .filter(amount -> amount != null)
            .collect(Collectors.toList()));

    BigDecimal totalAmount = amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

    DateTimeZone zone =
        Optional.ofNullable(timeZone).map(DateTimeZone::forID).orElse(DateTimeZone.getDefault());

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> params = new HashMap<>();

    String chiefName =
        Optional.ofNullable(shift.getChiefUser())
            .map(
                user ->
                    new StringBuilder()
                        .append(Optional.ofNullable(user.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getSecondLastName()).orElse(""))
                        .toString())
            .orElse("");

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    PaymentCashCheckpoint latest =
        checkpoints.stream()
            .reduce(
                (curr, checkpoint) -> {
                  if (curr.getDateCreated().compareTo(checkpoint.getDateCreated()) > 0) {
                    return curr;
                  } else {
                    return checkpoint;
                  }
                })
            .get();

    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("TerminalNo", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateFormatter.print(new DateTime(shift.getFinishDate())));
    params.put("Hora", dateFormatter.print(new DateTime(shift.getFinishDate())));
    params.put("Fondo", "$ " + format.format(totalAmount));
    params.put(
        "Ingreso",
        "$ " + format.format(latest.getNewAmount().subtract(latest.getPreviousAmount())));
    params.put("Total", "$ " + format.format(totalAmount));
    params.put("JefeLiquidacion", chiefName);
    params.put("MontoEnCaja", format.format(shift.getSalesTerminal().getPaymentAvailableAmount()));
    params.put("Liquidador", salesman);

    params.put(
        "Precortes",
        checkpoints.stream()
            .map(
                checkpoint -> {
                  Map<String, Object> data = new HashMap<>();
                  data.put(
                      "FechaPrecorte",
                      dateFormatter.print(new DateTime(checkpoint.getDateCreated())));
                  data.put(
                      "HoraPrecorte",
                      timeFormatter.print(new DateTime(checkpoint.getDateCreated())));
                  data.put("Fondo", "$ " + format.format(checkpoint.getPreviousAmount()));
                  data.put(
                      "Agregado",
                      "$ "
                          + format.format(
                              checkpoint.getNewAmount().subtract(checkpoint.getPreviousAmount())));
                  return data;
                })
            .collect(Collectors.toList()));
    params.put(
        "Liquidaciones",
        groups.stream()
            .flatMap(
                group -> {
                  List<Map<String, Object>> rows = new ArrayList<>();

                  Optional.ofNullable(group.getDriver1())
                      .ifPresent(
                          driver -> {
                            String operator =
                                new StringBuilder()
                                    .append(Optional.ofNullable(driver.getName()).orElse(""))
                                    .append(" ")
                                    .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                                    .append(" ")
                                    .append(
                                        Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                                    .toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("Operador", operator);
                            data.put(
                                "Liquidado",
                                "$ "
                                    + format.format(
                                        Optional.ofNullable(group.getDriver1Amount())
                                            .orElse(BigDecimal.ZERO)));

                            rows.add(data);
                          });

                  Optional.ofNullable(group.getDriver2())
                      .ifPresent(
                          driver -> {
                            String operator =
                                new StringBuilder()
                                    .append(Optional.ofNullable(driver.getName()).orElse(""))
                                    .append(" ")
                                    .append(Optional.ofNullable(driver.getLastName()).orElse(""))
                                    .append(" ")
                                    .append(
                                        Optional.ofNullable(driver.getSecondLastName()).orElse(""))
                                    .toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("Operador", operator);
                            data.put(
                                "Liquidado",
                                "$ "
                                    + format.format(
                                        Optional.ofNullable(group.getDriver2Amount())
                                            .orElse(BigDecimal.ZERO)));

                            rows.add(data);
                          });

                  return rows.stream();
                })
            .collect(Collectors.toList()));

    return new ByteArrayInputStream(jasperService.getPdfBytes("CorteLiquidacion", params));
  }

  public SalesBoothData closeShift(String terminalId, double amount) {

    recordSnapshot(terminalId, amount);

    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    PaymentShift paymentShift =
        paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);
    paymentShift.setFinished(true);

    paymentShiftRepository.save(paymentShift);

    SalesBoothData data = mapper.map(salesTerminal, SalesBoothData.class);

    data.setLatestShiftId(paymentShift.getId());

    Optional.ofNullable(paymentShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false))
        .ifPresent(
            paymentShift1 -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(paymentShift1.getUser().getName())
                      .append(" ")
                      .append(paymentShift1.getUser().getLastName())
                      .append(" ")
                      .append(paymentShift1.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(paymentShift1.getDateCreated());
            });
    return data;
  }

  public SalesBoothData getStatus(String terminalId) {
    User currentUser = userService.getCurrentUser();

    PaymentShift currentShift = paymentShiftRepository.findByUserAndFinished(currentUser, false);

    if (currentShift == null) {
      return null;
    }

    if (!currentShift.getSalesTerminal().getTerminalId().equals(terminalId)) {
      return null;
    }

    return mapTerminal(currentShift.getSalesTerminal());
  }

  public InputStream getPersonDetail(String personId, String timeZone, Date from, Date to)
      throws Exception {

    DateTimeZone zone =
        Optional.ofNullable(timeZone).map(DateTimeZone::forID).orElse(DateTimeZone.getDefault());

    User user = userRepository.findOne(personId);
    PaymentShift lastShift = paymentShiftRepository.findLatestByUser(personId);
    List<JoinedPayment> groups = joinedPaymentRepository.findAllByUserId(personId);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");
    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(lastShift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(lastShift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(lastShift.getUser().getSecondLastName()).orElse(""))
            .toString();

    BigDecimal total =
        groups.stream()
            .map(
                group -> {
                  return BigDecimal.ZERO
                      .add(Optional.ofNullable(group.getDriver1Amount()).orElse(BigDecimal.ZERO))
                      .add(Optional.ofNullable(group.getDriver2Amount()).orElse(BigDecimal.ZERO));
                })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Map<String, Object> params = new HashMap<>();
    params.put("liquidador", salesman);
    params.put("since", dateTimeFormatter.print(new DateTime(from)));
    params.put("to", dateTimeFormatter.print(new DateTime(to)));
    params.put("dateCreated", dateTimeFormatter.print(new DateTime()));
    params.put("totalLiquidados", String.valueOf(groups.size()));
    params.put("montoLiquidados", format.format(total));

    List<Map<String, ?>> rows =
        groups.stream()
            .flatMap(
                group -> {
                  List<Map<String, Object>> tripRows = new ArrayList<>();

                  Optional.ofNullable(group.getDriver1Amount())
                      .ifPresent(
                          amount -> {
                            tripRows.add(
                                new HashMap<String, Object>() {
                                  {
                                    String driver =
                                        new StringBuilder()
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver1)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .append(" ")
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver1)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .append(" ")
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver1)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .toString();

                                    put("operador", driver);
                                    put("monto", format.format(amount));
                                  }
                                });
                          });

                  Optional.ofNullable(group.getDriver2Amount())
                      .ifPresent(
                          amount -> {
                            tripRows.add(
                                new HashMap<String, Object>() {
                                  {
                                    String driver =
                                        new StringBuilder()
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver2)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .append(" ")
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver2)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .append(" ")
                                            .append(
                                                Optional.ofNullable(group)
                                                    .map(JoinedPayment::getTrips)
                                                    .map(List::stream)
                                                    .orElse(Stream.empty())
                                                    .findFirst()
                                                    .map(Trip::getDriver2)
                                                    .map(Employee::getName)
                                                    .orElse(""))
                                            .toString();

                                    put("operador", driver);
                                    put("monto", format.format(amount));
                                  }
                                });
                          });

                  return tripRows.stream();
                })
            .collect(Collectors.toList());

    return new ByteArrayInputStream(
        jasperService.getPdfBytes(
            "DetalleLiquidacion", params, new JRMapCollectionDataSource(rows)));
  }

  public List<UserData> findPeople(String salesTerminalId, String startDate, String endDate) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(salesTerminalId);
    OfficeLocation officeLocation = salesTerminal.getOfficeLocation();
    Date startingDate = new DateTime(startDate).toDate();
    Date endingDate = new DateTime(endDate).toDate();

    return paymentShiftRepository
        .findAllBySalesTerminalOfficeLocationAndDateCreatedBetween(
            officeLocation, startingDate, endingDate)
        .stream()
        .map(PaymentShift::getUser)
        .map(user -> mapper.map(user, UserData.class))
        .distinct()
        .collect(Collectors.toList());
  }

  public List<TripData> findTrips(
      String officeId, String startDate, String endDate, String operatorId, String routeId) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(officeId);
    OfficeLocation officeLocation = salesTerminal.getOfficeLocation();
    Date startingDate =
        new DateTime(startDate)
            .withZoneRetainFields(DateTimeZone.forID("America/Mexico_City"))
            .withTimeAtStartOfDay()
            .toDate();
    Date endingDate =
        new DateTime(endDate)
            .withZoneRetainFields(DateTimeZone.forID("America/Mexico_City"))
            .plusDays(1)
            .withTimeAtStartOfDay()
            .toDate();

    Specification<Trip> tripSpecification =
        new Specification<Trip>() {

          @Override
          public Predicate toPredicate(
              Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.between(root.get(Trip_.departureDate), startingDate, endingDate));
            predicates.add(cb.isTrue(root.get(Trip_.joinedPayment).get(JoinedPayment_.payed)));

            if (StringUtils.isNotEmpty(operatorId)) {
              predicates.add(
                  cb.or(
                      cb.equal(root.get(Trip_.driver1).get(Employee_.id), operatorId),
                      cb.equal(root.get(Trip_.driver2).get(Employee_.id), operatorId)));
            }
            if (StringUtils.isNotEmpty(routeId)) {
              predicates.add(cb.equal(root.get(Trip_.run).get(Run_.route).get(Route_.id), routeId));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
          }
        };

    return tripRepository.findAll(tripSpecification).stream()
        .map(
            trip -> {
              TripData data = new TripData();
              data.setId(trip.getId());
              data.setRouteData(mapper.map(trip.getRun().getRoute(), RouteData.class));
              data.setDepartureDate(trip.getDepartureDate());
              data.setBusName(trip.getBus().getBusNumber());
              data.setDriver1Name(
                  Optional.ofNullable(trip.getDriver1())
                      .map(
                          driver -> {
                            return new StringBuilder()
                                .append(driver.getName())
                                .append(" ")
                                .append(driver.getLastName())
                                .append(" ")
                                .append(driver.getSecondLastName())
                                .toString();
                          })
                      .orElse(null));
              data.setDriver2Name(
                  Optional.ofNullable(trip.getDriver2())
                      .map(
                          driver -> {
                            return new StringBuilder()
                                .append(driver.getName())
                                .append(" ")
                                .append(driver.getLastName())
                                .append(" ")
                                .append(driver.getSecondLastName())
                                .toString();
                          })
                      .orElse(null));
              return data;
            })
        .collect(Collectors.toList());
  }

  public List<TripData> findUnjoinedTrips(String startDate, String endDate) {
    Date startingDate = new DateTime(startDate).toDate();
    Date endingDate = new DateTime(endDate).toDate();

    return tripRepository.findAllByDepartureDateBetween(startingDate, endingDate).stream()
        .filter(trip -> trip.getJoinedPayment() == null || !trip.getJoinedPayment().isPayed())
        .map(
            trip -> {
              TripData data = new TripData();
              data.setId(trip.getId());
              data.setRouteData(mapper.map(trip.getRun().getRoute(), RouteData.class));
              data.setDepartureDate(trip.getDepartureDate());
              data.setBusName(Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(""));
              data.setDriver1Name(
                  Optional.ofNullable(trip.getDriver1())
                      .map(
                          driver -> {
                            return new StringBuilder()
                                .append(driver.getName())
                                .append(" ")
                                .append(driver.getLastName())
                                .append(" ")
                                .append(driver.getSecondLastName())
                                .toString();
                          })
                      .orElse(null));
              data.setDriver2Name(
                  Optional.ofNullable(trip.getDriver2())
                      .map(
                          driver -> {
                            return new StringBuilder()
                                .append(driver.getName())
                                .append(" ")
                                .append(driver.getLastName())
                                .append(" ")
                                .append(driver.getSecondLastName())
                                .toString();
                          })
                      .orElse(null));
              return data;
            })
        .collect(Collectors.toList());
  }
}
