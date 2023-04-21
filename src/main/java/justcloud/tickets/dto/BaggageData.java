package justcloud.tickets.dto;

import java.math.BigDecimal;

public class BaggageData {

  private String concept;
  private String contactData;
  private String receiverName;
  private String senderName;
  private BigDecimal totalPrice;

  private BigDecimal paymentPrice;

  /** @return the concept */
  public String getConcept() {
    return concept;
  }

  /** @param concept the concept to set */
  public void setConcept(String concept) {
    this.concept = concept;
  }

  /** @return the contactData */
  public String getContactData() {
    return contactData;
  }

  /** @param contactData the contactData to set */
  public void setContactData(String contactData) {
    this.contactData = contactData;
  }

  /** @return the receiverName */
  public String getReceiverName() {
    return receiverName;
  }

  /** @param receiverName the receiverName to set */
  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  /** @return the senderName */
  public String getSenderName() {
    return senderName;
  }

  /** @param senderName the senderName to set */
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  /** @return the totalPrice */
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  /** @param totalPrice the totalPrice to set */
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  /** @return the paymentPrice */
  public BigDecimal getPaymentPrice() {
    return paymentPrice;
  }

  /** @param paymentPrice the paymentPrice to set */
  public void setPaymentPrice(BigDecimal paymentPrice) {
    this.paymentPrice = paymentPrice;
  }
}
