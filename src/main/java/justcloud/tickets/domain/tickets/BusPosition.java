package justcloud.tickets.domain.tickets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import justcloud.tickets.domain.BaseModel;

@Entity
public class BusPosition extends BaseModel {

  @Enumerated(EnumType.STRING)
  private PositionType type;

  private String name;
  private Integer width;
  private Integer height;

  @Column(name = "row_idx")
  private Integer row;

  @Column(name = "column_idx")
  private Integer column;

  private Integer floor;

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

  /** @return the row */
  public Integer getRow() {
    return row;
  }

  /** @param row the row to set */
  public void setRow(Integer row) {
    this.row = row;
  }

  /** @return the column */
  public Integer getColumn() {
    return column;
  }

  /** @param column the column to set */
  public void setColumn(Integer column) {
    this.column = column;
  }

  /** @return the floor */
  public Integer getFloor() {
    return floor;
  }

  /** @param floor the floor to set */
  public void setFloor(Integer floor) {
    this.floor = floor;
  }
}
