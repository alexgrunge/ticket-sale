package justcloud.tickets.domain.tickets;

import javax.persistence.Entity;
import justcloud.tickets.domain.BaseModel;

@Entity
public class RouteType extends BaseModel {
  private String name;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }
}
