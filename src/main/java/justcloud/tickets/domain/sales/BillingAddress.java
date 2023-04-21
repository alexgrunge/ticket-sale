package justcloud.tickets.domain.sales;

import javax.persistence.Entity;
import justcloud.tickets.domain.BaseModel;

@Entity
public class BillingAddress extends BaseModel {

  private String postalCode;
  private String address;
  private String settlement;
  private String block;
  private String municipality;
  private String state;
  private String country;

  /** @return the postalCode */
  public String getPostalCode() {
    return postalCode;
  }

  /** @param postalCode the postalCode to set */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /** @return the address */
  public String getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(String address) {
    this.address = address;
  }

  /** @return the settlement */
  public String getSettlement() {
    return settlement;
  }

  /** @param settlement the settlement to set */
  public void setSettlement(String settlement) {
    this.settlement = settlement;
  }

  /** @return the block */
  public String getBlock() {
    return block;
  }

  /** @param block the block to set */
  public void setBlock(String block) {
    this.block = block;
  }

  /** @return the municipality */
  public String getMunicipality() {
    return municipality;
  }

  /** @param municipality the municipality to set */
  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  /** @return the state */
  public String getState() {
    return state;
  }

  /** @param state the state to set */
  public void setState(String state) {
    this.state = state;
  }

  /** @return the country */
  public String getCountry() {
    return country;
  }

  /** @param country the country to set */
  public void setCountry(String country) {
    this.country = country;
  }
}
