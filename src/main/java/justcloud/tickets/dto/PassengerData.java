package justcloud.tickets.dto;

import java.math.BigDecimal;
import justcloud.tickets.domain.tickets.PassengerType;

public class PassengerData {

  private String name;
  private String originalName;
  private String comments;
  private PassengerType passengerType;
  private BigDecimal departurePrice;
  private BigDecimal returnPrice;
  private BigDecimal originalPrice;

  private String departureSeat;
  private String returnSeat;

  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the comments */
  public String getComments() {
    return comments;
  }

  /** @param comments the comments to set */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /** @return the passengerType */
  public PassengerType getPassengerType() {
    return passengerType;
  }

  /** @param passengerType the passengerType to set */
  public void setPassengerType(PassengerType passengerType) {
    this.passengerType = passengerType;
  }

  /** @return the departurePrice */
  public BigDecimal getDeparturePrice() {
    return departurePrice;
  }

  /** @param departurePrice the departurePrice to set */
  public void setDeparturePrice(BigDecimal departurePrice) {
    this.departurePrice = departurePrice;
  }

  /** @return the returnPrice */
  public BigDecimal getReturnPrice() {
    return returnPrice;
  }

  /** @param returnPrice the returnPrice to set */
  public void setReturnPrice(BigDecimal returnPrice) {
    this.returnPrice = returnPrice;
  }

  /** @return the departureSeat */
  public String getDepartureSeat() {
    return departureSeat;
  }

  /** @param departureSeat the departureSeat to set */
  public void setDepartureSeat(String departureSeat) {
    this.departureSeat = departureSeat;
  }

  /** @return the returnSeat */
  public String getReturnSeat() {
    return returnSeat;
  }

  /** @param returnSeat the returnSeat to set */
  public void setReturnSeat(String returnSeat) {
    this.returnSeat = returnSeat;
  }

  public BigDecimal getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
  }
}
