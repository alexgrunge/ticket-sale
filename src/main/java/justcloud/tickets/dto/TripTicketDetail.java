package justcloud.tickets.dto;

public class TripTicketDetail {

  private String id;
  private int occupation;
  private int onboarding;
  private int downboarding;
  private String stopName;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the occupation */
  public int getOccupation() {
    return occupation;
  }

  /** @param occupation the occupation to set */
  public void setOccupation(int occupation) {
    this.occupation = occupation;
  }

  /** @return the onboarding */
  public int getOnboarding() {
    return onboarding;
  }

  /** @param onboarding the onboarding to set */
  public void setOnboarding(int onboarding) {
    this.onboarding = onboarding;
  }

  /** @return the downboarding */
  public int getDownboarding() {
    return downboarding;
  }

  /** @param downboarding the downboarding to set */
  public void setDownboarding(int downboarding) {
    this.downboarding = downboarding;
  }

  /** @return the stopName */
  public String getStopName() {
    return stopName;
  }

  /** @param stopName the stopName to set */
  public void setStopName(String stopName) {
    this.stopName = stopName;
  }
}
