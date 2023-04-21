package justcloud.tickets.domain;

import javax.persistence.Entity;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class ScriptCategory extends BaseModel {

  @NotBlank private String name;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }
}
