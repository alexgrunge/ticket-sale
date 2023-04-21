package justcloud.tickets.dto;

import justcloud.tickets.domain.tickets.PositionType;
import justcloud.tickets.domain.tickets.SeatStatus;

public class BusPositionData {

  private boolean partiallyReserved = false;
  private boolean reserved = false;
  private SeatStatus status;
  private PositionType type;
  private String name;
  private Integer width;
  private Integer height;

  public SeatStatus getStatus() {
    return status;
  }

  public void setStatus(SeatStatus status) {
    this.status = status;
  }

  /** @return the partiallyReserved */
  public boolean isPartiallyReserved() {
    return partiallyReserved;
  }

  /** @param partiallyReserved the partiallyReserved to set */
  public void setPartiallyReserved(boolean partiallyReserved) {
    this.partiallyReserved = partiallyReserved;
  }

  /** @return the reserved */
  public boolean isReserved() {
    return reserved;
  }

  /** @param reserved the reserved to set */
  public void setReserved(boolean reserved) {
    this.reserved = reserved;
  }

  /** @return the type */
  public PositionType getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(PositionType type) {
    this.type = type;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the width */
  public Integer getWidth() {
    return width;
  }

  /** @param width the width to set */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /** @return the height */
  public Integer getHeight() {
    return height;
  }

  /** @param height the height to set */
  public void setHeight(Integer height) {
    this.height = height;
  }
}
