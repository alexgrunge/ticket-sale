package justcloud.tickets.dto;

public class BillRequest {
  private Address billingAddress;
  private BillingData billingData;

  /** @return the billingAddress */
  public Address getBillingAddress() {
    return billingAddress;
  }

  /** @param billingAddress the billingAddress to set */
  public void setBillingAddress(Address billingAddress) {
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
}
