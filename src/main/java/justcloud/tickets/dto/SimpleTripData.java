package justcloud.tickets.dto;

import java.math.BigDecimal;

/** Created by iamedu on 10/8/16. */
public class SimpleTripData {

  private BigDecimal dieselLiters;

  private StopOffData startingStop;

  private StopOffData endingStop;

  private String tripId;

  public StopOffData getStartingStop() {
    return startingStop;
  }

  public void setStartingStop(StopOffData startingStop) {
    this.startingStop = startingStop;
  }

  public StopOffData getEndingStop() {
    return endingStop;
  }

  public void setEndingStop(StopOffData endingStop) {
    this.endingStop = endingStop;
  }

  public String getTripId() {
    return tripId;
  }

  public void setTripId(String tripId) {
    this.tripId = tripId;
  }

  public BigDecimal getDieselLiters() {
    return dieselLiters;
  }

  public void setDieselLiters(BigDecimal dieselLiters) {
    this.dieselLiters = dieselLiters;
  }
}
