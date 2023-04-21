package justcloud.tickets.dto;

public class SeatData {

  private String startingStop;
  private String endingStop;

  /** @return the startingStop */
  public String getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(String startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public String getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(String endingStop) {
    this.endingStop = endingStop;
  }
}
