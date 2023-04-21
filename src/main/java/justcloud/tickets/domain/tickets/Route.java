package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.BaseModel;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@ToString(exclude = "stops")
public class Route extends BaseModel {

  private Boolean masterPrice = false;

  @ManyToOne
  @RestResource(path = "beginning")
  private StopOff beginning;

  @ManyToOne
  @RestResource(path = "destination")
  private StopOff destination;

  @OneToMany(mappedBy = "route")
  private List<StopOff> stops;

  @ManyToOne private RouteType routeType;

  private String trackingId;
  private String name;
  private String description;
  private BigDecimal totalKilometers;
  private BigDecimal advance;
  private Boolean hasReverse;

  /** @return the stops */
  public List<StopOff> getStops() {
    return stops;
  }

  /** @param stops the stops to set */
  public void setStops(List<StopOff> stops) {
    this.stops = stops;
  }

  /** @return the routeType */
  public RouteType getRouteType() {
    return routeType;
  }

  /** @param routeType the routeType to set */
  public void setRouteType(RouteType routeType) {
    this.routeType = routeType;
  }

  /** @return the trackingId */
  public String getTrackingId() {
    return trackingId;
  }

  /** @param trackingId the trackingId to set */
  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
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

  /** @return the totalKilometers */
  public BigDecimal getTotalKilometers() {
    return totalKilometers;
  }

  /** @param totalKilometers the totalKilometers to set */
  public void setTotalKilometers(BigDecimal totalKilometers) {
    this.totalKilometers = totalKilometers;
  }

  /** @return the advance */
  public BigDecimal getAdvance() {
    return advance;
  }

  /** @param advance the advance to set */
  public void setAdvance(BigDecimal advance) {
    this.advance = advance;
  }

  /** @return the hasReverse */
  public Boolean getHasReverse() {
    return hasReverse;
  }

  /** @param hasReverse the hasReverse to set */
  public void setHasReverse(Boolean hasReverse) {
    this.hasReverse = hasReverse;
  }
}
