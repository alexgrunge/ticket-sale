package justcloud.tickets.dto;

public class BillingData {

  private String rfc;

  private String name;

  /** @return the rfc */
  public String getRfc() {
    return rfc;
  }

  /** @param rfc the rfc to set */
  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }
}
