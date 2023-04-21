package justcloud.tickets.domain.sales;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class BillingData extends BaseModel {

  @NotNull @NotBlank private String name;
  private String rfc;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the rfc */
  public String getRfc() {
    return rfc;
  }

  /** @param rfc the rfc to set */
  public void setRfc(String rfc) {
    this.rfc = rfc;
  }
}
