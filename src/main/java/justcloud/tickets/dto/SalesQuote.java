package justcloud.tickets.dto;

import java.util.List;

public class SalesQuote {

  private Long maxOlderAdults;
  private Long maxStudents;
  private List<TripQuote> departureQuotes;
  private List<TripQuote> returnQuotes;
  private List<PassengerData> passengers;

  /** @return the departureQuotes */
  public List<TripQuote> getDepartureQuotes() {
    return departureQuotes;
  }

  /** @param departureQuotes the departureQuotes to set */
  public void setDepartureQuotes(List<TripQuote> departureQuotes) {
    this.departureQuotes = departureQuotes;
  }

  /** @return the returnQuotes */
  public List<TripQuote> getReturnQuotes() {
    return returnQuotes;
  }

  /** @param returnQuotes the returnQuotes to set */
  public void setReturnQuotes(List<TripQuote> returnQuotes) {
    this.returnQuotes = returnQuotes;
  }

  /** @return the passengers */
  public List<PassengerData> getPassengers() {
    return passengers;
  }

  /** @param passengers the passengers to set */
  public void setPassengers(List<PassengerData> passengers) {
    this.passengers = passengers;
  }

  public Long getMaxOlderAdults() {
    return maxOlderAdults;
  }

  public void setMaxOlderAdults(Long maxOlderAdults) {
    this.maxOlderAdults = maxOlderAdults;
  }

  public Long getMaxStudents() {
    return maxStudents;
  }

  public void setMaxStudents(Long maxStudents) {
    this.maxStudents = maxStudents;
  }
}
