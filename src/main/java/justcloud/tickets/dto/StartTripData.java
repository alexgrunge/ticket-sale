package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class StartTripData {

  private String id;
  private String busName;
  private String routeName;
  private DriverData driver1;
  private DriverData driver2;
  private StopOffData startingStop;
  private StopOffData endingStop;
  private BigDecimal advance;
  private Date departureDate;
  private Date estimatedArrivalDate;

  public BigDecimal getAdvance() {
    return advance;
  }

  public void setAdvance(BigDecimal advance) {
    this.advance = advance;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the busName */
  public String getBusName() {
    return busName;
  }

  /** @param busName the busName to set */
  public void setBusName(String busName) {
    this.busName = busName;
  }

  /** @return the routeName */
  public String getRouteName() {
    return routeName;
  }

  /** @param routeName the routeName to set */
  public void setRouteName(String routeName) {
    this.routeName = routeName;
  }

  /** @return the driver1 */
  public DriverData getDriver1() {
    return driver1;
  }

  /** @param driver1 the driver1 to set */
  public void setDriver1(DriverData driver1) {
    this.driver1 = driver1;
  }

  /** @return the driver2 */
  public DriverData getDriver2() {
    return driver2;
  }

  /** @param driver2 the driver2 to set */
  public void setDriver2(DriverData driver2) {
    this.driver2 = driver2;
  }

  /** @return the startingStop */
  public StopOffData getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(StopOffData startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public StopOffData getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(StopOffData endingStop) {
    this.endingStop = endingStop;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the estimatedArrivalDate */
  public Date getEstimatedArrivalDate() {
    return estimatedArrivalDate;
  }

  /** @param estimatedArrivalDate the estimatedArrivalDate to set */
  public void setEstimatedArrivalDate(Date estimatedArrivalDate) {
    this.estimatedArrivalDate = estimatedArrivalDate;
  }
}
