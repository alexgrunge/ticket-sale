package justcloud.tickets.domain.tickets;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.Employee;

@Entity
public class Bus extends BaseModel {

  private String busNumber;
  private String plates;
  private String gps;

  @OneToMany
  @JoinTable(
      name = "bus_bus_position",
      joinColumns = @JoinColumn(name = "bus_seats_id"),
      inverseJoinColumns = @JoinColumn(name = "bus_position_id"))
  private List<BusPosition> seats;

  @OneToMany
  @JoinTable(
      name = "bus_bus_position",
      joinColumns = @JoinColumn(name = "bus_positions_id"),
      inverseJoinColumns = @JoinColumn(name = "bus_position_id"))
  private List<BusPosition> positions;

  private Boolean wifi;
  private Boolean cafeteria;
  private Boolean bathroom;
  private Boolean headphones;

  @ManyToOne private Employee driver1;

  @ManyToOne private Employee driver2;

  @ManyToOne private ServiceType serviceType;

  @ManyToOne private ServiceLevelType serviceLevelType;

  @Enumerated(EnumType.STRING)
  private BusStatus status;

  private String brand;
  private String model;
  private Integer year;

  public ServiceLevelType getServiceLevelType() {
    return serviceLevelType;
  }

  public void setServiceLevelType(ServiceLevelType serviceLevelType) {
    this.serviceLevelType = serviceLevelType;
  }

  public String getGps() {
    return gps;
  }

  public void setGps(String gps) {
    this.gps = gps;
  }

  /** @return the busNumber */
  public String getBusNumber() {
    return busNumber;
  }

  /** @param busNumber the busNumber to set */
  public void setBusNumber(String busNumber) {
    this.busNumber = busNumber;
  }

  /** @return the plates */
  public String getPlates() {
    return plates;
  }

  /** @param plates the plates to set */
  public void setPlates(String plates) {
    this.plates = plates;
  }

  /** @return the seats */
  public List<BusPosition> getSeats() {
    return seats;
  }

  /** @param seats the seats to set */
  public void setSeats(List<BusPosition> seats) {
    this.seats = seats;
  }

  /** @return the positions */
  public List<BusPosition> getPositions() {
    return positions;
  }

  /** @param positions the positions to set */
  public void setPositions(List<BusPosition> positions) {
    this.positions = positions;
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

  /** @return the serviceType */
  public ServiceType getServiceType() {
    return serviceType;
  }

  /** @param serviceType the serviceType to set */
  public void setServiceType(ServiceType serviceType) {
    this.serviceType = serviceType;
  }

  /** @return the status */
  public BusStatus getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(BusStatus status) {
    this.status = status;
  }

  /** @return the brand */
  public String getBrand() {
    return brand;
  }

  /** @param brand the brand to set */
  public void setBrand(String brand) {
    this.brand = brand;
  }

  /** @return the model */
  public String getModel() {
    return model;
  }

  /** @param model the model to set */
  public void setModel(String model) {
    this.model = model;
  }

  /** @return the year */
  public Integer getYear() {
    return year;
  }

  /** @param year the year to set */
  public void setYear(Integer year) {
    this.year = year;
  }

  public Boolean getWifi() {
    return wifi;
  }

  public void setWifi(Boolean wifi) {
    this.wifi = wifi;
  }

  public Boolean getCafeteria() {
    return cafeteria;
  }

  public void setCafeteria(Boolean cafeteria) {
    this.cafeteria = cafeteria;
  }

  public Boolean getBathroom() {
    return bathroom;
  }

  public void setBathroom(Boolean bathroom) {
    this.bathroom = bathroom;
  }

  public Boolean getHeadphones() {
    return headphones;
  }

  public void setHeadphones(Boolean headphones) {
    this.headphones = headphones;
  }
}
