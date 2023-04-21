package justcloud.tickets.dto;

public class TripLogisticData extends TripBusData {

  private int totalSeats;
  private int totalPassengers;
  private int largestOccupation;
  private int shortestOccupation;
  private Double average;

  /** @return the totalSeats */
  public int getTotalSeats() {
    return totalSeats;
  }

  /** @param totalSeats the totalSeats to set */
  public void setTotalSeats(int totalSeats) {
    this.totalSeats = totalSeats;
  }

  /** @return the totalPassengers */
  public int getTotalPassengers() {
    return totalPassengers;
  }

  /** @param totalPassengers the totalPassengers to set */
  public void setTotalPassengers(int totalPassengers) {
    this.totalPassengers = totalPassengers;
  }

  /** @return the largestOccupation */
  public int getLargestOccupation() {
    return largestOccupation;
  }

  /** @param largestOccupation the largestOccupation to set */
  public void setLargestOccupation(int largestOccupation) {
    this.largestOccupation = largestOccupation;
  }

  /** @return the shortestOccupation */
  public int getShortestOccupation() {
    return shortestOccupation;
  }

  /** @param shortestOccupation the shortestOccupation to set */
  public void setShortestOccupation(int shortestOccupation) {
    this.shortestOccupation = shortestOccupation;
  }

  /** @return the average */
  public Double getAverage() {
    return average;
  }

  /** @param average the average to set */
  public void setAverage(Double average) {
    this.average = average;
  }
}
