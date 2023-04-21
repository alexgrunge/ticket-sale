package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;

public class LocationData {

  private String plate;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private String heading;
  private String location;
  private String event;
  private String zones;
  private Date serverTime;
  private Date gpsTime;

  /** @return the plate */
  public String getPlate() {
    return plate;
  }

  /** @param plate the plate to set */
  public void setPlate(String plate) {
    this.plate = plate;
  }

  /** @return the latitude */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /** @param latitude the latitude to set */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /** @return the longitude */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /** @param longitude the longitude to set */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /** @return the heading */
  public String getHeading() {
    return heading;
  }

  /** @param heading the heading to set */
  public void setHeading(String heading) {
    this.heading = heading;
  }

  /** @return the location */
  public String getLocation() {
    return location;
  }

  /** @param location the location to set */
  public void setLocation(String location) {
    this.location = location;
  }

  /** @return the event */
  public String getEvent() {
    return event;
  }

  /** @param event the event to set */
  public void setEvent(String event) {
    this.event = event;
  }

  /** @return the zones */
  public String getZones() {
    return zones;
  }

  /** @param zones the zones to set */
  public void setZones(String zones) {
    this.zones = zones;
  }

  /** @return the serverTime */
  public Date getServerTime() {
    return serverTime;
  }

  /** @param serverTime the serverTime to set */
  public void setServerTime(Date serverTime) {
    this.serverTime = serverTime;
  }

  /** @return the gpsTime */
  public Date getGpsTime() {
    return gpsTime;
  }

  /** @param gpsTime the gpsTime to set */
  public void setGpsTime(Date gpsTime) {
    this.gpsTime = gpsTime;
  }
}
