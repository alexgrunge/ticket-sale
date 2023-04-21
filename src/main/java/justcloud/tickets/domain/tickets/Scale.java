package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Entity;
import justcloud.tickets.domain.BaseModel;

@Entity
public class Scale extends BaseModel {

  private String name;
  private String description;

  private BigDecimal latitude;
  private BigDecimal longitude;

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

  /** @return the latitude */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /** @param latitude the latitude to set */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /** @return the longitude */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /** @param longitude the longitude to set */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }
}
