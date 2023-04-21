package justcloud.tickets.domain.tickets;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.BaseModel;

@Entity
public class TripStopControl extends BaseModel {

  @ManyToOne private Trip trip;

  @ManyToOne private StopOff stopOff;

  private Boolean visited = false;

  private Date visitedTime;

  /** @return the trip */
  public Trip getTrip() {
    return trip;
  }

  /** @param trip the trip to set */
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  /** @return the stopOff */
  public StopOff getStopOff() {
    return stopOff;
  }

  /** @param stopOff the stopOff to set */
  public void setStopOff(StopOff stopOff) {
    this.stopOff = stopOff;
  }

  /** @return the visited */
  public Boolean getVisited() {
    return visited;
  }

  /** @param visited the visited to set */
  public void setVisited(Boolean visited) {
    this.visited = visited;
  }

  /** @return the visitedTime */
  public Date getVisitedTime() {
    return visitedTime;
  }

  /** @param visitedTime the visitedTime to set */
  public void setVisitedTime(Date visitedTime) {
    this.visitedTime = visitedTime;
  }
}
