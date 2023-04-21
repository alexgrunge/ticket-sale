package justcloud.tickets.domain;

import javax.persistence.Entity;

@Entity
public class EmployeePosition extends BaseModel {

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
