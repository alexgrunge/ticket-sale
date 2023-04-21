package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class PackageTicket extends BaseModel {

  @ManyToOne private InternetSale sale;

  @NotNull @ManyToOne private User user;

  private String ticketId;

  @ManyToOne private Trip trip;

  @NotNull @ManyToOne private StopOff startingStop;

  @NotNull @ManyToOne private StopOff endingStop;

  @NotNull private BigDecimal price;

  @NotNull private BigDecimal paymentPrice;

  @NotBlank
  @Column(length = 512)
  private String concept;

  @NotBlank
  @Column(length = 512)
  private String senderName;

  @NotBlank
  @Column(length = 512)
  private String receiverName;

  @NotBlank
  @Column(length = 512)
  private String contactData;

  /** @return the sale */
  public InternetSale getSale() {
    return sale;
  }

  /** @param sale the sale to set */
  public void setSale(InternetSale sale) {
    this.sale = sale;
  }

  /** @return the trip */
  public Trip getTrip() {
    return trip;
  }

  /** @param trip the trip to set */
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  /** @return the price */
  public BigDecimal getPrice() {
    return price;
  }

  /** @param price the price to set */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** @return the paymentPrice */
  public BigDecimal getPaymentPrice() {
    return paymentPrice;
  }

  /** @param paymentPrice the paymentPrice to set */
  public void setPaymentPrice(BigDecimal paymentPrice) {
    this.paymentPrice = paymentPrice;
  }

  /** @return the concept */
  public String getConcept() {
    return concept;
  }

  /** @param concept the concept to set */
  public void setConcept(String concept) {
    this.concept = concept;
  }

  /** @return the senderName */
  public String getSenderName() {
    return senderName;
  }

  /** @param senderName the senderName to set */
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  /** @return the receiverName */
  public String getReceiverName() {
    return receiverName;
  }

  /** @param receiverName the receiverName to set */
  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  /** @return the contactData */
  public String getContactData() {
    return contactData;
  }

  /** @param contactData the contactData to set */
  public void setContactData(String contactData) {
    this.contactData = contactData;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTicketId() {
    return ticketId;
  }

  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }
}
