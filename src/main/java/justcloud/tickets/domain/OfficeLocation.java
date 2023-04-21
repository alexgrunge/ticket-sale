package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;

@Entity
public class OfficeLocation extends BaseModel {

  private String shiftPrefix;

  private String name;
  private String address;

  private BigDecimal latitude;
  private BigDecimal longitude;
  private Long currentShift;
  private Long currentPayment;

  public Long getCurrentShift() {
    return currentShift;
  }

  public void setCurrentShift(Long currentShift) {
    this.currentShift = currentShift;
  }

  public String getShiftPrefix() {
    return shiftPrefix;
  }

  public void setShiftPrefix(String shiftPrefix) {
    this.shiftPrefix = shiftPrefix;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the address */
  public String getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(String address) {
    this.address = address;
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

  public Long getCurrentPayment() {
    return currentPayment;
  }

  public void setCurrentPayment(Long currentPayment) {
    this.currentPayment = currentPayment;
  }
}
