package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.search.RouteSegment.StopData;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TripData {

  private String id;

  private String driver1Name;

  private String driver2Name;

  private Long olderAdultCount;

  private Long studentCount;

  private Long reservedCount;

  private Long maxOlderAdults;

  private Long maxStudents;

  private Long maxReserved;

  private String busName;

  private Date departureDate;

  private RouteData routeData;

  private Boolean reverse;

  private List<TicketData> tickets;

  private List<RouteSegment.StopData> stops;

  private BusPositionData[][][] positions;

  private BigDecimal driver1Amount;
  private BigDecimal driver2Amount;

  private BigDecimal driver1Expenses;
  private BigDecimal driver2Expenses;

  private BigDecimal driver1ExpensesAdvance;
  private BigDecimal driver2ExpensesAdvance;

  private BigDecimal driver1Earnings;
  private BigDecimal driver2Earnings;

  private BigDecimal driver1Nominal;
  private BigDecimal driver2Nominal;

  private BigDecimal driver1Insurance;
  private BigDecimal driver2Insurance;

  private String driver1NominalWeek;
  private String driver2NominalWeek;

  private Boolean wifi;
  private Boolean cafeteria;
  private Boolean bathroom;
  private Boolean headphones;

  public BigDecimal getDriver1Amount() {
    return driver1Amount;
  }

  public void setDriver1Amount(BigDecimal driver1Amount) {
    this.driver1Amount = driver1Amount;
  }

  public BigDecimal getDriver2Amount() {
    return driver2Amount;
  }

  public void setDriver2Amount(BigDecimal driver2Amount) {
    this.driver2Amount = driver2Amount;
  }

  public BigDecimal getDriver1Expenses() {
    return driver1Expenses;
  }

  public void setDriver1Expenses(BigDecimal driver1Expenses) {
    this.driver1Expenses = driver1Expenses;
  }

  public BigDecimal getDriver2Expenses() {
    return driver2Expenses;
  }

  public void setDriver2Expenses(BigDecimal driver2Expenses) {
    this.driver2Expenses = driver2Expenses;
  }

  public BigDecimal getDriver1ExpensesAdvance() {
    return driver1ExpensesAdvance;
  }

  public void setDriver1ExpensesAdvance(BigDecimal driver1ExpensesAdvance) {
    this.driver1ExpensesAdvance = driver1ExpensesAdvance;
  }

  public BigDecimal getDriver2ExpensesAdvance() {
    return driver2ExpensesAdvance;
  }

  public void setDriver2ExpensesAdvance(BigDecimal driver2ExpensesAdvance) {
    this.driver2ExpensesAdvance = driver2ExpensesAdvance;
  }

  public BigDecimal getDriver1Earnings() {
    return driver1Earnings;
  }

  public void setDriver1Earnings(BigDecimal driver1Earnings) {
    this.driver1Earnings = driver1Earnings;
  }

  public BigDecimal getDriver2Earnings() {
    return driver2Earnings;
  }

  public void setDriver2Earnings(BigDecimal driver2Earnings) {
    this.driver2Earnings = driver2Earnings;
  }

  public BigDecimal getDriver1Nominal() {
    return driver1Nominal;
  }

  public void setDriver1Nominal(BigDecimal driver1Nominal) {
    this.driver1Nominal = driver1Nominal;
  }

  public BigDecimal getDriver2Nominal() {
    return driver2Nominal;
  }

  public void setDriver2Nominal(BigDecimal driver2Nominal) {
    this.driver2Nominal = driver2Nominal;
  }

  public BigDecimal getDriver1Insurance() {
    return driver1Insurance;
  }

  public void setDriver1Insurance(BigDecimal driver1Insurance) {
    this.driver1Insurance = driver1Insurance;
  }

  public BigDecimal getDriver2Insurance() {
    return driver2Insurance;
  }

  public void setDriver2Insurance(BigDecimal driver2Insurance) {
    this.driver2Insurance = driver2Insurance;
  }

  public String getDriver1NominalWeek() {
    return driver1NominalWeek;
  }

  public void setDriver1NominalWeek(String driver1NominalWeek) {
    this.driver1NominalWeek = driver1NominalWeek;
  }

  public String getDriver2NominalWeek() {
    return driver2NominalWeek;
  }

  public void setDriver2NominalWeek(String driver2NominalWeek) {
    this.driver2NominalWeek = driver2NominalWeek;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the routeData */
  public RouteData getRouteData() {
    return routeData;
  }

  /** @param routeData the routeData to set */
  public void setRouteData(RouteData routeData) {
    this.routeData = routeData;
  }

  /** @return the reverse */
  public Boolean getReverse() {
    return reverse;
  }

  /** @param reverse the reverse to set */
  public void setReverse(Boolean reverse) {
    this.reverse = reverse;
  }

  /** @return the tickets */
  public List<TicketData> getTickets() {
    return tickets;
  }

  /** @param tickets the tickets to set */
  public void setTickets(List<TicketData> tickets) {
    this.tickets = tickets;
  }

  /** @return the stops */
  public List<StopData> getStops() {
    return stops;
  }

  /** @param stops the stops to set */
  public void setStops(List<StopData> stops) {
    this.stops = stops;
  }

  /** @return the positions */
  public BusPositionData[][][] getPositions() {
    return positions;
  }

  /** @param positions the positions to set */
  public void setPositions(BusPositionData[][][] positions) {
    this.positions = positions;
  }

  public Long getOlderAdultCount() {
    return olderAdultCount;
  }

  public void setOlderAdultCount(Long olderAdultCount) {
    this.olderAdultCount = olderAdultCount;
  }

  public Long getStudentCount() {
    return studentCount;
  }

  public void setStudentCount(Long studentCount) {
    this.studentCount = studentCount;
  }

  public String getBusName() {
    return busName;
  }

  public void setBusName(String busName) {
    this.busName = busName;
  }

  public String getDriver1Name() {
    return driver1Name;
  }

  public void setDriver1Name(String driver1Name) {
    this.driver1Name = driver1Name;
  }

  public Long getReservedCount() {
    return reservedCount;
  }

  public void setReservedCount(Long reservedCount) {
    this.reservedCount = reservedCount;
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

  public Long getMaxReserved() {
    return maxReserved;
  }

  public void setMaxReserved(Long maxReserved) {
    this.maxReserved = maxReserved;
  }

  public String getDriver2Name() {
    return driver2Name;
  }

  public void setDriver2Name(String driver2Name) {
    this.driver2Name = driver2Name;
  }

  public Date getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  public Boolean getWifi() {
    return wifi;
  }

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

  public Boolean getHeadphones() {
    return headphones;
  }

  public void setHeadphones(Boolean headphones) {
    this.headphones = headphones;
  }
}
