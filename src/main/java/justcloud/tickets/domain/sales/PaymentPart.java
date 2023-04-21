package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.BaseModel;

@Entity
public class PaymentPart extends BaseModel {

  @ManyToOne private InternetSale sale;

  @ManyToOne private AccountSale accountSale;

  @ManyToOne private ClientAccount account;

  @Enumerated(EnumType.STRING)
  private PaymentPartType paymentType;

  private String reference;
  private BigDecimal amount;
  private BigDecimal change;

  /** @return the sale */
  public InternetSale getSale() {
    return sale;
  }

  /** @param sale the sale to set */
  public void setSale(InternetSale sale) {
    this.sale = sale;
  }

  /** @return the accountSale */
  public AccountSale getAccountSale() {
    return accountSale;
  }

  /** @param accountSale the accountSale to set */
  public void setAccountSale(AccountSale accountSale) {
    this.accountSale = accountSale;
  }

  /** @return the account */
  public ClientAccount getAccount() {
    return account;
  }

  /** @param account the account to set */
  public void setAccount(ClientAccount account) {
    this.account = account;
  }

  /** @return the paymentType */
  public PaymentPartType getPaymentType() {
    return paymentType;
  }

  /** @param paymentType the paymentType to set */
  public void setPaymentType(PaymentPartType paymentType) {
    this.paymentType = paymentType;
  }

  /** @return the reference */
  public String getReference() {
    return reference;
  }

  /** @param reference the reference to set */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /** @return the amount */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @param amount the amount to set */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** @return the change */
  public BigDecimal getChange() {
    return change;
  }

  /** @param change the change to set */
  public void setChange(BigDecimal change) {
    this.change = change;
  }
}
