package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Entity;
import justcloud.tickets.domain.BaseModel;

@Entity
public class ServiceLevelType extends BaseModel {

  private String name;

  private BigDecimal extraPercentage;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the extraPercentage */
  public BigDecimal getExtraPercentage() {
    return extraPercentage;
  }

  /** @param extraPercentage the extraPercentage to set */
  public void setExtraPercentage(BigDecimal extraPercentage) {
    this.extraPercentage = extraPercentage;
  }
}
