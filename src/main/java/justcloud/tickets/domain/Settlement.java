package justcloud.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Settlement extends BaseModel {

  @NotNull @ManyToOne private Municipality municipality;

  @NotNull @ManyToOne private State state;

  @NotBlank private String name;

  @NotBlank private String postalCode;

  /** @RETURN the municipality */
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

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the postalCode */
  public String getPostalCode() {
    return postalCode;
  }

  /** @param postalCode the postalCode to set */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
}
