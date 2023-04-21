package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.sales.InternetSale;

@Entity
public class TripSeat extends BaseModel {

  @Enumerated(EnumType.STRING)
  private SeatStatus status;

  @ManyToOne private InternetSale internetSale;

  @ManyToOne private BusPosition seat;

  @ManyToOne @NotNull private StopOff startingStop;

  @ManyToOne @NotNull private StopOff endingStop;

  @ManyToOne private User user;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PassengerType passengerType;

  @NotNull private BigDecimal soldPrice;

  @NotNull private BigDecimal originalPrice;

  private BigDecimal payedPrice;

  @NotNull private String seatName;

  private String ticketId;

  private String passengerName;

  private String comments;

  @ManyToOne private Trip trip;

  /** @return the status */
  public SeatStatus getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(SeatStatus status) {
    this.status = status;
  }

  /** @return the internetSale */
  public InternetSale getInternetSale() {
    return internetSale;
  }

  /** @param internetSale the internetSale to set */
  public void setInternetSale(InternetSale internetSale) {
    this.internetSale = internetSale;
  }

  /** @return the seat */
  public BusPosition getSeat() {
    return seat;
  }

  /** @param seat the seat to set */
  public void setSeat(BusPosition seat) {
    this.seat = seat;
  }

  /** @return the startingStop */
  public StopOff getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(StopOff startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public StopOff getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(StopOff endingStop) {
    this.endingStop = endingStop;
  }

  /** @return the seatName */
  public String getSeatName() {
    return seatName;
  }

  /** @param seatName the seatName to set */
  public void setSeatName(String seatName) {
    this.seatName = seatName;
  }

  /** @return the ticketId */
  public String getTicketId() {
    return ticketId;
  }

  /** @param ticketId the ticketId to set */
  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }

  /** @return the passengerName */
  public String getPassengerName() {
    return passengerName;
  }

  /** @param passengerName the passengerName to set */
  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }

  /** @return the comments */
  public String getComments() {
    return comments;
  }

  /** @param comments the comments to set */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /** @return the trip */
  public Trip getTrip() {
    return trip;
  }

  /** @param trip the trip to set */
  public void setTrip(Trip trip) {
    this.trip = trip;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal getPayedPrice() {
    return payedPrice;
  }

  public void setPayedPrice(BigDecimal payedPrice) {
    this.payedPrice = payedPrice;
  }

  public BigDecimal getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
  }
}
