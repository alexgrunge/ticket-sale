package justcloud.tickets.service.sales;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.repository.*;
import justcloud.tickets.domain.sales.*;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.dto.OfficeLocationData;
import justcloud.tickets.dto.SalesBoothData;
import justcloud.tickets.dto.SalesTerminalData;
import justcloud.tickets.dto.UserData;
import justcloud.tickets.service.JasperService;
import justcloud.tickets.service.UserService;
import justcloud.tickets.util.TimeZoneUtils;
import ma.glasnost.orika.MapperFacade;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BoothService {

  private static final Logger logger = LoggerFactory.getLogger(BoothService.class);

  @Autowired private SegmentVarRepository segmentVarRepository;

  @Autowired private UserService userService;

  @Autowired private PackageTicketRepository packageTicketRepository;

  @Autowired private OfficeLocationRepository officeLocationRepository;

  @Autowired private TripSeatRepository tripSeatRepository;

  @Autowired private SalesTerminalRepository salesTerminalRepository;

  @Autowired private SalesShiftRepository salesShiftRepository;

  @Autowired private CancelEventRepository cancelEventRepository;

  @Autowired private MapperFacade mapper;

  @Autowired private UserRepository userRepository;

  @Autowired private InternetSaleRepository internetSaleRepository;

  @Autowired private AccountSaleRepository accountSaleRepository;

  @Autowired private CashCheckpointRepository cashCheckpointRepository;

  @Autowired private JasperService jasperService;

  public List<SalesBoothData> listPaymentBooths(String salesTerminalId) {
    SalesTerminal terminal = salesTerminalRepository.findByTerminalId(salesTerminalId);
    return salesTerminalRepository
        .findAllByOfficeLocationAndPaymentTerminal(terminal.getOfficeLocation(), true).stream()
        .map(this::mapTerminal)
        .collect(Collectors.toList());
  }

  public List<SalesBoothData> listBooths(String salesTerminalId) {
    SalesTerminal terminal = salesTerminalRepository.findByTerminalId(salesTerminalId);
    return salesTerminalRepository
        .findAllByOfficeLocationAndSalesTerminal(terminal.getOfficeLocation(), true).stream()
        .map(this::mapTerminal)
        .collect(Collectors.toList());
  }

  public List<SalesTerminalData> listBoothsByOffice(String officeId) {
    return salesTerminalRepository.findAllByOfficeLocationId(officeId).stream()
        .map(terminal -> mapper.map(terminal, SalesTerminalData.class))
        .collect(Collectors.toList());
  }

  public InputStream getStartShiftInputStream(String terminalId, String timeZone) throws Exception {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    SalesShift shift = salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    String chiefName = "";

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> params = new HashMap<>();
    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("NoTaquilla", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("Hora", timeFormatter.print(new DateTime(shift.getDateCreated())));
    params.put("InicioCaja", "$ " + format.format(shift.getStartingAmount()));
    params.put("JefaOficina", chiefName);
    params.put("Taquillera", salesman);

    return new ByteArrayInputStream(jasperService.getPdfBytes("AperturaVenta", params));
  }

  public SalesBoothData startShift(String terminalId, String userUsername, double amount) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    User user = userRepository.findByUsername(userUsername);

    SalesShift oldShift = salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);
    if (oldShift != null) {
      closeShift(salesTerminal.getTerminalId());
    }
    oldShift = salesShiftRepository.findByUserAndFinished(user, false);
    if (oldShift != null) {
      closeShift(oldShift.getSalesTerminal().getTerminalId());
    }

    Long currentShift = salesTerminal.getOfficeLocation().getCurrentShift() + 1;

    SalesShift salesShift = new SalesShift();
    salesShift.setStartingAmount(BigDecimal.valueOf(amount));
    salesShift.setUser(user);
    salesShift.setCurrentSale(0l);
    salesShift.setShiftNumber(salesTerminal.getOfficeLocation().getShiftPrefix() + currentShift);
    salesShift.setFinished(false);
    salesShift.setSalesTerminal(salesTerminal);
    salesTerminal.setCurrentAmount(BigDecimal.valueOf(amount));

    salesTerminal.getOfficeLocation().setCurrentShift(currentShift);

    officeLocationRepository.save(salesTerminal.getOfficeLocation());
    salesShiftRepository.save(salesShift);
    salesTerminalRepository.save(salesTerminal);

    return mapTerminal(salesTerminal, salesShift);
  }

  private SalesBoothData mapTerminal(SalesTerminal booth, SalesShift salesShift) {
    SalesBoothData data = mapper.map(booth, SalesBoothData.class);

    StringBuilder nameBuilder = new StringBuilder();
    data.setSalesPersonName(
        nameBuilder
            .append(salesShift.getUser().getName())
            .append(" ")
            .append(salesShift.getUser().getLastName())
            .append(" ")
            .append(salesShift.getUser().getSecondLastName())
            .toString());
    data.setStartDate(salesShift.getDateCreated());

    return data;
  }

  private SalesBoothData mapTerminal(SalesTerminal booth) {
    SalesBoothData data = mapper.map(booth, SalesBoothData.class);

    Optional.ofNullable(salesShiftRepository.findBySalesTerminalAndFinished(booth, false))
        .ifPresent(
            salesShift -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(salesShift.getUser().getName())
                      .append(" ")
                      .append(salesShift.getUser().getLastName())
                      .append(" ")
                      .append(salesShift.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(salesShift.getDateCreated());
            });
    return data;
  }

  public InputStream latestSnapshotReport(String terminalId, String timeZone) throws Exception {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    SalesShift shift = salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);
    List<CashCheckpoint> checkpoints = cashCheckpointRepository.findBySalesShift(shift);

    CashCheckpoint latest =
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

    String chiefName = "";

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    List<InternetSale> sales =
        internetSaleRepository.findAllByCashCheckpoint(latest).stream()
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());
    List<CancelEvent> cancelEvents = cancelEventRepository.findAllByCashCheckpoint(latest);

    List<PaymentPart> parts = new LinkedList<>();

    parts.addAll(
        sales.stream()
            .flatMap(sale -> sale.getPaymentParts().stream())
            .collect(Collectors.toList()));

    Map<PaymentPartType, List<PaymentPart>> groupedParts =
        parts.stream().collect(Collectors.groupingBy(PaymentPart::getPaymentType));

    Map<PaymentPartType, BigDecimal> groupedAmounts = new HashMap<>();

    groupedParts.forEach(
        (type, paymentParts) -> {
          BigDecimal sum =
              paymentParts.stream()
                  .map(
                      (curr) -> {
                        return curr.getAmount()
                            .subtract(
                                Optional.ofNullable(curr.getChange()).orElse(BigDecimal.ZERO));
                      })
                  .reduce((acc, curr) -> acc.add(curr))
                  .get();
          groupedAmounts.put(type, sum);
        });

    BigDecimal cancelTotal =
        cancelEvents.stream()
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal total =
        parts.stream()
            .map(
                (curr) -> {
                  return curr.getAmount()
                      .subtract(Optional.ofNullable(curr.getChange()).orElse(BigDecimal.ZERO));
                })
            .reduce(
                (acc, curr) -> {
                  return acc.add(curr);
                })
            .orElse(BigDecimal.ZERO);

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> params = new HashMap<>();
    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("NoTaquilla", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateFormatter.print(new DateTime(latest.getDateCreated())));
    params.put("Hora", timeFormatter.print(new DateTime(latest.getDateCreated())));
    params.put(
        "VentaEfectivo",
        "$" + format.format(groupedAmounts.getOrDefault(PaymentPartType.CASH, BigDecimal.ZERO)));
    params.put(
        "VentaTC",
        "$"
            + format.format(
                groupedAmounts.getOrDefault(PaymentPartType.CREDIT_CARD, BigDecimal.ZERO)));
    params.put(
        "VentaME",
        "$" + format.format(groupedAmounts.getOrDefault(PaymentPartType.ACCOUNT, BigDecimal.ZERO)));
    params.put(
        "VentaDep",
        "$"
            + format.format(
                groupedAmounts.getOrDefault(PaymentPartType.TRANSFER, BigDecimal.ZERO)));
    params.put("InicioCaja", "$" + format.format(shift.getStartingAmount()));
    params.put(
        "Retiro", "$ " + format.format(latest.getPreviousAmount().subtract(latest.getNewAmount())));
    params.put("Resta", "$ " + format.format(latest.getNewAmount()));
    params.put("TotalVenta", "$ " + format.format(total));
    params.put("Cancelaciones", "$ " + format.format(cancelTotal));
    params.put("JefaOficina", chiefName);
    params.put("Taquillera", salesman);

    return new ByteArrayInputStream(jasperService.getPdfBytes("PrecorteCaja", params));
  }

  public SalesBoothData recordSnapshot(String terminalId, double amount) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    SalesShift saleShift =
        salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    List<CashCheckpoint> allCheckpoints = cashCheckpointRepository.findBySalesShift(saleShift);

    BigDecimal previousCheckpointSum =
        allCheckpoints.stream()
            .map(CashCheckpoint::getPreviousAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal newCheckpointSum =
        allCheckpoints.stream()
            .map(CashCheckpoint::getNewAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal retired = previousCheckpointSum.subtract(newCheckpointSum);

    List<InternetSale> sales =
        internetSaleRepository.findAllBySalesTerminalAndCashCheckpointIsNull(salesTerminal).stream()
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());
    List<CancelEvent> cancelEvents =
        cancelEventRepository.findAllBySaleShiftAndCashCheckpointIsNull(saleShift);
    List<AccountSale> accountSales =
        accountSaleRepository.findAllBySalesTerminalAndCashCheckpointIsNull(salesTerminal);

    List<InternetSale> allSales =
        internetSaleRepository.findAllBySalesShift(saleShift).stream()
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());

    List<AccountSale> allAccountSales = accountSaleRepository.findAllBySalesShift(saleShift);
    List<CancelEvent> allCancelEvents = cancelEventRepository.findAllBySaleShift(saleShift);

    BigDecimal ticketsPayed =
        allSales.stream()
            .map(InternetSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ticketsChange =
        allSales.stream()
            .map(InternetSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getChange)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal accountsPayed =
        allAccountSales.stream()
            .map(AccountSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal accountsChange =
        allAccountSales.stream()
            .map(AccountSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getChange)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal cancelPayed =
        allCancelEvents.stream()
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal currentAmount =
        ticketsPayed
            .add(accountsPayed)
            .subtract(ticketsChange)
            .subtract(accountsChange)
            .subtract(cancelPayed)
            .subtract(retired);

    CashCheckpoint checkpoint = new CashCheckpoint();
    checkpoint.setSalesShift(saleShift);
    checkpoint.setPreviousAmount(currentAmount);
    checkpoint.setNewAmount(currentAmount.subtract(BigDecimal.valueOf(amount)));
    salesTerminal.setCurrentAmount(checkpoint.getNewAmount());

    salesTerminalRepository.save(salesTerminal);
    cashCheckpointRepository.save(checkpoint);

    cancelEvents.forEach(
        event -> {
          event.setCashCheckpoint(checkpoint);
          cancelEventRepository.save(event);
        });

    sales.forEach(
        sale -> {
          sale.setCashCheckpoint(checkpoint);
          internetSaleRepository.save(sale);
        });

    accountSales.forEach(
        sale -> {
          sale.setCashCheckpoint(checkpoint);
          accountSaleRepository.save(sale);
        });

    SalesBoothData data = mapper.map(salesTerminal, SalesBoothData.class);
    Optional.ofNullable(salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false))
        .ifPresent(
            salesShift -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(salesShift.getUser().getName())
                      .append(" ")
                      .append(salesShift.getUser().getLastName())
                      .append(" ")
                      .append(salesShift.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(salesShift.getDateCreated());
            });
    return data;
  }

  public InputStream closeShift(String terminalId, String timeZone) throws Exception {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    SalesShift shift = salesShiftRepository.findLatestBySalesTerminalId(salesTerminal.getId());
    Map<String, Object> params = new HashMap<>();

    List<CashCheckpoint> checkpoints = cashCheckpointRepository.findBySalesShift(shift);

    CashCheckpoint latest =
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

    String chiefName = "";

    String salesman =
        new StringBuilder()
            .append(Optional.ofNullable(shift.getUser().getName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getLastName()).orElse(""))
            .append(" ")
            .append(Optional.ofNullable(shift.getUser().getSecondLastName()).orElse(""))
            .toString();

    List<CancelEvent> cancelEvents = cancelEventRepository.findAllBySaleShift(shift);

    List<InternetSale> sales =
        checkpoints.stream()
            .flatMap(
                checkpoint -> internetSaleRepository.findAllByCashCheckpoint(checkpoint).stream())
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());

    List<AccountSale> accountSales =
        checkpoints.stream()
            .flatMap(
                checkpoint -> accountSaleRepository.findAllByCashCheckpoint(checkpoint).stream())
            .collect(Collectors.toList());

    List<PaymentPart> parts = new LinkedList<>();

    parts.addAll(
        sales.stream()
            .flatMap(sale -> sale.getPaymentParts().stream())
            .collect(Collectors.toList()));

    parts.addAll(
        accountSales.stream()
            .flatMap(sale -> sale.getPaymentParts().stream())
            .collect(Collectors.toList()));

    BigDecimal totalSum =
        parts.stream()
            .map(
                part ->
                    part.getAmount()
                        .subtract(Optional.ofNullable(part.getChange()).orElse(BigDecimal.ZERO)))
            .reduce((acc, curr) -> acc.add(curr))
            .orElse(BigDecimal.ZERO);

    BigDecimal cashTotal =
        cancelEvents.stream()
            .filter(event -> event.getPaymentType() == PaymentPartType.CASH)
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal accountTotal =
        cancelEvents.stream()
            .filter(event -> event.getPaymentType() == PaymentPartType.ACCOUNT)
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Map<PaymentPartType, List<PaymentPart>> groupedParts =
        parts.stream().collect(Collectors.groupingBy(PaymentPart::getPaymentType));

    Map<PaymentPartType, BigDecimal> groupedAmounts = new HashMap<>();

    groupedParts.forEach(
        (type, paymentParts) -> {
          BigDecimal sum =
              paymentParts.stream()
                  .map(
                      part ->
                          part.getAmount()
                              .subtract(
                                  Optional.ofNullable(part.getChange()).orElse(BigDecimal.ZERO)))
                  .reduce((acc, curr) -> acc.add(curr))
                  .get();
          groupedAmounts.put(type, sum);
        });

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    List<PaymentPart> totalParts = new LinkedList<>();

    totalParts.addAll(
        internetSaleRepository.findAllBySalesShift(shift).stream()
            .flatMap(sale -> sale.getPaymentParts().stream())
            .collect(Collectors.toList()));

    totalParts.addAll(
        accountSaleRepository.findAllBySalesShift(shift).stream()
            .flatMap(sale -> sale.getPaymentParts().stream())
            .collect(Collectors.toList()));

    BigDecimal totalAmount =
        totalParts.stream()
            .map(
                part ->
                    part.getAmount()
                        .subtract(Optional.ofNullable(part.getChange()).orElse(BigDecimal.ZERO)))
            .reduce((acc, curr) -> acc.add(curr))
            .orElse(BigDecimal.ZERO);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    params.put("Oficina", shift.getSalesTerminal().getOfficeLocation().getName());
    params.put("NoTaquilla", shift.getSalesTerminal().getTerminalName());
    params.put("FechaHoy", dateFormatter.print(new DateTime(latest.getDateCreated())));
    params.put("Hora", timeFormatter.print(new DateTime(latest.getDateCreated())));
    params.put(
        "VentaEfectivo",
        "$" + format.format(groupedAmounts.getOrDefault(PaymentPartType.CASH, BigDecimal.ZERO)));
    params.put(
        "VentaTC",
        "$"
            + format.format(
                groupedAmounts.getOrDefault(PaymentPartType.CREDIT_CARD, BigDecimal.ZERO)));
    params.put(
        "VentaME",
        "$" + format.format(groupedAmounts.getOrDefault(PaymentPartType.ACCOUNT, BigDecimal.ZERO)));
    params.put(
        "VentaDep",
        "$"
            + format.format(
                groupedAmounts.getOrDefault(PaymentPartType.TRANSFER, BigDecimal.ZERO)));
    params.put("InicioCaja", "$" + format.format(shift.getStartingAmount()));
    params.put(
        "Retiro", "$ " + format.format(latest.getPreviousAmount().subtract(latest.getNewAmount())));
    params.put("Resta", "$ " + format.format(latest.getNewAmount()));
    params.put("VentaPrecortes", "$ " + format.format(totalSum));
    params.put("TotalVenta", "$ " + format.format(totalAmount));
    params.put("JefaOficina", chiefName);
    params.put("Taquillera", salesman);
    params.put("FolioCorte", shift.getShiftNumber());
    params.put("CancelacionesEf", "$ " + format.format(cashTotal));
    params.put("CancelacionesME", "$ " + format.format(accountTotal));

    List<Map<String, ?>> data =
        checkpoints.stream()
            .map(
                checkpoint -> {
                  Map<String, Object> row = new HashMap<>();
                  row.put(
                      "FechaPrecorte",
                      dateFormatter.print(new DateTime(checkpoint.getDateCreated())));
                  row.put(
                      "HoraPrecorte",
                      timeFormatter.print(new DateTime(checkpoint.getDateCreated())));
                  row.put(
                      "RetiroPrecorte",
                      "$ "
                          + format.format(
                              checkpoint.getPreviousAmount().subtract(checkpoint.getNewAmount())));
                  row.put("RestaPrecorte", "$ " + format.format(checkpoint.getNewAmount()));
                  return row;
                })
            .collect(Collectors.toList());

    return new ByteArrayInputStream(
        jasperService.getPdfBytes("CorteCaja", params, new JRMapCollectionDataSource(data)));
  }

  public SalesBoothData closeShift(String terminalId) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(terminalId);
    SalesShift saleShift =
        salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false);

    List<CashCheckpoint> allCheckpoints = cashCheckpointRepository.findBySalesShift(saleShift);

    BigDecimal previousCheckpointSum =
        allCheckpoints.stream()
            .map(CashCheckpoint::getPreviousAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal newCheckpointSum =
        allCheckpoints.stream()
            .map(CashCheckpoint::getNewAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal retired = previousCheckpointSum.subtract(newCheckpointSum);

    List<InternetSale> sales =
        internetSaleRepository.findAllBySalesTerminalAndCashCheckpointIsNull(salesTerminal).stream()
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());
    List<CancelEvent> cancelEvents =
        cancelEventRepository.findAllBySaleShiftAndCashCheckpointIsNull(saleShift);
    List<AccountSale> accountSales =
        accountSaleRepository.findAllBySalesTerminalAndCashCheckpointIsNull(salesTerminal);

    List<InternetSale> allSales =
        internetSaleRepository.findAllBySalesShift(saleShift).stream()
            .filter(sale -> sale.getPayed())
            .collect(Collectors.toList());

    List<AccountSale> allAccountSales = accountSaleRepository.findAllBySalesShift(saleShift);
    List<CancelEvent> allCancelEvents = cancelEventRepository.findAllBySaleShift(saleShift);

    BigDecimal ticketsPayed =
        allSales.stream()
            .map(InternetSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ticketsChange =
        allSales.stream()
            .map(InternetSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getChange)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal accountsPayed =
        allAccountSales.stream()
            .map(AccountSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal accountsChange =
        allAccountSales.stream()
            .map(AccountSale::getPaymentParts)
            .flatMap(List::stream)
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(PaymentPart::getChange)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal cancelPayed =
        allCancelEvents.stream()
            .filter(part -> part.getPaymentType() == PaymentPartType.CASH)
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal currentAmount =
        ticketsPayed
            .add(accountsPayed)
            .subtract(ticketsChange)
            .subtract(accountsChange)
            .subtract(cancelPayed)
            .subtract(retired);

    CashCheckpoint checkpoint = new CashCheckpoint();
    checkpoint.setSalesShift(saleShift);
    checkpoint.setPreviousAmount(currentAmount);
    checkpoint.setNewAmount(BigDecimal.ZERO);
    salesTerminal.setCurrentAmount(BigDecimal.ZERO);

    salesTerminalRepository.save(salesTerminal);
    cashCheckpointRepository.save(checkpoint);

    cancelEvents.forEach(
        event -> {
          event.setCashCheckpoint(checkpoint);
          cancelEventRepository.save(event);
        });

    sales.forEach(
        sale -> {
          sale.setCashCheckpoint(checkpoint);
          internetSaleRepository.save(sale);
        });

    accountSales.forEach(
        sale -> {
          sale.setCashCheckpoint(checkpoint);
          accountSaleRepository.save(sale);
        });

    saleShift.setFinished(true);

    salesShiftRepository.save(saleShift);

    SalesBoothData data = mapper.map(salesTerminal, SalesBoothData.class);
    data.setLatestShiftId(saleShift.getId());
    Optional.ofNullable(salesShiftRepository.findBySalesTerminalAndFinished(salesTerminal, false))
        .ifPresent(
            salesShift -> {
              StringBuilder nameBuilder = new StringBuilder();
              data.setSalesPersonName(
                  nameBuilder
                      .append(salesShift.getUser().getName())
                      .append(" ")
                      .append(salesShift.getUser().getLastName())
                      .append(" ")
                      .append(salesShift.getUser().getSecondLastName())
                      .toString());
              data.setStartDate(salesShift.getDateCreated());
            });
    return data;
  }

  public InputStream getPersonDetail(String personId, String timeZone, Date from, Date to)
      throws Exception {

    final DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.UTC);

    User user = userRepository.findOne(personId);
    SalesShift lastShift = salesShiftRepository.findLatestByUser(personId);
    List<TripSeat> seats =
        tripSeatRepository.findAllByUserAndDateCreatedBetween(user, from, to).stream()
            .filter(seat -> seat.getInternetSale().getPayed())
            .collect(Collectors.toList());
    List<CancelEvent> cancelEvents =
        cancelEventRepository.findAllByCancelUserAndDateCreatedBetween(user, from, to);
    List<PackageTicket> packageTickets =
        packageTicketRepository.findAllByUserAndDateCreatedBetween(user, from, to);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
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

    BigDecimal cancelTotal =
        cancelEvents.stream()
            .map(CancelEvent::getSoldPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ticketTotal =
        seats.stream().map(TripSeat::getSoldPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal baggageTotal =
        packageTickets.stream()
            .map(PackageTicket::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal total = ticketTotal.add(baggageTotal);

    Map<String, Object> params = new HashMap();
    params.put(
        "Oficina",
        Optional.ofNullable(lastShift)
            .map(SalesShift::getSalesTerminal)
            .map(SalesTerminal::getOfficeLocation)
            .map(OfficeLocation::getName)
            .orElse(""));
    params.put("Fecha", dateTimeFormatter.print(new DateTime()));
    params.put("Taquillera", salesman);
    params.put("Desde", dateTimeFormatter.print(new DateTime(from)));
    params.put("Hasta", dateTimeFormatter.print(new DateTime(to)));
    params.put("TFolios", String.valueOf(seats.size() + packageTickets.size()));
    params.put("TImporte", format.format(total));
    params.put("TCancelaciones", format.format(cancelTotal));

    List<Object> allElements = new LinkedList<>(seats);

    allElements.addAll(packageTickets);
    allElements.addAll(cancelEvents);

    List<Map<String, ?>> rows =
        allElements.stream()
            .sorted(
                (e1, e2) -> {
                  Date date1;
                  Date date2;
                  if (e1 instanceof TripSeat) {
                    date1 = ((TripSeat) e1).getDateCreated();
                  } else if (e1 instanceof PackageTicket) {
                    date1 = ((PackageTicket) e1).getDateCreated();
                  } else if (e1 instanceof CancelEvent) {
                    date1 = ((CancelEvent) e1).getDateCreated();
                  } else {
                    return 0;
                  }
                  if (e2 instanceof TripSeat) {
                    date2 = ((TripSeat) e2).getDateCreated();
                  } else if (e2 instanceof PackageTicket) {
                    date2 = ((PackageTicket) e2).getDateCreated();
                  } else if (e2 instanceof CancelEvent) {
                    date2 = ((CancelEvent) e2).getDateCreated();
                  } else {
                    return 0;
                  }
                  return date1.compareTo(date2);
                })
            .map(
                element -> {
                  if (element instanceof TripSeat) {
                    return mapDetail((TripSeat) element, zone);
                  } else if (element instanceof PackageTicket) {
                    return mapDetail((PackageTicket) element, zone);
                  } else if (element instanceof CancelEvent) {
                    return mapDetail((CancelEvent) element, zone);
                  } else {
                    return null;
                  }
                })
            .filter(element -> element != null)
            .collect(Collectors.toList());

    JRDataSource ds = new JRMapCollectionDataSource(rows);

    return new ByteArrayInputStream(jasperService.getPdfBytes("DetalleVenta", params, ds));
  }

  public Map<String, Object> mapDetail(CancelEvent cancelEvent, DateTimeZone zone) {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");
    Map<String, Object> detail = new HashMap<>();

    BigDecimal price = cancelEvent.getSoldPrice();
    String personTypeLabel;

    if (cancelEvent.getPassengerType() == PassengerType.ADULT) {
      personTypeLabel = "Adulto";
    } else if (cancelEvent.getPassengerType() == PassengerType.CHILD) {
      personTypeLabel = "Niño";
    } else if (cancelEvent.getPassengerType() == PassengerType.INFANT) {
      personTypeLabel = "Infante";
    } else if (cancelEvent.getPassengerType() == PassengerType.OLDER_ADULT) {
      personTypeLabel = "Adulto mayor";
    } else if (cancelEvent.getPassengerType() == PassengerType.STUDENT) {
      personTypeLabel = "Estudiante";
    } else {
      personTypeLabel = "";
    }

    detail.put("Folio", "(Cancelación) " + cancelEvent.getTicketId());
    detail.put("Hora", dateTimeFormatter.print(new DateTime(cancelEvent.getDateCreated())));
    detail.put("Origen", cancelEvent.getStartingStop().getName());
    detail.put("Destino", cancelEvent.getEndingStop().getName());
    detail.put("Tarifa", personTypeLabel);
    detail.put(
        "Precio", format.format(cancelEvent.getSoldPrice().multiply(BigDecimal.valueOf(-1))));

    return detail;
  }

  public Map<String, Object> mapDetail(TripSeat seat, DateTimeZone zone) {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");
    Map<String, Object> detail = new HashMap<>();

    BigDecimal price = seat.getSoldPrice();
    String personTypeLabel;

    if (seat.getPassengerType() == PassengerType.ADULT) {
      personTypeLabel = "Adulto";
    } else if (seat.getPassengerType() == PassengerType.CHILD) {
      personTypeLabel = "Niño";
    } else if (seat.getPassengerType() == PassengerType.INFANT) {
      personTypeLabel = "Infante";
    } else if (seat.getPassengerType() == PassengerType.OLDER_ADULT) {
      personTypeLabel = "Adulto mayor";
    } else if (seat.getPassengerType() == PassengerType.STUDENT) {
      personTypeLabel = "Estudiante";
    } else {
      personTypeLabel = "";
    }

    detail.put("Folio", seat.getTicketId());
    detail.put("Hora", dateTimeFormatter.print(new DateTime(seat.getDateCreated())));
    detail.put("Origen", seat.getStartingStop().getName());
    detail.put("Destino", seat.getEndingStop().getName());
    detail.put("Tarifa", personTypeLabel);
    detail.put("Precio", format.format(seat.getSoldPrice()));

    return detail;
  }

  public Map<String, Object> mapDetail(PackageTicket ticket, DateTimeZone zone) {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DecimalFormat format = new DecimalFormat("#,###.##");

    Map<String, Object> detail = new HashMap<>();

    detail.put("Folio", ticket.getId());
    detail.put("Hora", dateTimeFormatter.print(new DateTime(ticket.getDateCreated())));
    detail.put("Origen", ticket.getStartingStop().getName());
    detail.put("Destino", ticket.getEndingStop().getName());
    detail.put("Tarifa", "Equipaje");
    detail.put("Precio", format.format(ticket.getPrice()));

    return detail;
  }

  public SalesTerminalData getStatus() {
    User currentUser = userService.getCurrentUser();
    SalesShift currentShift = salesShiftRepository.findByUserAndFinished(currentUser, false);

    if (currentShift == null) {
      return null;
    }

    return mapTerminal(currentShift.getSalesTerminal());
  }

  public List<UserData> findPeople(String salesTerminalId, String startDate, String endDate) {
    SalesTerminal salesTerminal = salesTerminalRepository.findByTerminalId(salesTerminalId);
    OfficeLocation officeLocation = salesTerminal.getOfficeLocation();
    Date startingDate = new DateTime(startDate).toDate();
    Date endingDate = new DateTime(endDate).toDate();
    return salesShiftRepository
        .findAllBySalesTerminalOfficeLocationAndDateCreatedBetween(
            officeLocation, startingDate, endingDate)
        .stream()
        .map(SalesShift::getUser)
        .map(user -> mapper.map(user, UserData.class))
        .distinct()
        .collect(Collectors.toList());
  }

  public List<OfficeLocationData> listOffices() {
    return StreamSupport.stream(officeLocationRepository.findAll().spliterator(), false)
        .map(
            office ->
                OfficeLocationData.builder()
                    .address(office.getAddress())
                    .name(office.getName())
                    .latitude(office.getLatitude())
                    .longitude(office.getLongitude())
                    .build())
        .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
        .collect(Collectors.toList());
  }
}
