package justcloud.tickets.service.tracking;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Coord {

  public BigDecimal latitude;

  public BigDecimal longitude;

  public String knownPosition;

  public Coord() {
    // Nothing special, just to make it internal
  }

  public Coord(BigDecimal latitude, BigDecimal longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return String.format("(%f, %f)", longitude, latitude);
  }
}
