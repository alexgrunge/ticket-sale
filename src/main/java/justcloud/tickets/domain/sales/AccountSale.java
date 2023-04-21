package justcloud.tickets.domain.sales;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;

@Entity
public class AccountSale extends BaseModel {

  @OneToMany(mappedBy = "accountSale")
  private List<PaymentPart> paymentParts;

  @ManyToOne private ClientAccount clientAccount;

  @ManyToOne private SalesTerminal salesTerminal;

  @ManyToOne private SalesShift salesShift;

  @ManyToOne private InternetSale internetSale;

  @ManyToOne private CashCheckpoint cashCheckpoint;

  @ManyToOne private User salesman;

  public CashCheckpoint getCashCheckpoint() {
    return cashCheckpoint;
  }

  public void setCashCheckpoint(CashCheckpoint cashCheckpoint) {
    this.cashCheckpoint = cashCheckpoint;
  }

  public SalesShift getSalesShift() {
    return salesShift;
  }

  public void setSalesShift(SalesShift salesShift) {
    this.salesShift = salesShift;
  }

  /** @return the paymentParts */
  public List<PaymentPart> getPaymentParts() {
    return paymentParts;
  }

  /** @param paymentParts the paymentParts to set */
  public void setPaymentParts(List<PaymentPart> paymentParts) {
    this.paymentParts = paymentParts;
  }

  /** @return the clientAccount */
  public ClientAccount getClientAccount() {
    return clientAccount;
  }

  /** @param clientAccount the clientAccount to set */
  public void setClientAccount(ClientAccount clientAccount) {
    this.clientAccount = clientAccount;
  }

  /** @return the salesTerminal */
  public SalesTerminal getSalesTerminal() {
    return salesTerminal;
  }

  /** @param salesTerminal the salesTerminal to set */
  public void setSalesTerminal(SalesTerminal salesTerminal) {
    this.salesTerminal = salesTerminal;
  }

  /** @return the salesman */
  public User getSalesman() {
    return salesman;
  }

  /** @param salesman the salesman to set */
  public void setSalesman(User salesman) {
    this.salesman = salesman;
  }

  public InternetSale getInternetSale() {
    return internetSale;
  }

  public void setInternetSale(InternetSale internetSale) {
    this.internetSale = internetSale;
  }
}
