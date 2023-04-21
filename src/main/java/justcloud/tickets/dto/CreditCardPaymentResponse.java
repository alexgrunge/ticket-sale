package justcloud.tickets.dto;

public class CreditCardPaymentResponse extends PaymentResponseData {

  private String cardNumber;
  private String cardType;
  private String name;
  private String lastNames;
  private String cvt;
  private Integer expirationMonth;
  private Integer expirationYear;
  private String authorization;

  private Address address;

  @Override
  public String getPaymentProvider() {
    return "PAGOFACIL";
  }

  @Override
  public PaymentType getPaymentType() {
    return PaymentType.CREDIT_CARD;
  }

  @Override
  public boolean getDelayedResponse() {
    return false;
  }

  /** @return the cardNumber */
  public String getCardNumber() {
    return cardNumber;
  }

  /** @param cardNumber the cardNumber to set */
  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  /** @return the cardType */
  public String getCardType() {
    return cardType;
  }

  /** @param cardType the cardType to set */
  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the lastNames */
  public String getLastNames() {
    return lastNames;
  }

  /** @param lastNames the lastNames to set */
  public void setLastNames(String lastNames) {
    this.lastNames = lastNames;
  }

  /** @return the cvt */
  public String getCvt() {
    return cvt;
  }

  /** @param cvt the cvt to set */
  public void setCvt(String cvt) {
    this.cvt = cvt;
  }

  /** @return the expirationMonth */
  public Integer getExpirationMonth() {
    return expirationMonth;
  }

  /** @param expirationMonth the expirationMonth to set */
  public void setExpirationMonth(Integer expirationMonth) {
    this.expirationMonth = expirationMonth;
  }

  /** @return the expirationYear */
  public Integer getExpirationYear() {
    return expirationYear;
  }

  /** @param expirationYear the expirationYear to set */
  public void setExpirationYear(Integer expirationYear) {
    this.expirationYear = expirationYear;
  }

  /** @return the authorization */
  public String getAuthorization() {
    return authorization;
  }

  /** @param authorization the authorization to set */
  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }

  /** @return the address */
  public Address getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(Address address) {
    this.address = address;
  }
}
