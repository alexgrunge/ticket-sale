package justcloud.tickets.service;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.sendgrid.SendGrid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Base64;
import java.util.function.Function;
import java.util.stream.Collectors;
import justcloud.facturemosya.RespuestaTXT;
import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.Parameter;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.repository.*;
import justcloud.tickets.domain.sales.*;
import justcloud.tickets.domain.sales.BillingData;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.dto.*;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.service.sales.ComproPagoPaymentService;
import justcloud.tickets.service.sales.MastercardGatewayPaymentService;
import justcloud.tickets.service.sales.PaypalPaymentService;
import justcloud.tickets.util.TimeZoneUtils;
import lombok.Builder;
import lombok.Data;
import ma.glasnost.orika.MapperFacade;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaleService {

  private Logger log = LoggerFactory.getLogger(SaleService.class);
  private static final float DISCOUNT = 0.7f;

  @Autowired private MapperFacade mapper;

  @Autowired private UserService userService;

  @Autowired private ParameterRepository parameterRepository;

  @Autowired private AccountSaleRepository accountSaleRepository;

  @Autowired private OfficeLocationRepository officeLocationRepository;

  @Autowired private CancelEventRepository cancelEventRepository;

  @Autowired private CancelPackageEventRepository cancelPackageEventRepository;

  @Autowired private PaymentPartRepository paymentPartRepository;

  @Autowired private ClientAccountRepository clientAccountRepository;

  @Autowired private BillingAddressRepository billingAddressRepository;

  @Autowired private BillingDataRepository billingDataRepository;

  @Autowired private PackageTicketRepository packageTicketRepository;

  @Value("${sendgrid.from}")
  private String fromMail;

  @Value("${maximum.students}")
  private Long maxStudents;

  @Value("${maximum.olderAdults}")
  private Long maxOlderAdults;

  @Value("${maximum.reserved}")
  private Long maxReserved;

  @Autowired private Gson gson;

  @Autowired private SendGrid sendgrid;

  @Autowired private TemplateService templateService;

  @Autowired private JasperService jasperService;

  @Autowired private SalesTerminalRepository salesTerminalRepository;

  @Autowired private IdGenerationService idGenerationService;

  @Autowired private InternetSaleRepository internetSaleRepository;

  @Autowired private StopOffRepository stopOffRepository;

  @Autowired private TripSeatRepository tripSeatRepository;

  @Autowired private SegmentVarRepository segmentVaRepository;

  @Autowired private PaypalPaymentService paypalPaymentService;

  @Autowired private ComproPagoPaymentService comproPagoPaymentService;

  @Autowired private TripRepository tripRepository;

  @Autowired private FacturemosYaInvoiceService facturemosYaInvoiceService;

  @Autowired private SalesShiftRepository salesShiftRepository;

  @Autowired private Map<String, PaymentService> paymentServices;

  @Autowired private MastercardGatewayPaymentService mastercardGatewayPaymentService;

  public AccountData findAccount(String id) {
    return mapper.map(clientAccountRepository.findOne(id), AccountData.class);
  }

  public AccountData findAccountByNumber(String number) {
    return mapper.map(clientAccountRepository.findByName(number), AccountData.class);
  }

  public Map<String, Object> findPackageSaleData(String shortId) {
    InternetSale internetSale =
        Optional.ofNullable(internetSaleRepository.findByShortId(shortId))
            .orElseGet(
                () -> {
                  return Optional.ofNullable(packageTicketRepository.findOne(shortId))
                      .map(PackageTicket::getSale)
                      .orElseGet(
                          () -> {
                            return Optional.ofNullable(
                                    packageTicketRepository.findByTicketId(shortId))
                                .map(PackageTicket::getSale)
                                .orElse(null);
                          });
                });

    if (internetSale == null) {
      log.info("No info found for id {}", shortId);
      return null;
    }

    Map<String, Object> data = new HashMap<>();
    if (internetSale.getFullResponse() != null) {
      data.put("payment", gson.fromJson(internetSale.getFullResponse(), Map.class));
    }
    data.put("payed", internetSale.getPayed());
    data.put("bill", internetSale.getBill());
    data.put("shortId", internetSale.getShortId());
    data.put("id", internetSale.getId());
    data.put("paymentProvider", internetSale.getPaymentProvider());

    PackageTicket packageTicket = packageTicketRepository.findBySale(internetSale);

    data.put(
        "package",
        new HashMap<String, Object>() {
          {
            put("id", packageTicket.getId());
            put("concept", packageTicket.getConcept());
            put("contactData", packageTicket.getContactData());
            put("dateCreated", packageTicket.getDateCreated());
            put("lastUpdated", packageTicket.getLastUpdated());
            put("paymentPrice", packageTicket.getPaymentPrice());
            put("price", packageTicket.getPrice());
            put("receiverName", packageTicket.getReceiverName());
            put("senderName", packageTicket.getSenderName());
            put("startingStop", packageTicket.getStartingStop().getName());
            put("endingStop", packageTicket.getEndingStop().getName());
          }
        });

    return data;
  }

  public Map<String, Object> findSaleData(String shortId) {
    InternetSale internetSale =
        Optional.ofNullable(internetSaleRepository.findByShortId(shortId))
            .orElseGet(
                () -> {
                  return Optional.ofNullable(tripSeatRepository.findByTicketId(shortId))
                      .map(TripSeat::getInternetSale)
                      .orElse(null);
                });

    if (internetSale == null) {
      log.info("No info found for id {}", shortId);
      return null;
    }

    if (!internetSale.getPayed() && "mastercardGateway".equals(internetSale.getPaymentProvider())) {
      try {
        String result = mastercardGatewayPaymentService.attemptApproval(shortId);
        if ("CAPTURED".equals(result)) {
          approveSaleInternal(internetSale);
        } else if ("FAILED".equals(result)) {
          cancelSaleInternal(internetSale);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    List<TripSeat> seats = tripSeatRepository.findAllByInternetSale(internetSale);

    Map<String, Object> data = new HashMap<>();
    if (internetSale.getFullResponse() != null) {
      data.put("payment", gson.fromJson(internetSale.getFullResponse(), Map.class));
    }
    data.put("payed", internetSale.getPayed());
    data.put("bill", internetSale.getBill());
    data.put("shortId", internetSale.getShortId());
    data.put("id", internetSale.getId());
    data.put("paymentProvider", internetSale.getPaymentProvider());
    data.put(
        "tripId", seats.stream().map(TripSeat::getTrip).findFirst().map(Trip::getId).orElse(null));

    List<Map<String, Object>> seatData =
        seats.stream()
            .sorted(
                (a, b) -> {
                  return a.getStartingStop().getName().compareTo(b.getStartingStop().getName());
                })
            .map(
                (seat) -> {
                  Map<String, Object> d = new HashMap<>();
                  d.put("name", seat.getPassengerName());
                  d.put("ticketId", seat.getTicketId());
                  d.put("seat", seat.getSeatName());
                  d.put("status", seat.getStatus().toString());
                  d.put("comments", seat.getComments());
                  d.put("passengerType", seat.getPassengerType());
                  d.put("soldPrice", seat.getSoldPrice());
                  d.put("startingStop", seat.getStartingStop().getName());
                  d.put("endingStop", seat.getEndingStop().getName());
                  return d;
                })
            .collect(Collectors.toList());

    data.put("tickets", seatData);

    return data;
  }

  public InputStream getXmlBill(String shortId) {
    InternetSale internetSale =
        Optional.ofNullable(internetSaleRepository.findByShortId(shortId))
            .orElseGet(
                () -> {
                  TripSeat tripSeat = tripSeatRepository.findByTicketId(shortId);
                  return tripSeat.getInternetSale();
                });

    byte[] bytes = Optional.ofNullable(internetSale.getBillXml()).orElse("").getBytes();

    return new ByteArrayInputStream(bytes);
  }

  public InputStream getBill(String shortId) {
    InternetSale internetSale =
        Optional.ofNullable(internetSaleRepository.findByShortId(shortId))
            .orElseGet(
                () -> {
                  TripSeat tripSeat = tripSeatRepository.findByTicketId(shortId);
                  return tripSeat.getInternetSale();
                });

    byte[] bytes = Base64.getDecoder().decode(internetSale.getBillPdf());

    return new ByteArrayInputStream(bytes);
  }

  public InputStream getBaggageTicket(String ticketId, String timeZone) throws Exception {
    InternetSale internetSale = internetSaleRepository.findByShortId(ticketId);
    PackageTicket packageTicket = packageTicketRepository.findBySale(internetSale);

    byte[] ticketBytes = generateBaggageTicket(packageTicket, timeZone);

    return new ByteArrayInputStream(ticketBytes);
  }

  public InputStream getTicket(String ticketId, String timeZone) throws Exception {
    TripSeat seat = tripSeatRepository.findByTicketId(ticketId);

    byte[] ticketBytes = generateTicket(seat, timeZone);

    return new ByteArrayInputStream(ticketBytes);
  }

  public SaleResponse confirmPaypal(String paymentId, String token, String payerId)
      throws Exception {
    InternetSale internetSale =
        internetSaleRepository.findByPaymentIdAndPaymentProvider(paymentId, "PAYPAL");
    SaleResponse saleResponse = new SaleResponse();
    saleResponse.setId(internetSale.getId());
    saleResponse.setShortId(internetSale.getShortId());
    PaymentResponseData response = paypalPaymentService.confirmPayment(paymentId, token, payerId);

    if (response.isAuthorized()) {
      approveSale(internetSale.getId());
    }

    saleResponse.setPayment(response);

    return saleResponse;
  }

  public SaleResponse confirmVisa(Map<String, Object> confirmation) throws Exception {
    log.info("Visa response {}", confirmation);
    Map<String, Object> order =
        (Map<String, Object>) confirmation.getOrDefault("order", new HashMap<String, Object>());
    String id = (String) order.getOrDefault("id", "");
    String result = (String) confirmation.getOrDefault("status", "");
    InternetSale internetSale = internetSaleRepository.findByShortId(id);

    if (internetSale == null) {
      return null;
    }

    SaleResponse saleResponse = new SaleResponse();
    saleResponse.setId(internetSale.getId());
    saleResponse.setShortId(internetSale.getShortId());

    MastercardGatewayPaymentResponse response = new MastercardGatewayPaymentResponse();
    response.setServerResponse(confirmation);

    saleResponse.setPayment(response);

    if ("CAPTURED".equals(result)) {
      approveSale(internetSale.getId());
    } else if ("FAILED".equals(result)) {
      cancelSale(internetSale.getId());
    }

    return saleResponse;
  }

  public SaleResponse confirmCompropago(Map<String, Object> confirmation) throws Exception {
    Set<String> cancelStatuses = new HashSet<>();
    cancelStatuses.add("charge.expired");
    cancelStatuses.add("charge.declined");

    log.debug("Got a confirmation {}", confirmation);

    String id = confirmation.get("id").toString();
    String event = confirmation.get("type").toString();
    InternetSale internetSale =
        internetSaleRepository.findByPaymentIdAndPaymentProvider(id, "COMPROPAGO");
    SaleResponse saleResponse = new SaleResponse();

    if (internetSale != null) {
      if (cancelStatuses.contains(event)) {
        cancelSale(internetSale.getId());
      } else if (!"charge.pending".equals(event)) {
        PaymentResponseData payment = comproPagoPaymentService.confirmPayment(confirmation);
        saleResponse.setPayment(payment);
        saleResponse.setId(internetSale.getId());

        if (payment.isAuthorized()) {
          approveSale(internetSale.getId());
        }
      }
    }

    return saleResponse;
  }

  public Map<String, Object> cancelTicket(
      String ticketId, String clientAccountId, String clientPhone, boolean returnMoney) {
    TripSeat seat = tripSeatRepository.findByTicketId(ticketId);
    String name = seat.getPassengerName();

    User cancelUser = userService.getCurrentUser();
    SalesShift salesShift = salesShiftRepository.findLatestByUser(cancelUser.getId());

    if (salesShift == null) {
      throw new RuntimeException("There is no sales shift");
    }

    if (salesShift.getFinished()) {
      throw new RuntimeException("The sale shift has already ended " + salesShift.getId());
    }

    Map<String, Object> result = new HashMap<>();

    if (seat.getStatus() == SeatStatus.USED) {
      return result;
    }

    CancelEvent cancelEvent = new CancelEvent();
    cancelEvent.setCancelUser(cancelUser);
    cancelEvent.setSaleShift(salesShift);
    cancelEvent.setSeat(seat.getSeat());
    cancelEvent.setStartingStop(seat.getStartingStop());
    cancelEvent.setEndingStop(seat.getEndingStop());
    cancelEvent.setPassengerType(seat.getPassengerType());
    cancelEvent.setSoldPrice(seat.getSoldPrice());
    cancelEvent.setInternetSale(seat.getInternetSale());
    cancelEvent.setTicketId(seat.getTicketId());
    cancelEvent.setSeatName(seat.getSeatName());
    cancelEvent.setPassengerName(seat.getPassengerName());
    cancelEvent.setTrip(seat.getTrip());
    cancelEvent.setOriginalDate(seat.getDateCreated());

    if (!returnMoney) {
      ClientAccount clientAccount =
          Optional.ofNullable(clientAccountId)
              .map(id -> clientAccountRepository.findOne(id))
              .orElse(new ClientAccount());

      cancelEvent.setAccount(clientAccount);
      cancelEvent.setPaymentType(PaymentPartType.ACCOUNT);

      if (clientAccount.getId() == null) {
        clientAccount.setId(clientAccountId);
        clientAccount.setName(clientPhone);
      }

      if (clientAccount.getAmount() == null) {
        clientAccount.setAmount(BigDecimal.ZERO);
      }

      SegmentVar price =
          segmentVaRepository.findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
              seat.getStartingStop().getId(),
              seat.getEndingStop().getId(),
              "price",
              seat.getTrip().getServiceLevelType());

      clientAccount.setAmount(price.getNumericVar().add(clientAccount.getAmount()));
      clientAccountRepository.save(clientAccount);
      result.put("id", clientAccount.getId());
    } else {
      cancelEvent.setPaymentType(PaymentPartType.CASH);
      SegmentVar price =
          segmentVaRepository.findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
              seat.getStartingStop().getId(),
              seat.getEndingStop().getId(),
              "price",
              seat.getTrip().getServiceLevelType());
      SalesShift currentShift =
          salesShiftRepository.findByUserAndFinished(userService.getCurrentUser(), false);
      if (currentShift != null && currentShift.getSalesTerminal() != null) {
        currentShift
            .getSalesTerminal()
            .setCurrentAmount(
                currentShift.getSalesTerminal().getCurrentAmount().subtract(price.getNumericVar()));
        salesTerminalRepository.save(currentShift.getSalesTerminal());
      }
    }
    seat.getTrip().getSeats().remove(seat);
    tripRepository.save(seat.getTrip());

    cancelEventRepository.save(cancelEvent);

    tripSeatRepository.delete(seat);

    return result;
  }

  public Map<String, Object> cancelPackageTicket(
      String ticketId, String clientAccountId, String clientPhone, boolean returnMoney) {
    PackageTicket packageTicket = packageTicketRepository.findOne(ticketId);

    User cancelUser = userService.getCurrentUser();
    SalesShift salesShift = salesShiftRepository.findLatestByUser(cancelUser.getId());

    if (salesShift == null) {
      throw new RuntimeException("There is no sales shift");
    }

    if (salesShift.getFinished()) {
      throw new RuntimeException("The sale shift has already ended " + salesShift.getId());
    }

    Map<String, Object> result = new HashMap<>();

    CancelPackageEvent cancelEvent = new CancelPackageEvent();
    cancelEvent.setCancelUser(cancelUser);
    cancelEvent.setSaleShift(salesShift);
    cancelEvent.setStartingStop(packageTicket.getStartingStop());
    cancelEvent.setEndingStop(packageTicket.getEndingStop());
    cancelEvent.setSoldPrice(packageTicket.getPrice());
    cancelEvent.setInternetSale(packageTicket.getSale());
    cancelEvent.setTrip(packageTicket.getTrip());
    cancelEvent.setOriginalDate(packageTicket.getDateCreated());
    cancelEvent.setStartingStop(packageTicket.getStartingStop());
    cancelEvent.setEndingStop(packageTicket.getEndingStop());

    if (!returnMoney) {
      ClientAccount clientAccount =
          Optional.ofNullable(clientAccountId)
              .map(id -> clientAccountRepository.findOne(id))
              .orElse(new ClientAccount());

      cancelEvent.setAccount(clientAccount);
      cancelEvent.setPaymentType(PaymentPartType.ACCOUNT);

      if (clientAccount.getAmount() == null) {
        clientAccount.setAmount(BigDecimal.ZERO);
      }

      clientAccount.setAmount(cancelEvent.getSoldPrice().add(clientAccount.getAmount()));
      if (clientAccount.getId() == null) {
        clientAccount.setId(clientAccountId);
        clientAccount.setName(clientPhone);
      }
      clientAccountRepository.save(clientAccount);
      result.put("id", clientAccount.getId());
    } else {
      cancelEvent.setPaymentType(PaymentPartType.CASH);
      SalesShift currentShift =
          salesShiftRepository.findByUserAndFinished(userService.getCurrentUser(), false);
      if (currentShift != null && currentShift.getSalesTerminal() != null) {
        currentShift
            .getSalesTerminal()
            .setCurrentAmount(
                currentShift
                    .getSalesTerminal()
                    .getCurrentAmount()
                    .subtract(packageTicket.getPaymentPrice()));
        salesTerminalRepository.save(currentShift.getSalesTerminal());
      }
    }

    cancelPackageEventRepository.save(cancelEvent);

    packageTicketRepository.delete(packageTicket);

    return result;
  }

  public List<Map<String, Object>> findPackageSaleDataByName(String name) {
    return packageTicketRepository.findAllBySenderNameContaining(name).stream()
        .map(PackageTicket::getSale)
        .distinct()
        .map(InternetSale::getShortId)
        .map(this::findPackageSaleData)
        .collect(Collectors.toList());
  }

  public List<Map<String, Object>> findSaleDataByName(String name) {
    return tripSeatRepository.findAllByPassengerNameContaining(name).stream()
        .map(TripSeat::getInternetSale)
        .distinct()
        .map(InternetSale::getShortId)
        .map(this::findSaleData)
        .collect(Collectors.toList());
  }

  public AccountSaleResponse buyAccount(AccountSaleRequest saleRequest) throws Exception {
    AccountSale clientAccountSale = new AccountSale();

    BigDecimal amount =
        saleRequest.getPaymentParts().stream()
            .map(PaymentPartData::getAmount)
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

    InternetSale internetSale = new InternetSale();
    internetSale.setShortId(idGenerationService.generateId());
    internetSale.setPayed(false);
    internetSale.setTimeZone("America/Mexico_City");
    internetSale.setEmail("accounts@turismoenomnibus.com.mx");
    internetSale.setPayed(true);

    if (userService.getCurrentUser() != null) {
      internetSale.setSalesman(userService.getCurrentUser());
    }
    internetSale.setTotalAmount(amount);
    internetSale.setChangeAmount(BigDecimal.ZERO);
    internetSale.setPayed(true);

    internetSaleRepository.save(internetSale);

    clientAccountSale.setInternetSale(internetSale);

    SalesTerminal terminal;

    if (saleRequest.getTerminalId() != null) {
      terminal = salesTerminalRepository.findOne(saleRequest.getTerminalId());
      clientAccountSale.setSalesTerminal(terminal);
      SalesShift shift = salesShiftRepository.findBySalesTerminalAndFinished(terminal, false);
      clientAccountSale.setSalesShift(shift);
    } else {
      terminal = null;
    }

    ClientAccount clientAccount =
        Optional.ofNullable(clientAccountRepository.findOne(saleRequest.getCardNumber()))
            .orElse(new ClientAccount());
    clientAccount.setId(saleRequest.getCardNumber());
    clientAccount.setName(saleRequest.getName());

    if (clientAccount.getAmount() == null) {
      clientAccount.setAmount(BigDecimal.ZERO);
    }

    clientAccount.setAmount(amount.add(clientAccount.getAmount()));

    clientAccountRepository.save(clientAccount);
    clientAccountSale.setClientAccount(clientAccount);

    log.debug("Payment parts {}", saleRequest.getPaymentParts());
    if (saleRequest.getPaymentParts() != null) {
      int cashParts =
          saleRequest.getPaymentParts().stream()
              .filter(part -> "CASH".equals(part.getPaymentType()))
              .collect(Collectors.toList())
              .size();
      if (cashParts > 1) {
        throw new RuntimeException("Cannot pay more than one part in cash");
      }
      List<PaymentPart> parts =
          saleRequest.getPaymentParts().stream()
              .map(
                  partData -> {
                    PaymentPart paymentPart = new PaymentPart();
                    paymentPart.setAmount(partData.getAmount());
                    if (paymentPart.getPaymentType() == PaymentPartType.CASH) {
                      paymentPart.setChange(
                          Optional.ofNullable(partData.getChange()).orElse(BigDecimal.ZERO));
                    } else {
                      paymentPart.setChange(BigDecimal.ZERO);
                    }
                    paymentPart.setReference(partData.getReference());
                    paymentPart.setPaymentType(PaymentPartType.valueOf(partData.getPaymentType()));
                    paymentPart.setAccountSale(clientAccountSale);
                    paymentPart.setSale(internetSale);
                    if (paymentPart.getPaymentType().equals(PaymentPartType.CASH)
                        && terminal != null) {
                      if (terminal.getCurrentAmount() == null) {
                        terminal.setCurrentAmount(BigDecimal.ZERO);
                      }
                      terminal.setCurrentAmount(
                          terminal
                              .getCurrentAmount()
                              .add(paymentPart.getAmount())
                              .subtract(paymentPart.getChange()));
                      salesTerminalRepository.save(terminal);
                    }
                    if (paymentPart.getPaymentType().equals(PaymentPartType.ACCOUNT)) {
                      ClientAccount account =
                          clientAccountRepository.findOne(partData.getReference());
                      paymentPart.setAccount(account);
                      clientAccountRepository.save(account);
                    }
                    paymentPartRepository.save(paymentPart);
                    return paymentPart;
                  })
              .collect(Collectors.toList());
      clientAccountSale.setPaymentParts(parts);
    }

    accountSaleRepository.save(clientAccountSale);

    AccountSaleResponse response = new AccountSaleResponse();
    response.setId(clientAccount.getId());

    return response;
  }

  public SaleResponse buyTrip(SaleRequest saleRequest) throws Exception {
    SaleResponse saleResponse = reserveTrip(saleRequest);
    InternetSale internetSale = internetSaleRepository.findOne(saleResponse.getId());

    BigDecimal price = calculatePrice(saleRequest);

    String startingStopId = saleRequest.getDepartureSegment().getStartingStop().getId();
    String endingStopId = saleRequest.getDepartureSegment().getEndingStop().getId();

    Optional.ofNullable(saleRequest.getBaggage())
        .ifPresent(
            baggage -> {
              PackageTicket packageTicket = new PackageTicket();
              packageTicket.setTrip(tripRepository.findOne(saleRequest.getDepartureTrip().getId()));
              packageTicket.setSale(internetSale);
              packageTicket.setTicketId(idGenerationService.generateId());
              packageTicket.setUser(userService.getCurrentUser());
              packageTicket.setReceiverName(baggage.getReceiverName());
              packageTicket.setSenderName(baggage.getSenderName());
              packageTicket.setPrice(baggage.getTotalPrice());
              packageTicket.setPaymentPrice(baggage.getTotalPrice().multiply(new BigDecimal(0.8)));
              packageTicket.setConcept(baggage.getConcept());
              packageTicket.setContactData(baggage.getContactData());
              packageTicket.setStartingStop(stopOffRepository.findOne(startingStopId));
              packageTicket.setEndingStop(stopOffRepository.findOne(endingStopId));
              packageTicketRepository.save(packageTicket);
            });

    PaymentResponseData payment;

    BigDecimal paymentPrice = price;

    if (saleRequest.getCoupon() != null && saleRequest.getCouponAmount() != null) {
      paymentPrice = price.subtract(saleRequest.getCouponAmount());
      if (saleRequest.getPaymentParts() == null) {
        saleRequest.setPaymentParts(new ArrayList<>());
      }
      ClientAccount account = clientAccountRepository.findByName(saleRequest.getCoupon());
      PaymentPartData couponPart = new PaymentPartData();
      couponPart.setAmount(saleRequest.getCouponAmount());
      couponPart.setChange(BigDecimal.ZERO);
      couponPart.setCoupon(account.getId());
      couponPart.setPaymentType(PaymentPartType.ACCOUNT.name());
      couponPart.setReference(account.getId());
      saleRequest.getPaymentParts().add(couponPart);
    }

    try {
      payment =
          paymentServices
              .get(saleRequest.getPaymentType())
              .executePayment(internetSale.getShortId(), saleRequest, paymentPrice);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    SalesTerminal terminal;

    if (saleRequest.getTerminalId() != null) {
      terminal = salesTerminalRepository.findOne(saleRequest.getTerminalId());
      SalesShift shift = salesShiftRepository.findBySalesTerminalAndFinished(terminal, false);
      Long currentSale = Optional.ofNullable(shift.getCurrentSale()).orElse(0l) + 1l;
      shift.setCurrentSale(currentSale);
      internetSale.setSaleNumber(shift.getShiftNumber() + "-" + currentSale);
      internetSale.setSalesShift(shift);
      internetSale.setSalesTerminal(terminal);
      salesShiftRepository.save(shift);
    } else {
      terminal = null;
    }

    BigDecimal totalPayed =
        Optional.ofNullable(saleRequest.getPaymentParts()).orElse(Collections.emptyList()).stream()
            .map(PaymentPartData::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal change = totalPayed.subtract(price);

    log.debug("Payment parts {}", saleRequest.getPaymentParts());
    int cashParts =
        Optional.ofNullable(saleRequest.getPaymentParts()).orElse(Collections.emptyList()).stream()
            .filter(part -> "CASH".equals(part.getPaymentType()))
            .collect(Collectors.toList())
            .size();
    if (cashParts > 1) {
      throw new RuntimeException("Cannot pay more than one part in cash");
    }
    if (cashParts == 0 && change.doubleValue() > 10) {
      throw new RuntimeException("Cannot give change without cash payment " + change.doubleValue());
    }
    if (saleRequest.getPaymentParts() != null) {
      List<PaymentPart> parts =
          saleRequest.getPaymentParts().stream()
              .map(
                  partData -> {
                    if (partData.getPaymentType().equals("CASH") && terminal != null) {
                      if (terminal.getCurrentAmount() == null) {
                        terminal.setCurrentAmount(BigDecimal.ZERO);
                      }
                      terminal.setCurrentAmount(
                          terminal.getCurrentAmount().add(partData.getAmount().subtract(change)));
                      log.debug("Current amount in terminal {}", terminal.getCurrentAmount());
                      salesTerminalRepository.save(terminal);
                    }
                    PaymentPart paymentPart = new PaymentPart();
                    paymentPart.setAmount(partData.getAmount());
                    paymentPart.setReference(partData.getReference());
                    paymentPart.setPaymentType(PaymentPartType.valueOf(partData.getPaymentType()));
                    if (paymentPart.getPaymentType() == PaymentPartType.CASH) {
                      paymentPart.setChange(change);
                    } else {
                      paymentPart.setChange(BigDecimal.ZERO);
                    }
                    paymentPart.setSale(internetSale);
                    if (paymentPart.getPaymentType().equals(PaymentPartType.ACCOUNT)
                        && payment.isAuthorized()) {
                      ClientAccount account =
                          clientAccountRepository.findOne(partData.getReference());
                      if (account == null) {
                        account = clientAccountRepository.findByName(partData.getReference());
                      }
                      account.setAmount(account.getAmount().subtract(paymentPart.getAmount()));
                      if (account.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                        throw new RuntimeException("Account cannot be negative");
                      }
                      paymentPart.setAccount(account);
                      clientAccountRepository.save(account);
                    }
                    paymentPartRepository.save(paymentPart);
                    return paymentPart;
                  })
              .collect(Collectors.toList());
      internetSale.setPaymentParts(parts);
    }

    internetSale.setTotalAmount(price);
    internetSale.setBill(saleRequest.isNeedBill());
    internetSale.setPaymentId(payment.getId());
    internetSale.setPaymentProvider(payment.getPaymentProvider());
    internetSale.setFullResponse(gson.toJson(payment));

    internetSale.setPayedAmount(saleRequest.getPayedAmount());
    internetSale.setChangeAmount(change);
    internetSale.setPaymentType(saleRequest.getPaymentMethod());

    saleResponse.setPayment(payment);
    saleResponse.setShortId(internetSale.getShortId());

    if (internetSale.getBill()) {
      internetSale.setBillingAddress(
          mapper.map(saleRequest.getBillingAddress(), BillingAddress.class));
      internetSale.setBillingData(mapper.map(saleRequest.getBillingData(), BillingData.class));
      billingAddressRepository.save(internetSale.getBillingAddress());
      billingDataRepository.save(internetSale.getBillingData());
    }

    internetSaleRepository.save(internetSale);

    if (payment.isAuthorized()) {
      approveSale(saleResponse.getId());
    } else if (!payment.getDelayedResponse()) {
      cancelSale(internetSale.getId());
    }

    return saleResponse;
  }

  public void cancelSaleInternal(InternetSale internetSale) {
    List<TripSeat> seats = tripSeatRepository.findAllByInternetSale(internetSale);

    seats.forEach(
        (seat) -> {
          if ("mastercardGateway".equals(internetSale.getPaymentProvider())) {
            log.info(
                "Received an un approved on {} {}", internetSale.getShortId(), seat.getTicketId());
          } else {
            tripSeatRepository.delete(seat);
          }
        });
  }

  public void cancelSale(String saleResponseId) {
    InternetSale internetSale = internetSaleRepository.findOne(saleResponseId);
    cancelSaleInternal(internetSale);
  }

  public Map<String, Object> generateBill(String saleId, BillRequest request) throws Exception {
    InternetSale internetSale =
        Optional.ofNullable(internetSaleRepository.findByShortId(saleId))
            .orElseGet(
                () -> {
                  TripSeat tripSeat = tripSeatRepository.findByTicketId(saleId);
                  return tripSeat.getInternetSale();
                });

    internetSale.setBillingAddress(mapper.map(request.getBillingAddress(), BillingAddress.class));
    internetSale.setBillingData(mapper.map(request.getBillingData(), BillingData.class));

    String rfc =
        Optional.ofNullable(internetSale.getBillingData().getRfc())
            .map(String::toUpperCase)
            .orElse("");
    internetSale.getBillingData().setRfc(rfc);

    billingAddressRepository.save(internetSale.getBillingAddress());
    billingDataRepository.save(internetSale.getBillingData());

    RespuestaTXT response = facturemosYaInvoiceService.createInvoice(internetSale);

    byte[] billBytes = com.google.api.client.util.Base64.decodeBase64(response.getDocumentopdf());

    internetSale.setBill(true);
    internetSale.setBillPdf(Base64.getEncoder().encodeToString(billBytes));
    internetSale.setBillXml(response.getDescripcion());
    internetSaleRepository.save(internetSale);

    return findSaleData(saleId);
  }

  public BillRequest lookupBillingData(String rfc) {
    BillingData billingData = billingDataRepository.findByRfc(rfc);
    if (billingData != null) {
      BillRequest billRequest = new BillRequest();

      billRequest.setBillingData(mapper.map(billingData, justcloud.tickets.dto.BillingData.class));
      InternetSale internetSale = internetSaleRepository.findByBillingData(billingData);

      billRequest.setBillingAddress(mapper.map(internetSale.getBillingAddress(), Address.class));

      return billRequest;
    } else {
      return null;
    }
  }

  private void approveSaleInternal(InternetSale internetSale) throws Exception {
    internetSale.setPayed(true);
    internetSaleRepository.save(internetSale);
    List<TripSeat> seats = tripSeatRepository.findAllByInternetSale(internetSale);

    log.debug("Tickets {}", seats.size());

    List<TicketHolder> tickets =
        seats.stream()
            .map(
                (seat) -> {
                  seat.setStatus(SeatStatus.OCCUPIED);
                  seat.setTicketId(idGenerationService.generateId());
                  tripSeatRepository.save(seat);
                  try {
                    return new TicketHolder(
                        seat.getTicketId(), generateTicket(seat, internetSale.getTimeZone()));
                  } catch (Exception ex) {
                    throw new RuntimeException(ex);
                  }
                })
            .collect(Collectors.toList());

    Map<String, TripSeat> seatMap =
        seats.stream().collect(Collectors.toMap(TripSeat::getTicketId, Function.identity()));

    List<TripHolder> trips =
        seats.stream()
            .map(
                seat -> {
                  Trip trip = seat.getTrip();
                  Date departureDate = calculateTimeAtStop(trip, seat.getStartingStop(), true);
                  return TripHolder.builder()
                      .tripId(trip.getId())
                      .busNumber(
                          Optional.ofNullable(trip.getBus()).map(Bus::getBusNumber).orElse(null))
                      .departureDate(departureDate)
                      .driver1Name(
                          Optional.ofNullable(trip.getDriver1())
                              .map(
                                  driver -> {
                                    return new StringBuilder()
                                        .append(driver.getName())
                                        .append(" ")
                                        .append(
                                            Optional.ofNullable(driver.getLastName()).orElse(""))
                                        .append(" ")
                                        .append(
                                            Optional.ofNullable(driver.getSecondLastName())
                                                .orElse(""))
                                        .toString();
                                  })
                              .orElse(null))
                      .driver2Name(
                          Optional.ofNullable(trip.getDriver2())
                              .map(
                                  driver -> {
                                    return new StringBuilder()
                                        .append(driver.getName())
                                        .append(" ")
                                        .append(
                                            Optional.ofNullable(driver.getLastName()).orElse(""))
                                        .append(" ")
                                        .append(
                                            Optional.ofNullable(driver.getSecondLastName())
                                                .orElse(""))
                                        .toString();
                                  })
                              .orElse(null))
                      .startingStop(seat.getStartingStop().getName())
                      .endingStop(seat.getEndingStop().getName())
                      .route(trip.getRun().getName())
                      .service(trip.getServiceLevelType().getName())
                      .build();
                })
            .distinct()
            .sorted((t1, t2) -> t1.getDepartureDate().compareTo(t2.getDepartureDate()))
            .collect(Collectors.toList());

    Map<String, Date> tripDates =
        trips.stream()
            .collect(Collectors.toMap(TripHolder::getTripId, TripHolder::getDepartureDate));

    Map<String, Object> mailParams = new HashMap<>();
    mailParams.put(
        "seats",
        seats.stream()
            .map(
                seat -> {
                  Date date = tripDates.get(seat.getTrip().getId());
                  return SeatHolder.builder()
                      .ticketId(seat.getTicketId())
                      .seatName(seat.getSeatName())
                      .passengerName(seat.getPassengerName())
                      .startingStop(seat.getStartingStop().getName())
                      .endingStop(seat.getEndingStop().getName())
                      .departureDate(date)
                      .route(seat.getTrip().getRun().getName())
                      .service(seat.getTrip().getServiceLevelType().getName())
                      .build();
                })
            .sorted((t1, t2) -> t1.getDepartureDate().compareTo(t2.getDepartureDate()))
            .collect(Collectors.toList()));
    mailParams.put("sale", internetSale);
    if (trips.size() == 0) {
      return;
    }
    mailParams.put("departureTrip", trips.get(0));
    log.info("HOLA {}", mailParams.get("seats"));
    if (trips.size() > 1) {
      mailParams.put("returnTrip", trips.get(1));
    }

    String template = templateService.buildTemplate("saleApproved", mailParams);

    SendGrid.Email email = new SendGrid.Email();
    email.setSubject("Entrega de tickets medrano");
    email.setFrom(fromMail);
    email.addTo(internetSale.getEmail());
    email.addCategory("SALE_APPROVED");
    email.setHtml(template);

    log.debug("Should generate bill {}", internetSale.getBill());

    if (internetSale.getBill()) {
      RespuestaTXT response = facturemosYaInvoiceService.createInvoice(internetSale);
      byte[] billBytes = com.google.api.client.util.Base64.decodeBase64(response.getDocumentopdf());
      byte[] xmlBytes = response.getDescripcion().getBytes();
      if (billBytes != null) {
        email.addAttachment("factura.pdf", new ByteArrayInputStream(billBytes));
        internetSale.setBillPdf(Base64.getEncoder().encodeToString(billBytes));
      }
      if (xmlBytes != null) {
        email.addAttachment("factura.xml", new ByteArrayInputStream(xmlBytes));
      }
      internetSale.setBillXml(response.getDescripcion());
      internetSaleRepository.save(internetSale);
    }

    tickets.forEach(
        (ticket) -> {
          try {
            StringBuilder ticketName = new StringBuilder();
            TripSeat seat = seatMap.get(ticket.getTicketId());

            ticketName.append(seat.getPassengerName());
            ticketName.append(" - ");
            ticketName.append(seat.getStartingStop().getName());
            ticketName.append(" a ");
            ticketName.append(seat.getEndingStop().getName());
            ticketName.append(".pdf");

            log.debug("Ticket {}", ticketName.toString());

            email.addAttachment(
                ticketName.toString(), new ByteArrayInputStream(ticket.getTicketBytes()));
          } catch (IOException ioe) {
            throw new RuntimeException(ioe);
          }
        });

    if (internetSale.getSalesShift() == null) {
      SendGrid.Response mailResponse = sendgrid.send(email);
      log.debug("Mail response {} {}", internetSale.getEmail(), mailResponse.getMessage());
    }
  }

  public void approveSale(String saleResponseId) throws Exception {
    InternetSale internetSale = internetSaleRepository.findOne(saleResponseId);
    approveSaleInternal(internetSale);
  }

  public byte[] generateAccount(ClientAccount account, String timeZone) throws Exception {
    // ByteArrayOutputStream qrBytes = QRCode.from(seat.getTicketId()).to(ImageType.JPG).stream();
    Map<String, Object> params = new HashMap<>();

    BigDecimal price = account.getAmount();
    DecimalFormat format = new DecimalFormat("#,###.##");

    BigDecimal total = price.abs();
    BigDecimal subtotal = price.divide(new BigDecimal(1.16), 2, RoundingMode.HALF_EVEN);
    BigDecimal taxes = subtotal.multiply(new BigDecimal(0.16));

    DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.getDefault());

    // DateTimeFormatter dateFormatter = DateTimeFormat.shortDate().withLocale(new Locale("es",
    // "MX"));
    // DateTimeFormatter timeFormatter = DateTimeFormat.shortTime().withLocale(new Locale("es",
    // "MX"));
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);

    int width = 100;
    int height = 60;

    BitMatrix bitMatrix =
        new Code128Writer().encode(account.getId(), BarcodeFormat.CODE_128, width, height);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "png", out);

    LocalDateTime dateCreated = new LocalDateTime(account.getDateCreated());

    params.put("qrCode", new ByteArrayInputStream(out.toByteArray()));
    params.put("leftQrCode", new ByteArrayInputStream(out.toByteArray()));
    params.put("passenger", account.getName());
    params.put("taxes", format.format(taxes));
    params.put("subtotalPrice", format.format(subtotal));
    params.put("totalPrice", format.format(total));
    params.put("dateCreated", dateTimeFormatter.print(dateCreated));
    params.put("ticketNumber", account.getId());
    return jasperService.getPdfBytes("account", params);
  }

  public byte[] generateBaggageTicket(PackageTicket packageTicket, String timeZone)
      throws Exception {
    Map<String, Object> params = new HashMap<>();

    String folio = packageTicket.getId();
    String originId = packageTicket.getStartingStop().getId();
    String destinationId = packageTicket.getEndingStop().getId();
    Trip trip = packageTicket.getTrip();
    BigDecimal price = packageTicket.getPrice();
    DecimalFormat format = new DecimalFormat("#,###.##");

    BigDecimal total = price.abs();
    BigDecimal subtotal = price.divide(new BigDecimal(1.16), 2, RoundingMode.HALF_EVEN);
    BigDecimal taxes = subtotal.multiply(new BigDecimal(0.16));

    DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.getDefault());

    DateTime dateCreated = new DateTime(packageTicket.getDateCreated()).withZone(zone);
    DateTime departureTime =
        new DateTime(calculateTimeAtStop(trip, packageTicket.getStartingStop(), true))
            .withZone(zone);
    DateTime arrivalTime =
        new DateTime(calculateTimeAtStop(trip, packageTicket.getEndingStop(), false))
            .withZone(zone);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);

    int width = 100;
    int height = 60;

    log.debug("Departure time {}", dateTimeFormatter.print(departureTime));
    log.debug("Return time {}", dateTimeFormatter.print(arrivalTime));

    params.put(
        "taquillera",
        Optional.ofNullable(packageTicket.getSale())
            .map(InternetSale::getSalesShift)
            .map(SalesShift::getUser)
            .map(
                user ->
                    new StringBuilder()
                        .append(Optional.ofNullable(user.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getSecondLastName()).orElse(""))
                        .toString())
            .orElse(""));

    log.debug("Salesman {}", params.get("taquillera"));

    params.put("origin", packageTicket.getStartingStop().getName());
    params.put("destination", packageTicket.getEndingStop().getName());
    params.put("serviceType", packageTicket.getTrip().getServiceLevelType().getName());
    params.put("taxes", format.format(taxes));
    params.put("subtotalPrice", format.format(subtotal));
    params.put("totalPrice", format.format(total));
    params.put("dateCreated", dateTimeFormatter.print(dateCreated));
    params.put("entrega", packageTicket.getSenderName());
    params.put("recibe", packageTicket.getReceiverName());
    params.put("observaciones", packageTicket.getConcept());
    params.put("departure", dateTimeFormatter.print(departureTime));
    params.put("departureDate", dateFormatter.print(departureTime));
    params.put("departureTime", timeFormatter.print(departureTime));
    params.put("arrival", dateTimeFormatter.print(arrivalTime));
    params.put("arrivalDate", dateFormatter.print(arrivalTime));
    params.put("arrivalTime", timeFormatter.print(arrivalTime));
    params.put("routeName", trip.getRun().getRoute().getName());
    params.put("Folio", packageTicket.getSale().getShortId());

    return jasperService.getPdfBytes("TicketPaqueteria", params);
  }

  public InputStream generateAllTickets(String saleId, String timeZone) throws Exception {
    return generateAllTickets(internetSaleRepository.findOne(saleId), timeZone);
  }

  public InputStream generateAllTickets(InternetSale internetSale, String timeZone)
      throws Exception {
    List<TripSeat> seats = tripSeatRepository.findAllByInternetSale(internetSale);

    List<byte[]> seatTickets =
        seats.stream()
            .map(
                seat -> {
                  try {
                    return generateTicket(seat, timeZone);
                  } catch (Exception ex) {
                    return null;
                  }
                })
            .filter(ticket -> ticket != null)
            .collect(Collectors.toList());

    PDFMergerUtility merger = new PDFMergerUtility();
    seatTickets.forEach(ticket -> merger.addSource(new ByteArrayInputStream(ticket)));

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    merger.setDestinationStream(out);

    merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

    return new ByteArrayInputStream(out.toByteArray());
  }

  public byte[] generateTicket(TripSeat seat, String timeZone) throws Exception {
    byte[] qrBytes = QRCode.from(seat.getTicketId()).to(ImageType.JPG).stream().toByteArray();
    Map<String, Object> params = new HashMap<>();

    String originId = seat.getStartingStop().getId();
    String destinationId = seat.getEndingStop().getId();
    Trip trip = seat.getTrip();
    BigDecimal price = seat.getSoldPrice();
    DecimalFormat format = new DecimalFormat("#,###.##");

    BigDecimal total = price.abs();
    BigDecimal subtotal = price.divide(new BigDecimal(1.16), 2, RoundingMode.HALF_EVEN);
    BigDecimal taxes = subtotal.multiply(new BigDecimal(0.16));

    DateTimeZone zone =
        Optional.ofNullable(TimeZoneUtils.cleanTimezone(timeZone))
            .map(DateTimeZone::forID)
            .orElse(DateTimeZone.getDefault());

    DateTime dateCreated = new DateTime(seat.getDateCreated()).withZone(zone);
    DateTime departureTime =
        new DateTime(calculateTimeAtStop(trip, seat.getStartingStop(), true)).withZone(zone);
    DateTime arrivalTime =
        new DateTime(calculateTimeAtStop(trip, seat.getEndingStop(), false)).withZone(zone);

    DateTimeFormatter dateFormatter =
        DateTimeFormat.shortDate().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter timeFormatter =
        DateTimeFormat.shortTime().withLocale(new Locale("es", "MX")).withZone(zone);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.shortDateTime().withLocale(new Locale("es", "MX")).withZone(zone);

    int width = 100;
    int height = 60;

    log.debug("Departure time {}", dateTimeFormatter.print(departureTime));
    log.debug("Return time {}", dateTimeFormatter.print(arrivalTime));

    params.put(
        "taquillera",
        Optional.ofNullable(seat.getInternetSale())
            .map(InternetSale::getSalesShift)
            .map(SalesShift::getUser)
            .map(
                user ->
                    new StringBuilder()
                        .append(Optional.ofNullable(user.getName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getLastName()).orElse(""))
                        .append(" ")
                        .append(Optional.ofNullable(user.getSecondLastName()).orElse(""))
                        .toString())
            .orElse(""));

    log.debug("Salesman {}", params.get("taquillera"));

    params.put("qrCode", new ByteArrayInputStream(qrBytes));
    params.put("leftQrCode", new ByteArrayInputStream(qrBytes));
    params.put("origin", seat.getStartingStop().getName());
    params.put("destination", seat.getEndingStop().getName());
    params.put("passenger", seat.getPassengerName());
    params.put("seatNumber", seat.getSeatName());
    params.put("serviceType", seat.getTrip().getServiceLevelType().getName());
    params.put("taxes", format.format(taxes));
    params.put("subtotalPrice", format.format(subtotal));
    params.put("totalPrice", format.format(total));
    params.put("dateCreated", dateTimeFormatter.print(dateCreated));
    params.put("passengerType", getHumanReadablePassengerType(seat.getPassengerType()));
    params.put("departure", dateTimeFormatter.print(departureTime));
    params.put("departureDate", dateFormatter.print(departureTime));
    params.put("departureTime", timeFormatter.print(departureTime));
    params.put("arrival", dateTimeFormatter.print(arrivalTime));
    params.put("arrivalDate", dateFormatter.print(arrivalTime));
    params.put("arrivalTime", timeFormatter.print(arrivalTime));
    params.put("routeName", trip.getRun().getRoute().getName());
    params.put("ticketNumber", seat.getTicketId());

    String ticketTemplate;

    if (seat.getInternetSale().getSalesman() == null) {
      ticketTemplate = "ticketWeb";
    } else {
      ticketTemplate = "ticket";
    }

    return jasperService.getPdfBytes(ticketTemplate, params);
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

  public SalesTerminalData findTerminal(String terminalId) {
    return mapper.map(
        salesTerminalRepository.findByTerminalId(terminalId), SalesTerminalData.class);
  }

  Date calculateTimeAtStop(Trip trip, StopOff stop, boolean before) {
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

  public SaleResponse reserveTrip(SaleRequest saleRequest) {
    InternetSale internetSale = new InternetSale();
    internetSale.setShortId(idGenerationService.generateId());
    internetSale.setPayed(false);
    internetSale.setTimeZone(saleRequest.getTimeZone());
    internetSale.setEmail(saleRequest.getEmail());

    if (userService.getCurrentUser() != null) {
      internetSale.setSalesman(userService.getCurrentUser());
    }

    internetSaleRepository.save(internetSale);

    SaleResponse saleResponse = new SaleResponse();
    saleResponse.setShortId(internetSale.getShortId());
    saleResponse.setId(internetSale.getId());

    Trip departureTrip =
        Optional.ofNullable(saleRequest.getDepartureTrip())
            .map((tripDto) -> tripDto.getId())
            .map((tripId) -> tripRepository.findOne(tripId))
            .orElse(null);

    Trip returnTrip =
        Optional.ofNullable(saleRequest.getReturnTrip())
            .map((tripDto) -> tripDto.getId())
            .map((tripId) -> tripRepository.findOne(tripId))
            .orElse(null);

    final Map<String, Boolean> departureOccupations;
    final Map<String, Boolean> returnOccupations;

    if (saleRequest.getApproverUsername() != null) {
      internetSale.setApprover(userService.findByUsername(saleRequest.getApproverUsername()));
    }

    if (saleRequest.getDepartureSegment() != null) {
      departureOccupations =
          checkOccupation(
              tripSeatRepository.findAllByTrip(departureTrip).stream()
                  .collect(Collectors.groupingBy(TripSeat::getSeatName)),
              departureTrip.getRun().getStops().stream()
                  .sorted(
                      (s1, s2) -> {
                        if (departureTrip.getReverse()) {
                          return s2.getOrderIndex().compareTo(s1.getOrderIndex());
                        } else {
                          return s1.getOrderIndex().compareTo(s2.getOrderIndex());
                        }
                      })
                  .collect(Collectors.toList()),
              saleRequest.getDepartureSegment().getStartingStop().getId(),
              saleRequest.getDepartureSegment().getEndingStop().getId());
    } else {
      departureOccupations = null;
    }

    if (saleRequest.getReturnSegment() != null
        && saleRequest.getReturnSegment().getStartingStop() != null
        && saleRequest.getReturnSegment().getStartingStop().getId() != null) {
      returnOccupations =
          checkOccupation(
              tripSeatRepository.findAllByTrip(returnTrip).stream()
                  .collect(Collectors.groupingBy(TripSeat::getSeatName)),
              returnTrip.getRun().getStops().stream()
                  .sorted(
                      (s1, s2) -> {
                        if (departureTrip.getReverse()) {
                          return s2.getOrderIndex().compareTo(s1.getOrderIndex());
                        } else {
                          return s1.getOrderIndex().compareTo(s2.getOrderIndex());
                        }
                      })
                  .collect(Collectors.toList()),
              saleRequest.getReturnSegment().getStartingStop().getId(),
              saleRequest.getReturnSegment().getEndingStop().getId());
    } else {
      returnOccupations = null;
    }

    Map<PassengerType, Long> buyTypeCounts =
        Optional.ofNullable(saleRequest.getPassengers()).orElse(Collections.emptyList()).stream()
            .collect(Collectors.groupingBy(PassengerData::getPassengerType, Collectors.counting()));

    Long olderAdultsBought = buyTypeCounts.getOrDefault(PassengerType.OLDER_ADULT, 0l);
    Long studentsBought = buyTypeCounts.getOrDefault(PassengerType.STUDENT, 0l);

    if (saleRequest.getDepartureSegment() != null) {
      Map<PassengerType, Long> counts =
          tripSeatRepository.findAllByTrip(departureTrip).stream()
              .collect(Collectors.groupingBy(TripSeat::getPassengerType, Collectors.counting()));

      Long soldOlderAdults = counts.getOrDefault(PassengerType.OLDER_ADULT, 0l);
      Long soldStudents = counts.getOrDefault(PassengerType.STUDENT, 0l);

      if (soldOlderAdults + olderAdultsBought > maxOlderAdults && olderAdultsBought > 0) {
        throw new RuntimeException("OLDER_ADULTS_EXCEEDED");
      }

      if (soldStudents + studentsBought > maxStudents && studentsBought > 0) {
        throw new RuntimeException("STUDENTS_EXCEEDED");
      }
    }

    if (saleRequest.getReturnSegment() != null
        && saleRequest.getReturnSegment().getStartingStop() != null
        && saleRequest.getReturnSegment().getStartingStop().getId() != null) {
      Map<PassengerType, Long> counts =
          tripSeatRepository.findAllByTrip(returnTrip).stream()
              .collect(Collectors.groupingBy(TripSeat::getPassengerType, Collectors.counting()));

      Long soldOlderAdults = counts.getOrDefault(PassengerType.OLDER_ADULT, 0l);
      Long soldStudents = counts.getOrDefault(PassengerType.STUDENT, 0l);

      if (soldOlderAdults + olderAdultsBought > maxOlderAdults) {
        throw new RuntimeException("OLDER_ADULTS_EXCEEDED");
      }

      if (soldStudents + studentsBought > maxStudents) {
        throw new RuntimeException("STUDENTS_EXCEEDED");
      }
    }

    List<PassengerData> passengerDataList =
        Optional.ofNullable(saleRequest.getPassengers()).orElse(Collections.emptyList()).stream()
            .filter((passenger) -> passenger.getPassengerType() != PassengerType.INFANT)
            .collect(Collectors.toList());
    for (PassengerData passenger : passengerDataList) {
      if (saleRequest.getDepartureSegment() != null) {
        if (departureOccupations.containsKey(passenger.getDepartureSeat())
            && departureOccupations.get(passenger.getDepartureSeat())) {
          throw new RuntimeException("Seat already reserved");
        }
        StopOff startingStop =
            stopOffRepository.findOne(saleRequest.getDepartureSegment().getStartingStop().getId());
        StopOff endingStop =
            stopOffRepository.findOne(saleRequest.getDepartureSegment().getEndingStop().getId());
        TripSeat seat = new TripSeat();
        seat.setInternetSale(internetSale);
        seat.setTrip(departureTrip);
        seat.setSeatName(passenger.getDepartureSeat());
        seat.setStatus(SeatStatus.RESERVED);
        seat.setPassengerType(passenger.getPassengerType());
        seat.setSoldPrice(passenger.getDeparturePrice());
        seat.setOriginalPrice(
            passenger.getOriginalPrice() != null
                ? passenger.getOriginalPrice()
                : passenger.getDeparturePrice());
        seat.setUser(userService.getCurrentUser());
        seat.setComments(passenger.getComments());
        seat.setStartingStop(startingStop);
        seat.setPassengerName(passenger.getName());
        seat.setEndingStop(endingStop);
        seat = tripSeatRepository.save(seat);
        log.debug("Creating seat {} {}", seat.getId(), internetSale.getId());
      }
      if (saleRequest.getReturnSegment() != null
          && saleRequest.getReturnSegment().getStartingStop() != null
          && saleRequest.getReturnSegment().getStartingStop().getId() != null) {
        if (returnOccupations.containsKey(passenger.getReturnSeat())
            && returnOccupations.get(passenger.getReturnSeat())) {
          throw new RuntimeException("Seat already reserved");
        }
        StopOff startingStop =
            stopOffRepository.findOne(saleRequest.getReturnSegment().getStartingStop().getId());
        StopOff endingStop =
            stopOffRepository.findOne(saleRequest.getReturnSegment().getEndingStop().getId());
        TripSeat seat = new TripSeat();
        seat.setInternetSale(internetSale);
        seat.setTrip(returnTrip);
        seat.setSeatName(passenger.getReturnSeat());
        seat.setComments(passenger.getComments());
        seat.setPassengerType(passenger.getPassengerType());
        seat.setSoldPrice(passenger.getReturnPrice());
        seat.setOriginalPrice(
            passenger.getOriginalPrice() != null
                ? passenger.getOriginalPrice()
                : passenger.getReturnPrice());
        seat.setStatus(SeatStatus.RESERVED);
        seat.setUser(userService.getCurrentUser());
        seat.setStartingStop(startingStop);
        seat.setPassengerName(passenger.getName());
        seat.setEndingStop(endingStop);
        seat = tripSeatRepository.save(seat);
      }
    }

    internetSaleRepository.save(internetSale);

    return saleResponse;
  }

  public String generateReportCsv(String officeLocationId) {
    OfficeLocation officeLocation = officeLocationRepository.findOne(officeLocationId);
    List<SalesTerminal> terminals = salesTerminalRepository.findAllByOfficeLocation(officeLocation);

    String ticketSales =
        terminals.stream()
            .flatMap(
                (salesTerminal) ->
                    internetSaleRepository.findAllBySalesTerminal(salesTerminal).stream())
            .map(
                sale -> {
                  return sale.getPaymentParts().stream()
                      .map(
                          part -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append(officeLocation.getName());
                            builder.append(",");
                            builder.append(sale.getSalesTerminal().getTerminalName());
                            builder.append(",");
                            builder.append(sale.getShortId());
                            builder.append(",");
                            builder.append(part.getPaymentType().toString());
                            builder.append(",");
                            if (part.getPaymentType() == PaymentPartType.ACCOUNT) {
                              if (part.getAccount() != null) {
                                builder.append(part.getAccount().getId());
                              }
                            } else {
                              builder.append(part.getReference());
                            }
                            builder.append(",");
                            builder.append(part.getAmount());
                            return builder.toString();
                          })
                      .collect(Collectors.joining("\n"));
                })
            .collect(Collectors.joining("\n"));

    String clientAccountSales =
        terminals.stream()
            .flatMap(
                (salesTerminal) ->
                    accountSaleRepository.findAllBySalesTerminal(salesTerminal).stream())
            .map(
                sale -> {
                  return sale.getPaymentParts().stream()
                      .map(
                          part -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append(officeLocation.getName());
                            builder.append(",");
                            builder.append(sale.getSalesTerminal().getTerminalName());
                            builder.append(",");
                            builder.append(sale.getId());
                            builder.append(",");
                            builder.append(part.getPaymentType().toString());
                            builder.append(",");
                            if (part.getReference() != null) {
                              builder.append(part.getReference());
                            }
                            builder.append(",");
                            builder.append(part.getAmount());
                            return builder.toString();
                          })
                      .collect(Collectors.joining("\n"));
                })
            .collect(Collectors.joining("\n"));

    return ticketSales + "\n" + clientAccountSales;
  }

  private BigDecimal calculatePrice(SaleRequest saleRequest) {
    // Calculate price
    Trip departureTrip = tripRepository.findOne(saleRequest.getDepartureTrip().getId());
    /*
    Trip returnTrip = null;
    if(saleRequest.getReturnTrip() != null && saleRequest.getReturnTrip().getId() != null) {
      returnTrip = tripRepository.findOne(saleRequest.getReturnTrip().getId());
    }
    */
    BigDecimal departurePrice = getSegmentPrice(saleRequest.getDepartureSegment(), departureTrip);
    BigDecimal returnPrice =
        departurePrice; // getSegmentPrice(saleRequest.getReturnSegment(), returnTrip);
    List<BigDecimal> items =
        Optional.ofNullable(saleRequest.getPassengers()).orElse(Collections.emptyList()).stream()
            .flatMap(
                (passenger) -> {
                  List<BigDecimal> innerItems = new ArrayList<>();
                  if (passenger.getDepartureSeat() != null && departurePrice != null) {
                    innerItems.add(computeDiscount(departurePrice, passenger.getPassengerType()));
                  }
                  if (passenger.getReturnSeat() != null && returnPrice != null) {
                    innerItems.add(computeDiscount(returnPrice, passenger.getPassengerType()));
                  }
                  return innerItems.stream();
                })
            .collect(Collectors.toList());

    return Optional.ofNullable(saleRequest.getBaggage())
        .map(baggage -> baggage.getTotalPrice())
        .orElse(BigDecimal.ZERO)
        .add(
            items.stream()
                .reduce(
                    BigDecimal.ZERO,
                    (acc, curr) -> {
                      return acc.add(curr);
                    }));
    // Done calculate price
  }

  private BigDecimal getSegmentPrice(RouteSegment segment, Trip trip) {
    if (segment == null
        || segment.getStartingStop() == null
        || segment.getStartingStop().getId() == null) return null;

    String originId = segment.getStartingStop().getId();
    String destinationId = segment.getEndingStop().getId();

    SegmentVar price =
        segmentVaRepository.findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
            originId, destinationId, "price", trip.getServiceLevelType());
    if (price == null) {
      price =
          segmentVaRepository.findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
              destinationId, originId, "price", trip.getServiceLevelType());
    }

    return price.getNumericVar();
  }

  private Map<String, Boolean> checkOccupation(
      Map<String, List<TripSeat>> tripSeats,
      List<StopOff> tripStops,
      String originId,
      String destinationId) {
    int index = 0;
    Map<String, Boolean> result = new HashMap<>();

    Map<String, Integer> stopDict = new HashMap<>();
    for (StopOff stop : tripStops) {
      stopDict.put(stop.getId(), index++);
    }

    Integer wantedStart = stopDict.get(originId);
    Integer wantedEnd = stopDict.get(destinationId);

    HashSet<Integer> wantedPosition = new HashSet<>();

    for (int i = wantedStart; i < wantedEnd; i++) {
      wantedPosition.add(i);
    }

    log.debug("Wanted position: {}", wantedPosition);

    tripSeats.forEach(
        (seatName, tickets) -> {
          HashSet<Integer> occupiedPositions = new HashSet<>();

          tickets.forEach(
              ticket -> {
                Integer startingPosition = stopDict.get(ticket.getStartingStop().getId());
                Integer endingPosition = stopDict.get(ticket.getEndingStop().getId());

                log.debug(
                    "Starting position data {} {}",
                    startingPosition,
                    ticket.getStartingStop().getId());
                log.debug(
                    "Ending position data {} {}", endingPosition, ticket.getEndingStop().getId());

                for (int i = startingPosition; i < endingPosition; i++) {
                  occupiedPositions.add(i);
                }
              });

          log.debug("Occupation {} {}", seatName, occupiedPositions);

          HashSet<Integer> intersection = new HashSet<>(wantedPosition);
          intersection.retainAll(occupiedPositions);

          result.put(seatName, intersection.size() > 0);
        });

    return result;
  }

  public SaleResponse payTrip(String id, SaleRequest saleRequest) throws Exception {
    SaleResponse saleResponse = new SaleResponse();
    InternetSale internetSale = internetSaleRepository.findOne(id);
    saleResponse.setShortId(internetSale.getShortId());
    saleResponse.setId(internetSale.getId());

    List<TripSeat> tickets = tripSeatRepository.findAllByInternetSale(internetSale);

    BigDecimal price =
        tickets.stream()
            .map(TripSeat::getSoldPrice)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);

    PaymentResponseData payment;

    try {
      payment =
          paymentServices
              .get(saleRequest.getPaymentType())
              .executePayment(internetSale.getShortId(), saleRequest, price);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    SalesTerminal terminal;

    if (saleRequest.getTerminalId() != null) {
      terminal = salesTerminalRepository.findOne(saleRequest.getTerminalId());
      SalesShift shift = salesShiftRepository.findBySalesTerminalAndFinished(terminal, false);
      Long currentSale = Optional.ofNullable(shift.getCurrentSale()).orElse(0l) + 1l;
      shift.setCurrentSale(currentSale);
      internetSale.setSaleNumber(shift.getShiftNumber() + "-" + currentSale);
      internetSale.setSalesShift(shift);
      internetSale.setCashCheckpoint(null);
      internetSale.setSalesTerminal(terminal);

      for (TripSeat ticket : tickets) {
        ticket.setUser(shift.getUser());
        ticket.setDateCreated(new Date());
        tripSeatRepository.save(ticket);
      }

      salesShiftRepository.save(shift);
    } else {
      terminal = null;
    }

    BigDecimal totalPayed =
        saleRequest.getPaymentParts().stream()
            .map(PaymentPartData::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal change = totalPayed.subtract(price);

    log.debug("Payment parts {}", saleRequest.getPaymentParts());
    int cashParts =
        saleRequest.getPaymentParts().stream()
            .filter(part -> "CASH".equals(part.getPaymentType()))
            .collect(Collectors.toList())
            .size();
    if (cashParts > 1) {
      throw new RuntimeException("Cannot pay more than one part in cash");
    }
    if (cashParts == 0 && change.doubleValue() > 10) {
      throw new RuntimeException("Cannot give change without cash payment");
    }
    if (saleRequest.getPaymentParts() != null) {
      List<PaymentPart> parts =
          saleRequest.getPaymentParts().stream()
              .map(
                  partData -> {
                    if (partData.getPaymentType().equals("CASH") && terminal != null) {
                      if (terminal.getCurrentAmount() == null) {
                        terminal.setCurrentAmount(BigDecimal.ZERO);
                      }
                      terminal.setCurrentAmount(
                          terminal.getCurrentAmount().add(partData.getAmount().subtract(change)));
                      log.debug("Current amount in terminal {}", terminal.getCurrentAmount());
                      salesTerminalRepository.save(terminal);
                    }

                    PaymentPart paymentPart = new PaymentPart();
                    paymentPart.setAmount(partData.getAmount());
                    paymentPart.setReference(partData.getReference());
                    paymentPart.setPaymentType(PaymentPartType.valueOf(partData.getPaymentType()));
                    if (paymentPart.getPaymentType() == PaymentPartType.CASH) {
                      paymentPart.setChange(change);
                    } else {
                      paymentPart.setChange(BigDecimal.ZERO);
                    }
                    paymentPart.setSale(internetSale);
                    if (paymentPart.getPaymentType().equals(PaymentPartType.ACCOUNT)) {
                      ClientAccount account =
                          clientAccountRepository.findOne(partData.getReference());
                      account.setAmount(account.getAmount().subtract(paymentPart.getAmount()));
                      if (account.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                        throw new RuntimeException("Account cannot be negative");
                      }
                      paymentPart.setAccount(account);
                      clientAccountRepository.save(account);
                    }
                    paymentPartRepository.save(paymentPart);
                    return paymentPart;
                  })
              .collect(Collectors.toList());
      internetSale.setPaymentParts(parts);
    }

    internetSale.setTotalAmount(price);
    internetSale.setBill(saleRequest.isNeedBill());
    internetSale.setPaymentId(payment.getId());
    internetSale.setPaymentProvider(payment.getPaymentProvider());
    internetSale.setFullResponse(gson.toJson(payment));

    internetSale.setPayedAmount(saleRequest.getPayedAmount());
    internetSale.setChangeAmount(change);
    internetSale.setPaymentType(saleRequest.getPaymentMethod());

    saleResponse.setPayment(payment);
    saleResponse.setShortId(internetSale.getShortId());

    if (internetSale.getBill()) {
      internetSale.setBillingAddress(
          mapper.map(saleRequest.getBillingAddress(), BillingAddress.class));
      internetSale.setBillingData(mapper.map(saleRequest.getBillingData(), BillingData.class));
      billingAddressRepository.save(internetSale.getBillingAddress());
      billingDataRepository.save(internetSale.getBillingData());
    }

    internetSaleRepository.save(internetSale);

    if (payment.isAuthorized()) {
      approveSale(saleResponse.getId());
    } else if (!payment.getDelayedResponse()) {
      cancelSale(internetSale.getId());
    }

    return saleResponse;
  }

  public Map<String, Object> updateComments(String ticketId, String comments) {
    TripSeat ticket = tripSeatRepository.findByTicketId(ticketId);
    Map<String, Object> result = new HashMap<>();
    if (ticket != null) {
      ticket.setComments(comments);
      tripSeatRepository.save(ticket);
      result.put("outcome", "success");
    } else {
      result.put("outcome", "error");
    }
    return result;
  }

  public SaleResponse cancelReservation(String id) {
    SaleResponse saleResponse = new SaleResponse();
    InternetSale internetSale = internetSaleRepository.findOne(id);
    saleResponse.setShortId(internetSale.getShortId());
    saleResponse.setId(internetSale.getId());

    List<TripSeat> seats = tripSeatRepository.findAllByInternetSale(internetSale);
    tripSeatRepository.delete(seats);

    return saleResponse;
  }

  public LoginResponse checkUserDetail(LoginRequest loginRequest) {
    return new LoginResponse(
        userService.checkPassword(loginRequest.getUsername(), loginRequest.getPassword()));
  }

  @Data
  @Builder
  public static class TripHolder {
    private String tripId;
    private String startingStop;
    private String endingStop;
    private String route;
    private String service;
    private String busNumber;
    private String driver1Name;
    private String driver2Name;
    private Date departureDate;
  }

  @Data
  @Builder
  public static class SeatHolder {
    private String ticketId;
    private String passengerName;
    private String seatName;
    private String route;
    private String service;
    private String startingStop;
    private String endingStop;
    private Date departureDate;
  }

  public static class TicketHolder {
    private String ticketId;
    private byte[] ticketBytes;

    public TicketHolder(String ticketId, byte[] ticketBytes) {
      setTicketId(ticketId);
      setTicketBytes(ticketBytes);
    }

    /** @return the ticketId */
    public String getTicketId() {
      return ticketId;
    }

    /** @param ticketId the ticketId to set */
    public void setTicketId(String ticketId) {
      this.ticketId = ticketId;
    }

    /** @return the ticketBytes */
    public byte[] getTicketBytes() {
      return ticketBytes;
    }

    /** @param ticketBytes the ticketBytes to set */
    public void setTicketBytes(byte[] ticketBytes) {
      this.ticketBytes = ticketBytes;
    }
  }

  private BigDecimal computeDiscount(BigDecimal originalPrice, PassengerType passengerType) {
    if (getDiscountedPassengers().contains(passengerType)) {
      return computeDiscount(originalPrice);
    }
    return originalPrice;
  }

  private ImmutableList<PassengerType> getDiscountedPassengers() {
    ImmutableList.Builder<PassengerType> types = ImmutableList.builder();

    types.add(PassengerType.OLDER_ADULT);
    types.add(PassengerType.CHILD);

    Parameter parameter = parameterRepository.findOne("student-discount");
    if (parameter != null && "enabled".equals(parameter.getValue())) {
      types.add(PassengerType.STUDENT);
    }

    return types.build();
  }

  private static BigDecimal computeDiscount(BigDecimal originalPrice) {
    return originalPrice.multiply(new BigDecimal(DISCOUNT)).setScale(0, RoundingMode.CEILING);
  }
}
