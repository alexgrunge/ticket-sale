package justcloud.tickets.domain.tickets;

import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import justcloud.tickets.domain.BaseModel;
import lombok.ToString;

@Entity
@ToString
public class ServiceTypeTime extends BaseModel {

  @ElementCollection(targetClass = DayOfWeek.class)
  @CollectionTable(name = "service_type_time_days")
  @Column(name = "day_of_week")
  @Enumerated(EnumType.STRING)
  private List<DayOfWeek> days;

  @ManyToOne private ServiceLevelType serviceLevelType;

  @ManyToOne private ServiceType serviceType;

  @Temporal(TemporalType.TIME)
  private Date departureTime;

  private String departureTimeString;

  private Boolean reverse;

  /** @return the days */
  public List<DayOfWeek> getDays() {
    return days;
  }

  /** @param days the days to set */
  public void setDays(List<DayOfWeek> days) {
    this.days = days;
  }

  public void setDepartureTimeString(String departureTimeString) {
    this.departureTimeString = departureTimeString;
  }

  public String getDepartureTimeString() {
    return departureTimeString;
  }

  /** @return the serviceLevelType */
  public ServiceLevelType getServiceLevelType() {
    return serviceLevelType;
  }

  /** @param serviceLevelType the serviceLevelType to set */
  public void setServiceLevelType(ServiceLevelType serviceLevelType) {
    this.serviceLevelType = serviceLevelType;
  }

  /** @return the serviceType */
  public ServiceType getServiceType() {
    return serviceType;
  }

  /** @param serviceType the serviceType to set */
  public void setServiceType(ServiceType serviceType) {
    this.serviceType = serviceType;
  }

  /** @return the departureTime */
  public Date getDepartureTime() {
    return departureTime;
  }

  /** @param departureTime the departureTime to set */
  public void setDepartureTime(Date departureTime) {
    this.departureTime = departureTime;
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
