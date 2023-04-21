package justcloud.tickets.dto;

import java.util.Date;

public class StopOffData {

  private String name;

  private String id;

  private Date stopDate;

  private String routeName;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getStopDate() {
    return stopDate;
  }

  public void setStopDate(Date stopDate) {
    this.stopDate = stopDate;
  }

  public String getRouteName() {
    return routeName;
  }

  public void setRouteName(String routeName) {
    this.routeName = routeName;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof StopOffData)) return false;
    StopOffData other = (StopOffData) obj;
    return name.equals(other.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
