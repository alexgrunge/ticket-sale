package justcloud.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Address extends BaseModel {

  @NotBlank private String postalCode;

  @NotBlank private String address;

  @ManyToOne private Settlement settlement;

  @ManyToOne private Municipality municipality;

  @ManyToOne private State state;

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
  public Settlement getSettlement() {
    return settlement;
  }

  /** @param settlement the settlement to set */
  public void setSettlement(Settlement settlement) {
    this.settlement = settlement;
  }

  /** @return the municipality */
  public Municipality getMunicipality() {
    return municipality;
  }

  /** @param municipality the municipality to set */
  public void setMunicipality(Municipality municipality) {
    this.municipality = municipality;
  }

  /** @return the state */
  public State getState() {
    return state;
  }

  /** @param state the state to set */
  public void setState(State state) {
    this.state = state;
  }
}
