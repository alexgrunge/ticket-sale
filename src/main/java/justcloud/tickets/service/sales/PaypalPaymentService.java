package justcloud.tickets.service.sales;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import justcloud.tickets.domain.repository.SegmentVarRepository;
import justcloud.tickets.domain.repository.StopOffRepository;
import justcloud.tickets.domain.repository.TripRepository;
import justcloud.tickets.domain.tickets.SegmentVar;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;
import justcloud.tickets.dto.PaymentResponseData;
import justcloud.tickets.dto.PaypalPaymentResponse;
import justcloud.tickets.dto.SaleRequest;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("paypal")
@Qualifier("paypal")
public class PaypalPaymentService implements PaymentService {

  private Logger log = LoggerFactory.getLogger(PaypalPaymentService.class);

  @Autowired private SegmentVarRepository segmentVarRepository;

  @Autowired private TripRepository tripRepository;

  @Autowired private StopOffRepository stopOffRepository;

  @Override
  public PaymentResponseData executePayment(
      String saleId, SaleRequest saleRequest, BigDecimal amount) throws Exception {
    OAuthTokenCredential tokenCredential =
        Payment.initConfig(getClass().getResourceAsStream("/sdk_config.properties"));
    String accessToken = tokenCredential.getAccessToken();

    StopOff departureOrigin =
        stopOffRepository.findOne(saleRequest.getDepartureSegment().getStartingStop().getId());
    StopOff departureDestination =
        stopOffRepository.findOne(saleRequest.getDepartureSegment().getEndingStop().getId());

    if (saleRequest.getReturnSegment() != null
        && saleRequest.getReturnSegment().getStartingStop() != null
        && saleRequest.getReturnSegment().getStartingStop().getId() == null) {
      saleRequest.setReturnSegment(null);
    }

    StopOff returnOrigin =
        Optional.ofNullable(saleRequest.getReturnSegment())
            .map(
                (segment) -> {
                  return stopOffRepository.findOne(segment.getStartingStop().getId());
                })
            .orElse(null);
    StopOff returnDestination =
        Optional.ofNullable(saleRequest.getReturnSegment())
            .map(
                (segment) -> {
                  return stopOffRepository.findOne(segment.getEndingStop().getId());
                })
            .orElse(null);

    APIContext apiContext = new APIContext(accessToken);
    Payment payment = new Payment();
    payment.setIntent("sale");
    payment.setPayer(new Payer().setPaymentMethod("paypal"));

    Trip departureTrip = tripRepository.findOne(saleRequest.getDepartureTrip().getId());
    // Trip returnTrip = null;

    BigDecimal departurePrice = getSegmentPrice(saleRequest.getDepartureSegment(), departureTrip);
    BigDecimal rPrice =
        departurePrice; // getSegmentPrice(saleRequest.getReturnSegment(), returnTrip);

    // THIS IS A TERRIBLE BUG FIX
    // if(rPrice == null) {
    // 	if(saleRequest.getReturnTrip() != null && saleRequest.getReturnTrip().getId() != null) {
    // 		rPrice = departurePrice;
    // 	}
    // }
    BigDecimal returnPrice = rPrice;

    DecimalFormat format = new DecimalFormat("#0.##");

    payment.setRedirectUrls(
        new RedirectUrls()
            .setReturnUrl("http://site.medrano.just-cloud.com/validatePaypal")
            .setCancelUrl("http://site.medrano.just-cloud.com/cancelPaypal"));

    List<Item> items =
        saleRequest.getPassengers().stream()
            .flatMap(
                (passenger) -> {
                  List<Item> innerItems = new ArrayList<>();
                  log.info("Departure price {}", departurePrice);
                  if (passenger.getDepartureSeat() != null && departurePrice != null) {
                    innerItems.add(
                        new Item(
                            departureOrigin.getName()
                                + "-"
                                + departureDestination.getName()
                                + " "
                                + passenger.getName()
                                + ", asiento: "
                                + passenger.getDepartureSeat(),
                            "1",
                            format.format(departurePrice),
                            "MXN"));
                  }
                  log.info("Return price {} {}", returnPrice);
                  if (passenger.getReturnSeat() != null && returnPrice != null) {
                    innerItems.add(
                        new Item(
                            returnOrigin.getName()
                                + "-"
                                + returnDestination.getName()
                                + " "
                                + passenger.getName()
                                + ", asiento: "
                                + passenger.getReturnSeat(),
                            "1",
                            format.format(returnPrice),
                            "MXN"));
                  }
                  return innerItems.stream();
                })
            .collect(Collectors.toList());

    List<Transaction> transactions = new ArrayList<>();
    log.info("Monto price {}", amount);

    Transaction transaction = new Transaction();
    transaction.setItemList(new ItemList().setItems(items));
    transaction.setAmount(new Amount("MXN", format.format(amount)));
    transactions.add(transaction);
    payment.setTransactions(transactions);

    payment = payment.create(apiContext);

    String self =
        payment.getLinks().stream()
            .filter(
                (link) -> {
                  return link.getRel().equals("self");
                })
            .map(
                (link) -> {
                  return link.getHref();
                })
            .findAny()
            .get();

    String redirect =
        payment.getLinks().stream()
            .filter(
                (link) -> {
                  return link.getRel().equals("approval_url");
                })
            .map(
                (link) -> {
                  return link.getHref();
                })
            .findAny()
            .get();

    PaypalPaymentResponse response = new PaypalPaymentResponse();
    response.setAmount(amount);
    response.setApprovalUrl(redirect);
    response.setSelfUrl(self);
    response.setId(payment.getId());

    return response;
  }

  public PaymentResponseData confirmPayment(String paymentId, String token, String payerId)
      throws Exception {
    OAuthTokenCredential tokenCredential =
        Payment.initConfig(getClass().getResourceAsStream("/sdk_config.properties"));
    String accessToken = tokenCredential.getAccessToken();
    APIContext apiContext = new APIContext(accessToken);

    Payment payment = new Payment();
    payment.setId(paymentId);
    PaymentExecution execution = new PaymentExecution();
    execution.setPayerId(payerId);

    payment = payment.execute(apiContext, execution);

    PaypalPaymentResponse response = new PaypalPaymentResponse();

    boolean authorized = payment.getState().equals("approved");

    BigDecimal amount =
        payment.getTransactions().stream()
            .map(
                (transaction) -> {
                  return new BigDecimal(transaction.getAmount().getTotal());
                })
            .findFirst()
            .get();

    response.setAmount(amount);
    response.setAuthorized(authorized);
    response.setId(paymentId);

    return response;
  }

  private BigDecimal getSegmentPrice(RouteSegment segment, Trip trip) {
    if (segment == null
        || segment.getStartingStop() == null
        || segment.getStartingStop().getId() == null) return null;

    String originId = segment.getStartingStop().getId();
    String destinationId = segment.getEndingStop().getId();

    SegmentVar price =
        segmentVarRepository.findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
            originId, destinationId, "price", trip.getServiceLevelType());

    return price.getNumericVar();
  }
}
