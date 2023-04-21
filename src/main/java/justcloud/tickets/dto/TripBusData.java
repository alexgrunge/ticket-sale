package justcloud.tickets.dto;

import java.util.Date;

public class TripBusData {

  private String id;

  private String runId;
  private String busId;
  private String busPlates;
  private String busNumber;

  private DriverData driver1;
  private DriverData driver2;

  private RouteData route;

  private ServiceTypeData serviceType;

  private Date departureDate;

  private String originId;
  private String originName;

  private String destinationId;
  private String destinationName;

  private Boolean reverse;

  public ServiceTypeData getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceTypeData serviceType) {
    this.serviceType = serviceType;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the busId */
  public String getBusId() {
    return busId;
  }

  /** @param busId the busId to set */
  public void setBusId(String busId) {
    this.busId = busId;
  }

  /** @return the runId */
  public String getRunId() {
    return runId;
  }

  /** @param runId the runId to set */
  public void setRunId(String runId) {
    this.runId = runId;
  }

  /** @return the busPlates */
  public String getBusPlates() {
    return busPlates;
  }

  /** @param busPlates the busPlates to set */
  public void setBusPlates(String busPlates) {
    this.busPlates = busPlates;
  }

  /** @return the busNumber */
  public String getBusNumber() {
    return busNumber;
  }

  /** @param busNumber the busNumber to set */
  public void setBusNumber(String busNumber) {
    this.busNumber = busNumber;
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

  /** @return the route */
  public RouteData getRoute() {
    return route;
  }

  /** @param route the route to set */
  public void setRoute(RouteData route) {
    this.route = route;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the originId */
  public String getOriginId() {
    return originId;
  }

  /** @param originId the originId to set */
  public void setOriginId(String originId) {
    this.originId = originId;
  }

  /** @return the originName */
  public String getOriginName() {
    return originName;
  }

  /** @param originName the originName to set */
  public void setOriginName(String originName) {
    this.originName = originName;
  }

  /** @return the destinationId */
  public String getDestinationId() {
    return destinationId;
  }

  /** @param destinationId the destinationId to set */
  public void setDestinationId(String destinationId) {
    this.destinationId = destinationId;
  }

  /** @return the destinationName */
  public String getDestinationName() {
    return destinationName;
  }

  /** @param destinationName the destinationName to set */
  public void setDestinationName(String destinationName) {
    this.destinationName = destinationName;
  }

  /** @return the reverse */
  public Boolean getReverse() {
    return reverse;
  }

  /** @param reverse the reverse to set */
  public void setReverse(Boolean reverse) {
    this.reverse = reverse;
  }
}
