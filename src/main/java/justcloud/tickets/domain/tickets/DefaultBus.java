package justcloud.tickets.domain.tickets;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import justcloud.tickets.domain.BaseModel;

@Entity
public class DefaultBus extends BaseModel {

  @OneToMany
  @JoinTable(
      name = "default_bus_bus_position",
      joinColumns = @JoinColumn(name = "default_bus_seats_id"),
      inverseJoinColumns = @JoinColumn(name = "bus_position_id"))
  private List<BusPosition> seats;

  @OneToMany
  @JoinTable(
      name = "default_bus_bus_position",
      joinColumns = @JoinColumn(name = "default_bus_positions_id"),
      inverseJoinColumns = @JoinColumn(name = "bus_position_id"))
  private List<BusPosition> positions;

  @ManyToOne private ServiceLevelType serviceLevel;

  private Boolean wifi;
  private Boolean cafeteria;
  private Boolean bathroom;
  private Boolean headphones;

  private Integer trunkSize;

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

  /** @return the serviceLevel */
  public ServiceLevelType getServiceLevel() {
    return serviceLevel;
  }

  /** @param serviceLevel the serviceLevel to set */
  public void setServiceLevel(ServiceLevelType serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  /** @return the wifi */
  public Boolean getWifi() {
    return wifi;
  }

  /** @param wifi the wifi to set */
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

  /** @return the headphones */
  public Boolean getHeadphones() {
    return headphones;
  }

  /** @param headphones the headphones to set */
  public void setHeadphones(Boolean headphones) {
    this.headphones = headphones;
  }

  /** @return the trunkSize */
  public Integer getTrunkSize() {
    return trunkSize;
  }

  /** @param trunkSize the trunkSize to set */
  public void setTrunkSize(Integer trunkSize) {
    this.trunkSize = trunkSize;
  }
}
