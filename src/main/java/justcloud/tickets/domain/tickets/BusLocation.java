package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;

@Entity
public class BusLocation extends BaseModel {

  @NotNull @ManyToOne private Bus bus;
  private String location;
  private String heading;
  private String event;
  private String zones;

  @NotNull private BigDecimal latitude;

  @NotNull private BigDecimal longitude;

  @Temporal(TemporalType.TIMESTAMP)
  private Date serverTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date gpsTime;

  public Bus getBus() {
    return bus;
  }

  public void setBus(Bus bus) {
    this.bus = bus;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getHeading() {
    return heading;
  }

  public void setHeading(String heading) {
    this.heading = heading;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getZones() {
    return zones;
  }

  public void setZones(String zones) {
    this.zones = zones;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public Date getServerTime() {
    return serverTime;
  }

  public void setServerTime(Date serverTime) {
    this.serverTime = serverTime;
  }

  public Date getGpsTime() {
    return gpsTime;
  }

  public void setGpsTime(Date gpsTime) {
    this.gpsTime = gpsTime;
  }
}
