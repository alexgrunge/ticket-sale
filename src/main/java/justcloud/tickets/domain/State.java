package justcloud.tickets.domain;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class State extends BaseModel {

  @NotBlank private String name;

  private String isoCode;

  @OneToMany(mappedBy = "state")
  private List<Municipality> municipalities;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the isoCode */
  public String getIsoCode() {
    return isoCode;
  }

  /** @param isoCode the isoCode to set */
  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

  /** @return the municipalities */
  public List<Municipality> getMunicipalities() {
    return municipalities;
  }

  /** @param municipalities the municipalities to set */
  public void setMunicipalities(List<Municipality> municipalities) {
    this.municipalities = municipalities;
  }
}
