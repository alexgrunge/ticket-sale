package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.tickets.StopOff;
import justcloud.tickets.domain.tickets.Trip;

@Entity
public class CancelPackageEvent extends BaseModel {

  @ManyToOne private User cancelUser;

  @ManyToOne private CashCheckpoint cashCheckpoint;

  @ManyToOne private SalesShift saleShift;

  @ManyToOne private StopOff startingStop;

  @ManyToOne private StopOff endingStop;

  @ManyToOne private ClientAccount account;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentPartType paymentType;

  @NotNull private BigDecimal soldPrice;

  @ManyToOne private InternetSale internetSale;

  @ManyToOne private Trip trip;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = true, updatable = false)
  private Date originalDate;

  private String senderName;

  private String receiverName;

  private String concept;

  private String contactData;

  public User getCancelUser() {
    return cancelUser;
  }

  public void setCancelUser(User cancelUser) {
    this.cancelUser = cancelUser;
  }

  public CashCheckpoint getCashCheckpoint() {
    return cashCheckpoint;
  }

  public void setCashCheckpoint(CashCheckpoint cashCheckpoint) {
    this.cashCheckpoint = cashCheckpoint;
  }

  public SalesShift getSaleShift() {
    return saleShift;
  }

  public void setSaleShift(SalesShift saleShift) {
    this.saleShift = saleShift;
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

  public BigDecimal getSoldPrice() {
    return soldPrice;
  }

  public void setSoldPrice(BigDecimal soldPrice) {
    this.soldPrice = soldPrice;
  }

  public InternetSale getInternetSale() {
    return internetSale;
  }

  public void setInternetSale(InternetSale internetSale) {
    this.internetSale = internetSale;
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

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public String getConcept() {
    return concept;
  }

  public void setConcept(String concept) {
    this.concept = concept;
  }

  public String getContactData() {
    return contactData;
  }

  public void setContactData(String contactData) {
    this.contactData = contactData;
  }
}
