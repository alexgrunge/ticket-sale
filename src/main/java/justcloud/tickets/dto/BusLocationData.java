package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class BusLocationData {

  private TripData trip;
  private BusData bus;
  private String location;
  private String heading;
  private String event;
  private String zones;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private Date serverTime;
  private Date gpsTime;
  private String originName;
  private String destinationName;

  private String routeName;
  private String serviceLevelTypeName;

  private Long delayedTime;
  private Boolean currentlyDelayed;
  private boolean offRoute;
}
