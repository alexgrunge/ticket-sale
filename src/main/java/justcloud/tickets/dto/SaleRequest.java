package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.List;
import justcloud.tickets.search.RouteSegment;

public class SaleRequest {

  private List<PaymentPartData> paymentParts;

  private BaggageData baggage;

  private String account;
  private String coupon;
  private BigDecimal couponAmount;
  private String timeZone;
  private String email;
  private String paymentType;
  private String paymentMethod;

  private String approverUsername;
  private String terminalId;

  private BigDecimal payedAmount;
  private BigDecimal changeAmount;
  private BigDecimal totalPrice;

  private Address address;
  private Address billingAddress;
  private BillingData billingData;
  private boolean needBill = false;
  private StoreRequestData store;
  private CreditCardData creditCard;

  private TripData departureTrip;
  private TripData returnTrip;

  private RouteSegment departureSegment;
  private RouteSegment returnSegment;

  private List<PassengerData> passengers;

  /** @return the paymentParts */
  public List<PaymentPartData> getPaymentParts() {
    return paymentParts;
  }

  /** @param paymentParts the paymentParts to set */
  public void setPaymentParts(List<PaymentPartData> paymentParts) {
    this.paymentParts = paymentParts;
  }

  /** @return the baggage */
  public BaggageData getBaggage() {
    return baggage;
  }

  /** @param baggage the baggage to set */
  public void setBaggage(BaggageData baggage) {
    this.baggage = baggage;
  }

  /** @return the account */
  public String getAccount() {
    return account;
  }

  /** @param account the account to set */
  public void setAccount(String account) {
    this.account = account;
  }

  /** @return the timeZone */
  public String getTimeZone() {
    return timeZone;
  }

  /** @param timeZone the timeZone to set */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /** @return the email */
  public String getEmail() {
    return email;
  }

  /** @param email the email to set */
  public void setEmail(String email) {
    this.email = email;
  }

  /** @return the paymentType */
  public String getPaymentType() {
    return paymentType;
  }

  /** @param paymentType the paymentType to set */
  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  /** @return the paymentMethod */
  public String getPaymentMethod() {
    return paymentMethod;
  }

  /** @param paymentMethod the paymentMethod to set */
  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  /** @return the terminalId */
  public String getTerminalId() {
    return terminalId;
  }

  /** @param terminalId the terminalId to set */
  public void setTerminalId(String terminalId) {
    this.terminalId = terminalId;
  }

  /** @return the approverUsername */
  public String getApproverUsername() {
    return approverUsername;
  }

  /** @param approverUsername the approverUsername to set */
  public void setApproverUsername(String approverUsername) {
    this.approverUsername = approverUsername;
  }

  /** @return the payedAmount */
  public BigDecimal getPayedAmount() {
    return payedAmount;
  }

  /** @param payedAmount the payedAmount to set */
  public void setPayedAmount(BigDecimal payedAmount) {
    this.payedAmount = payedAmount;
  }

  /** @return the changeAmount */
  public BigDecimal getChangeAmount() {
    return changeAmount;
  }

  /** @param changeAmount the changeAmount to set */
  public void setChangeAmount(BigDecimal changeAmount) {
    this.changeAmount = changeAmount;
  }

  /** @return the totalPrice */
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  /** @param totalPrice the totalPrice to set */
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  /** @return the address */
  public Address getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(Address address) {
    this.address = address;
  }

  /** @return the billingAddress */
  public Address getBillingAddress() {
    return billingAddress;
  }

  /** @param billingAddress the billingAddress to set */
  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  /** @return the billingData */
  public BillingData getBillingData() {
    return billingData;
  }

  /** @param billingData the billingData to set */
  public void setBillingData(BillingData billingData) {
    this.billingData = billingData;
  }

  /** @return the needBill */
  public boolean isNeedBill() {
    return needBill;
  }

  /** @param needBill the needBill to set */
  public void setNeedBill(boolean needBill) {
    this.needBill = needBill;
  }

  /** @return the store */
  public StoreRequestData getStore() {
    return store;
  }

  /** @param store the store to set */
  public void setStore(StoreRequestData store) {
    this.store = store;
  }

  /** @return the creditCard */
  public CreditCardData getCreditCard() {
    return creditCard;
  }

  /** @param creditCard the creditCard to set */
  public void setCreditCard(CreditCardData creditCard) {
    this.creditCard = creditCard;
  }

  /** @return the departureTrip */
  public TripData getDepartureTrip() {
    return departureTrip;
  }

  /** @param departureTrip the departureTrip to set */
  public void setDepartureTrip(TripData departureTrip) {
    this.departureTrip = departureTrip;
  }

  /** @return the returnTrip */
  public TripData getReturnTrip() {
    return returnTrip;
  }

  /** @param returnTrip the returnTrip to set */
  public void setReturnTrip(TripData returnTrip) {
    this.returnTrip = returnTrip;
  }

  /** @return the departureSegment */
  public RouteSegment getDepartureSegment() {
    return departureSegment;
  }

  /** @param departureSegment the departureSegment to set */
  public void setDepartureSegment(RouteSegment departureSegment) {
    this.departureSegment = departureSegment;
  }

  /** @return the returnSegment */
  public RouteSegment getReturnSegment() {
    return returnSegment;
  }

  /** @param returnSegment the returnSegment to set */
  public void setReturnSegment(RouteSegment returnSegment) {
    this.returnSegment = returnSegment;
  }

  /** @return the passengers */
  public List<PassengerData> getPassengers() {
    return passengers;
  }

  /** @param passengers the passengers to set */
  public void setPassengers(List<PassengerData> passengers) {
    this.passengers = passengers;
  }

  public String getCoupon() {
    return coupon;
  }

  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }

  public BigDecimal getCouponAmount() {
    return couponAmount;
  }

  public void setCouponAmount(BigDecimal couponAmount) {
    this.couponAmount = couponAmount;
  }
}
