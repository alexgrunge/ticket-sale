package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.BaseModel;
import lombok.ToString;

@Entity
@ToString(exclude = "route")
public class StopOff extends BaseModel {

  @ManyToOne private Route route;

  @ManyToOne private Scale scale;

  private String name;
  private String description;
  private BigDecimal kilometers;
  private BigDecimal stopPrice;

  private Boolean checkpoint;
  private Boolean notNecessary;

  private Long travelMinutes;
  private Long waitingMinutes;
  private Long missingMinutes;

  private Long orderIndex;

  /** @return the route */
  public Route getRoute() {
    return route;
  }

  /** @param route the route to set */
  public void setRoute(Route route) {
    this.route = route;
  }

  /** @return the scale */
  public Scale getScale() {
    return scale;
  }

  /** @param scale the scale to set */
  public void setScale(Scale scale) {
    this.scale = scale;
  }

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

  /** @return the kilometers */
  public BigDecimal getKilometers() {
    return kilometers;
  }

  /** @param kilometers the kilometers to set */
  public void setKilometers(BigDecimal kilometers) {
    this.kilometers = kilometers;
  }

  /** @return the stopPrice */
  public BigDecimal getStopPrice() {
    return stopPrice;
  }

  /** @param stopPrice the stopPrice to set */
  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }

  /** @return the checkpoint */
  public Boolean getCheckpoint() {
    return checkpoint;
  }

  /** @param checkpoint the checkpoint to set */
  public void setCheckpoint(Boolean checkpoint) {
    this.checkpoint = checkpoint;
  }

  /** @return the travelMinutes */
  public Long getTravelMinutes() {
    return travelMinutes;
  }

  /** @param travelMinutes the travelMinutes to set */
  public void setTravelMinutes(Long travelMinutes) {
    this.travelMinutes = travelMinutes;
  }

  /** @return the waitingMinutes */
  public Long getWaitingMinutes() {
    return waitingMinutes;
  }

  /** @param waitingMinutes the waitingMinutes to set */
  public void setWaitingMinutes(Long waitingMinutes) {
    this.waitingMinutes = waitingMinutes;
  }

  /** @return the missingMinutes */
  public Long getMissingMinutes() {
    return missingMinutes;
  }

  /** @param missingMinutes the missingMinutes to set */
  public void setMissingMinutes(Long missingMinutes) {
    this.missingMinutes = missingMinutes;
  }

  /** @return the orderIndex */
  public Long getOrderIndex() {
    return orderIndex;
  }

  /** @param orderIndex the orderIndex to set */
  public void setOrderIndex(Long orderIndex) {
    this.orderIndex = orderIndex;
  }

  public Boolean getNotNecessary() {
    return notNecessary;
  }

  public void setNotNecessary(Boolean notNecessary) {
    this.notNecessary = notNecessary;
  }
}
