package justcloud.tickets.dto;

import java.math.BigDecimal;

public class TicketData {

  private String status;

  private String seatName;

  private String passengerName;

  private String ticketId;

  private BusPositionData position;

  private BigDecimal payment;

  private StopOffData startingStop;

  private StopOffData endingStop;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /** @return the seatName */
  public String getSeatName() {
    return seatName;
  }

  /** @param seatName the seatName to set */
  public void setSeatName(String seatName) {
    this.seatName = seatName;
  }

  /** @return the passengerName */
  public String getPassengerName() {
    return passengerName;
  }

  /** @param passengerName the passengerName to set */
  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }

  /** @return the ticketId */
  public String getTicketId() {
    return ticketId;
  }

  /** @param ticketId the ticketId to set */
  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }

  /** @return the position */
  public BusPositionData getPosition() {
    return position;
  }

  /** @param position the position to set */
  public void setPosition(BusPositionData position) {
    this.position = position;
  }

  /** @return the payment */
  public BigDecimal getPayment() {
    return payment;
  }

  /** @param payment the payment to set */
  public void setPayment(BigDecimal payment) {
    this.payment = payment;
  }

  /** @return the startingStop */
  public StopOffData getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(StopOffData startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public StopOffData getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(StopOffData endingStop) {
    this.endingStop = endingStop;
  }
}
