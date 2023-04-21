package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class InternetSale extends BaseModel {

  @OneToMany(mappedBy = "sale")
  private List<PaymentPart> paymentParts;

  @OneToMany(mappedBy = "sale")
  private List<PackageTicket> packages;

  @ManyToOne private CashCheckpoint cashCheckpoint;

  @ManyToOne private SalesShift salesShift;

  private String saleNumber;

  @NotBlank private String email;
  private String shortId;
  private String timeZone;
  private String paymentProvider;
  private String paymentId;
  private String paymentType;
  private BigDecimal totalAmount;
  private BigDecimal payedAmount;
  private BigDecimal changeAmount;
  private Boolean payed;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String fullResponse;

  @ManyToOne private User salesman;

  @ManyToOne private User approver;

  @ManyToOne private SalesTerminal salesTerminal;

  @ManyToOne private BillingAddress billingAddress;

  @ManyToOne private BillingData billingData;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String billPdf;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String billXml;

  private Boolean bill;

  public String getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(String saleNumber) {
    this.saleNumber = saleNumber;
  }

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

  /** @return the packages */
  public List<PackageTicket> getPackages() {
    return packages;
  }

  /** @param packages the packages to set */
  public void setPackages(List<PackageTicket> packages) {
    this.packages = packages;
  }

  /** @return the email */
  public String getEmail() {
    return email;
  }

  /** @param email the email to set */
  public void setEmail(String email) {
    this.email = email;
  }

  /** @return the shortId */
  public String getShortId() {
    return shortId;
  }

  /** @param shortId the shortId to set */
  public void setShortId(String shortId) {
    this.shortId = shortId;
  }

  /** @return the timeZone */
  public String getTimeZone() {
    return timeZone;
  }

  /** @param timeZone the timeZone to set */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /** @return the paymentProvider */
  public String getPaymentProvider() {
    return paymentProvider;
  }

  /** @param paymentProvider the paymentProvider to set */
  public void setPaymentProvider(String paymentProvider) {
    this.paymentProvider = paymentProvider;
  }

  /** @return the paymentId */
  public String getPaymentId() {
    return paymentId;
  }

  /** @param paymentId the paymentId to set */
  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  /** @return the paymentType */
  public String getPaymentType() {
    return paymentType;
  }

  /** @param paymentType the paymentType to set */
  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  /** @return the totalAmount */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  /** @param totalAmount the totalAmount to set */
  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  /** @return the payedAmount */
  public BigDecimal getPayedAmount() {
    return payedAmount;
  }

  /** @param payedAmount the payedAmount to set */
  public void setPayedAmount(BigDecimal payedAmount) {
    this.payedAmount = payedAmount;
  }

  /** @return the changeAmount */
  public BigDecimal getChangeAmount() {
    return changeAmount;
  }

  /** @param changeAmount the changeAmount to set */
  public void setChangeAmount(BigDecimal changeAmount) {
    this.changeAmount = changeAmount;
  }

  /** @return the payed */
  public Boolean getPayed() {
    if (payed == null) {
      return false;
    }
    return payed;
  }

  /** @param payed the payed to set */
  public void setPayed(Boolean payed) {
    this.payed = payed;
  }

  /** @return the fullResponse */
  public String getFullResponse() {
    return fullResponse;
  }

  /** @param fullResponse the fullResponse to set */
  public void setFullResponse(String fullResponse) {
    this.fullResponse = fullResponse;
  }

  /** @return the salesman */
  public User getSalesman() {
    return salesman;
  }

  /** @param salesman the salesman to set */
  public void setSalesman(User salesman) {
    this.salesman = salesman;
  }

  /** @return the approver */
  public User getApprover() {
    return approver;
  }

  /** @param approver the approver to set */
  public void setApprover(User approver) {
    this.approver = approver;
  }

  /** @return the salesTerminal */
  public SalesTerminal getSalesTerminal() {
    return salesTerminal;
  }

  /** @param salesTerminal the salesTerminal to set */
  public void setSalesTerminal(SalesTerminal salesTerminal) {
    this.salesTerminal = salesTerminal;
  }

  /** @return the billingAddress */
  public BillingAddress getBillingAddress() {
    return billingAddress;
  }

  /** @param billingAddress the billingAddress to set */
  public void setBillingAddress(BillingAddress billingAddress) {
    this.billingAddress = billingAddress;
  }

  /** @return the billingData */
  public BillingData getBillingData() {
    return billingData;
  }

  /** @param billingData the billingData to set */
  public void setBillingData(BillingData billingData) {
    this.billingData = billingData;
  }

  /** @return the billPdf */
  public String getBillPdf() {
    return billPdf;
  }

  /** @param billPdf the billPdf to set */
  public void setBillPdf(String billPdf) {
    this.billPdf = billPdf;
  }

  /** @return the bill */
  public Boolean getBill() {
    return bill;
  }

  /** @param bill the bill to set */
  public void setBill(Boolean bill) {
    this.bill = bill;
  }

  public String getBillXml() {
    return billXml;
  }

  public void setBillXml(String billXml) {
    this.billXml = billXml;
  }
}
