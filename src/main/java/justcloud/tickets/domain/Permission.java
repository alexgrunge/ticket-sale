package justcloud.tickets.domain;

import javax.persistence.*;

@Entity
public class Permission extends BaseModel {

  private String name;
  private String description;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the description */
  public String getDescription() {
    return description;
  }

  /** @param description the description to set */
  public void setDescription(String description) {
    this.description = description;
  }
}
