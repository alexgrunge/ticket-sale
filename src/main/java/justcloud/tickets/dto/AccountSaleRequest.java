package justcloud.tickets.dto;

import java.util.List;

public class AccountSaleRequest {

  private List<PaymentPartData> paymentParts;

  private String name;

  private String cardNumber;

  private String terminalId;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  /** @return the paymentParts */
  public List<PaymentPartData> getPaymentParts() {
    return paymentParts;
  }

  /** @param paymentParts the paymentParts to set */
  public void setPaymentParts(List<PaymentPartData> paymentParts) {
    this.paymentParts = paymentParts;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the terminalId */
  public String getTerminalId() {
    return terminalId;
  }

  /** @param terminalId the terminalId to set */
  public void setTerminalId(String terminalId) {
    this.terminalId = terminalId;
  }
}
