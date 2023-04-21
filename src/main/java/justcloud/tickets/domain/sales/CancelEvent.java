package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.BusPosition;
import justcloud.tickets.domain.tickets.PassengerType;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;

@Entity
public class CancelEvent extends BaseModel {

  @ManyToOne private BusPosition seat;

  @ManyToOne private User cancelUser;

  @ManyToOne private CashCheckpoint cashCheckpoint;

  @ManyToOne private SalesShift saleShift;

  @ManyToOne private StopOff startingStop;

  @ManyToOne private StopOff endingStop;

  @ManyToOne private ClientAccount account;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentPartType paymentType;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PassengerType passengerType;

  @NotNull private BigDecimal soldPrice;

  @NotNull private String seatName;

  @ManyToOne private InternetSale internetSale;

  private String ticketId;

  private String passengerName;

  @ManyToOne private Trip trip;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = false)
  private Date originalDate;

  public User getCancelUser() {
    return cancelUser;
  }

  public void setCancelUser(User cancelUser) {
    this.cancelUser = cancelUser;
  }

  public SalesShift getSaleShift() {
    return saleShift;
  }

  public void setSaleShift(SalesShift saleShift) {
    this.saleShift = saleShift;
  }

  public BusPosition getSeat() {
    return seat;
  }

  public void setSeat(BusPosition seat) {
    this.seat = seat;
  }

  public StopOff getStartingStop() {
    return startingStop;
  }

  public void setStartingStop(StopOff startingStop) {
    this.startingStop = startingStop;
  }

  public StopOff getEndingStop() {
    return endingStop;
  }

  public void setEndingStop(StopOff endingStop) {
    this.endingStop = endingStop;
  }

  public PassengerType getPassengerType() {
    return passengerType;
  }

  public void setPassengerType(PassengerType passengerType) {
    this.passengerType = passengerType;
  }

  public BigDecimal getSoldPrice() {
    return soldPrice;
  }

  public void setSoldPrice(BigDecimal soldPrice) {
    this.soldPrice = soldPrice;
  }

  public String getSeatName() {
    return seatName;
  }

  public void setSeatName(String seatName) {
    this.seatName = seatName;
  }

  public InternetSale getInternetSale() {
    return internetSale;
  }

  public void setInternetSale(InternetSale internetSale) {
    this.internetSale = internetSale;
  }

  public String getTicketId() {
    return ticketId;
  }

  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }

  public String getPassengerName() {
    return passengerName;
  }

  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }

  public ClientAccount getAccount() {
    return account;
  }

  public void setAccount(ClientAccount account) {
    this.account = account;
  }

  public PaymentPartType getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(PaymentPartType paymentType) {
    this.paymentType = paymentType;
  }

  public CashCheckpoint getCashCheckpoint() {
    return cashCheckpoint;
  }

  public void setCashCheckpoint(CashCheckpoint cashCheckpoint) {
    this.cashCheckpoint = cashCheckpoint;
  }

  public Trip getTrip() {
    return trip;
  }

  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  public Date getOriginalDate() {
    return originalDate;
  }

  public void setOriginalDate(Date originalDate) {
    this.originalDate = originalDate;
  }
}
