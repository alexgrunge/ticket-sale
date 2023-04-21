package justcloud.tickets.domain.tickets;

import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.sales.Product;

@Entity
@DiscriminatorValue("justcloud.tickets.domain.tickets.Run")
public class Run extends Product {

  @ManyToMany
  @JoinTable(
      name = "run_stop_off",
      joinColumns = @JoinColumn(name = "run_stops_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "stop_off_id", referencedColumnName = "id"))
  private List<StopOff> stops;

  @OneToMany
  @JoinTable(
      name = "run_service_type_time",
      joinColumns = @JoinColumn(name = "run_service_type_times_id"),
      inverseJoinColumns = @JoinColumn(name = "service_type_time_id"))
  private List<ServiceTypeTime> serviceTypeTimes;

  @ManyToOne private Route route;

  @ManyToOne private StopOff beginning;

  @ManyToOne private StopOff destination;

  private Date validFrom;
  private Date validTo;

  /** @return the stops */
  public List<StopOff> getStops() {
    return stops;
  }

  /** @param stops the stops to set */
  public void setStops(List<StopOff> stops) {
    this.stops = stops;
  }

  /** @return the serviceTypeTimes */
  public List<ServiceTypeTime> getServiceTypeTimes() {
    return serviceTypeTimes;
  }

  /** @param serviceTypeTimes the serviceTypeTimes to set */
  public void setServiceTypeTimes(List<ServiceTypeTime> serviceTypeTimes) {
    this.serviceTypeTimes = serviceTypeTimes;
  }

  /** @return the route */
  public Route getRoute() {
    return route;
  }

  /** @param route the route to set */
  public void setRoute(Route route) {
    this.route = route;
  }

  /** @return the beginning */
  public StopOff getBeginning() {
    return beginning;
  }

  /** @param beginning the beginning to set */
  public void setBeginning(StopOff beginning) {
    this.beginning = beginning;
  }

  /** @return the destination */
  public StopOff getDestination() {
    return destination;
  }

  /** @param destination the destination to set */
  public void setDestination(StopOff destination) {
    this.destination = destination;
  }

  /** @return the validFrom */
  public Date getValidFrom() {
    return validFrom;
  }

  /** @param validFrom the validFrom to set */
  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  /** @return the validTo */
  public Date getValidTo() {
    return validTo;
  }

  /** @param validTo the validTo to set */
  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }
}
