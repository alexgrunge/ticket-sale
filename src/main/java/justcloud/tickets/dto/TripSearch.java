package justcloud.tickets.dto;

import java.util.Date;
import lombok.ToString;

@ToString
public class TripSearch {
  private String origin;

  private String destination;

  private String timeZone;

  private Date departureDate;

  private Date returningDate;

  private Boolean roundTrip;

  private Integer adultCount;

  private Integer studentCount;

  private Integer olderAdultCount;

  private Integer infantCount;

  private Integer childrenCount;

  /** @return the origin */
  public String getOrigin() {
    return origin;
  }

  /** @param origin the origin to set */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /** @return the destination */
  public String getDestination() {
    return destination;
  }

  /** @param destination the destination to set */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /** @return the timeZone */
  public String getTimeZone() {
    return timeZone;
  }

  /** @param timeZone the timeZone to set */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the returningDate */
  public Date getReturningDate() {
    return returningDate;
  }

  /** @param returningDate the returningDate to set */
  public void setReturningDate(Date returningDate) {
    this.returningDate = returningDate;
  }

  /** @return the roundTrip */
  public Boolean getRoundTrip() {
    return roundTrip;
  }

  /** @param roundTrip the roundTrip to set */
  public void setRoundTrip(Boolean roundTrip) {
    this.roundTrip = roundTrip;
  }

  /** @return the adultCount */
  public Integer getAdultCount() {
    return adultCount;
  }

  /** @param adultCount the adultCount to set */
  public void setAdultCount(Integer adultCount) {
    this.adultCount = adultCount;
  }

  /** @return the olderAdultCount */
  public Integer getOlderAdultCount() {
    return olderAdultCount;
  }

  /** @param olderAdultCount the olderAdultCount to set */
  public void setOlderAdultCount(Integer olderAdultCount) {
    this.olderAdultCount = olderAdultCount;
  }

  /** @return the infantCount */
  public Integer getInfantCount() {
    return infantCount;
  }

  /** @param infantCount the infantCount to set */
  public void setInfantCount(Integer infantCount) {
    this.infantCount = infantCount;
  }

  /** @return the childrenCount */
  public Integer getChildrenCount() {
    return childrenCount;
  }

  /** @param childrenCount the childrenCount to set */
  public void setChildrenCount(Integer childrenCount) {
    this.childrenCount = childrenCount;
  }

  public Integer getStudentCount() {
    return studentCount;
  }

  public void setStudentCount(Integer studentCount) {
    this.studentCount = studentCount;
  }
}
