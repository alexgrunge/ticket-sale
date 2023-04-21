package justcloud.tickets.dto;

import java.util.Date;

public class TripStopData {

  private Boolean visited;
  private String id;
  private String runName;
  private String busNumber;
  private StopOffData beginning;
  private StopOffData ending;
  private StopOffData stop;
  private Date expectedStop;
  private Date departureDate;
  private String driver1;
  private TripData trip;

  /** @return the visited */
  public Boolean getVisited() {
    return visited;
  }

  /** @param visited the visited to set */
  public void setVisited(Boolean visited) {
    this.visited = visited;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the runName */
  public String getRunName() {
    return runName;
  }

  /** @param runName the runName to set */
  public void setRunName(String runName) {
    this.runName = runName;
  }

  /** @return the busNumber */
  public String getBusNumber() {
    return busNumber;
  }

  /** @param busNumber the busNumber to set */
  public void setBusNumber(String busNumber) {
    this.busNumber = busNumber;
  }

  /** @return the beginning */
  public StopOffData getBeginning() {
    return beginning;
  }

  /** @param beginning the beginning to set */
  public void setBeginning(StopOffData beginning) {
    this.beginning = beginning;
  }

  /** @return the ending */
  public StopOffData getEnding() {
    return ending;
  }

  /** @param ending the ending to set */
  public void setEnding(StopOffData ending) {
    this.ending = ending;
  }

  /** @return the stop */
  public StopOffData getStop() {
    return stop;
  }

  /** @param stop the stop to set */
  public void setStop(StopOffData stop) {
    this.stop = stop;
  }

  /** @return the expectedStop */
  public Date getExpectedStop() {
    return expectedStop;
  }

  /** @param expectedStop the expectedStop to set */
  public void setExpectedStop(Date expectedStop) {
    this.expectedStop = expectedStop;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the driver1 */
  public String getDriver1() {
    return driver1;
  }

  /** @param driver1 the driver1 to set */
  public void setDriver1(String driver1) {
    this.driver1 = driver1;
  }

  /** @return the trip */
  public TripData getTrip() {
    return trip;
  }

  /** @param trip the trip to set */
  public void setTrip(TripData trip) {
    this.trip = trip;
  }
}
