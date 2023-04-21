package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.Employee;
import justcloud.tickets.domain.sales.JoinedPayment;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
public class Trip extends BaseModel {

  @RestResource(path = "seats")
  @OneToMany(orphanRemoval = true)
  @JoinTable(
      name = "trip_trip_seat",
      joinColumns = @JoinColumn(name = "trip_seats_id"),
      inverseJoinColumns = @JoinColumn(name = "trip_seat_id"))
  private List<TripSeat> seats;

  @ManyToOne private JoinedPayment joinedPayment;

  @ManyToOne private ServiceLevelType serviceLevelType;

  @Enumerated(EnumType.STRING)
  private TripStatus status;

  @ManyToOne private Run run;

  @ManyToOne private Bus bus;

  @ManyToOne private Employee driver1;

  @ManyToOne private Employee driver2;

  private String packages;
  private BigDecimal dieselLiters;
  private BigDecimal dieselCost;
  private Boolean packageArrived;
  private Boolean hasAllStamps;
  private Boolean hasAllPlaces;

  private Date departureDate;
  private Date estimatedArrival;

  private Boolean offRoute;
  private Boolean currentlyTravelling;
  private Integer delayedMinutes;
  private Long totalTravelMinutes;

  private Integer soldTickets;
  private Boolean reverse;
  private Boolean guideGenerated;

  /** @return the seats */
  public List<TripSeat> getSeats() {
    return seats;
  }

  /** @param seats the seats to set */
  public void setSeats(List<TripSeat> seats) {
    this.seats = seats;
  }

  /** @return the serviceLevelType */
  public ServiceLevelType getServiceLevelType() {
    return serviceLevelType;
  }

  /** @param serviceLevelType the serviceLevelType to set */
  public void setServiceLevelType(ServiceLevelType serviceLevelType) {
    this.serviceLevelType = serviceLevelType;
  }

  /** @return the status */
  public TripStatus getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(TripStatus status) {
    this.status = status;
  }

  /** @return the run */
  public Run getRun() {
    return run;
  }

  /** @param run the run to set */
  public void setRun(Run run) {
    this.run = run;
  }

  /** @return the bus */
  public Bus getBus() {
    return bus;
  }

  /** @param bus the bus to set */
  public void setBus(Bus bus) {
    this.bus = bus;
  }

  /** @return the driver1 */
  public Employee getDriver1() {
    return driver1;
  }

  /** @param driver1 the driver1 to set */
  public void setDriver1(Employee driver1) {
    this.driver1 = driver1;
  }

  /** @return the driver2 */
  public Employee getDriver2() {
    return driver2;
  }

  /** @param driver2 the driver2 to set */
  public void setDriver2(Employee driver2) {
    this.driver2 = driver2;
  }

  /** @return the packages */
  public String getPackages() {
    return packages;
  }

  /** @param packages the packages to set */
  public void setPackages(String packages) {
    this.packages = packages;
  }

  /** @return the dieselLiters */
  public BigDecimal getDieselLiters() {
    return dieselLiters;
  }

  /** @param dieselLiters the dieselLiters to set */
  public void setDieselLiters(BigDecimal dieselLiters) {
    this.dieselLiters = dieselLiters;
  }

  /** @return the packageArrived */
  public Boolean getPackageArrived() {
    return packageArrived;
  }

  /** @param packageArrived the packageArrived to set */
  public void setPackageArrived(Boolean packageArrived) {
    this.packageArrived = packageArrived;
  }

  /** @return the hasAllStamps */
  public Boolean getHasAllStamps() {
    return hasAllStamps;
  }

  /** @param hasAllStamps the hasAllStamps to set */
  public void setHasAllStamps(Boolean hasAllStamps) {
    this.hasAllStamps = hasAllStamps;
  }

  /** @return the hasAllPlaces */
  public Boolean getHasAllPlaces() {
    return hasAllPlaces;
  }

  /** @param hasAllPlaces the hasAllPlaces to set */
  public void setHasAllPlaces(Boolean hasAllPlaces) {
    this.hasAllPlaces = hasAllPlaces;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the estimatedArrival */
  public Date getEstimatedArrival() {
    return estimatedArrival;
  }

  /** @param estimatedArrival the estimatedArrival to set */
  public void setEstimatedArrival(Date estimatedArrival) {
    this.estimatedArrival = estimatedArrival;
  }

  /** @return the offRoute */
  public Boolean getOffRoute() {
    return offRoute;
  }

  /** @param offRoute the offRoute to set */
  public void setOffRoute(Boolean offRoute) {
    this.offRoute = offRoute;
  }

  /** @return the currentlyTravelling */
  public Boolean getCurrentlyTravelling() {
    return currentlyTravelling;
  }

  /** @param currentlyTravelling the currentlyTravelling to set */
  public void setCurrentlyTravelling(Boolean currentlyTravelling) {
    this.currentlyTravelling = currentlyTravelling;
  }

  /** @return the delayedMinutes */
  public Integer getDelayedMinutes() {
    return delayedMinutes;
  }

  /** @param delayedMinutes the delayedMinutes to set */
  public void setDelayedMinutes(Integer delayedMinutes) {
    this.delayedMinutes = delayedMinutes;
  }

  /** @return the totalTravelMinutes */
  public Long getTotalTravelMinutes() {
    return totalTravelMinutes;
  }

  /** @param totalTravelMinutes the totalTravelMinutes to set */
  public void setTotalTravelMinutes(Long totalTravelMinutes) {
    this.totalTravelMinutes = totalTravelMinutes;
  }

  /** @return the soldTickets */
  public Integer getSoldTickets() {
    return soldTickets;
  }

  /** @param soldTickets the soldTickets to set */
  public void setSoldTickets(Integer soldTickets) {
    this.soldTickets = soldTickets;
  }

  /** @return the reverse */
  public Boolean getReverse() {
    return reverse;
  }

  /** @param reverse the reverse to set */
  public void setReverse(Boolean reverse) {
    this.reverse = reverse;
  }

  /** @return the guideGenerated */
  public Boolean getGuideGenerated() {
    return guideGenerated;
  }

  /** @param guideGenerated the guideGenerated to set */
  public void setGuideGenerated(Boolean guideGenerated) {
    this.guideGenerated = guideGenerated;
  }

  public BigDecimal getDieselCost() {
    return dieselCost;
  }

  public void setDieselCost(BigDecimal dieselCost) {
    this.dieselCost = dieselCost;
  }

  public JoinedPayment getJoinedPayment() {
    return joinedPayment;
  }

  public void setJoinedPayment(JoinedPayment joinedPayment) {
    this.joinedPayment = joinedPayment;
  }
}
