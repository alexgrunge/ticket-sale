package justcloud.tickets.dto;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FullGroupData {

  private String id;

  private List<TripAdvanceData> advances;
  private List<TripExpense> expenses;
  private List<TripStopData> stopControlData;
  private List<BaggageData> baggageData;
  private List<SimpleTripData> trips;

  private EmployeeWeekDiscount driver1NominalDiscount;
  private EmployeeWeekDiscount driver2NominalDiscount;

  private EmployeeWeekDiscount driver1InsuranceDiscount;
  private EmployeeWeekDiscount driver2InsuranceDiscount;

  private String packages;
  private BigDecimal advance;
  private BigDecimal dieselCost;
  private String routeName;
  private Date departureDate;
  private Date estimatedArrivalDate;
  private Boolean payed;
  private Boolean hasAllStamps;
  private Boolean hasAllPlaces;

  private TripData tripData;

  private EmployeeData driver1;
  private EmployeeData driver2;
  private String busName;

  public EmployeeWeekDiscount getDriver1NominalDiscount() {
    return driver1NominalDiscount;
  }

  public void setDriver1NominalDiscount(EmployeeWeekDiscount driver1NominalDiscount) {
    this.driver1NominalDiscount = driver1NominalDiscount;
  }

  public EmployeeWeekDiscount getDriver2NominalDiscount() {
    return driver2NominalDiscount;
  }

  public void setDriver2NominalDiscount(EmployeeWeekDiscount driver2NominalDiscount) {
    this.driver2NominalDiscount = driver2NominalDiscount;
  }

  public EmployeeWeekDiscount getDriver1InsuranceDiscount() {
    return driver1InsuranceDiscount;
  }

  public void setDriver1InsuranceDiscount(EmployeeWeekDiscount driver1InsuranceDiscount) {
    this.driver1InsuranceDiscount = driver1InsuranceDiscount;
  }

  public EmployeeWeekDiscount getDriver2InsuranceDiscount() {
    return driver2InsuranceDiscount;
  }

  public void setDriver2InsuranceDiscount(EmployeeWeekDiscount driver2InsuranceDiscount) {
    this.driver2InsuranceDiscount = driver2InsuranceDiscount;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the advances */
  public List<TripAdvanceData> getAdvances() {
    return advances;
  }

  /** @param advances the advances to set */
  public void setAdvances(List<TripAdvanceData> advances) {
    this.advances = advances;
  }

  /** @return the expenses */
  public List<TripExpense> getExpenses() {
    return expenses;
  }

  /** @param expenses the expenses to set */
  public void setExpenses(List<TripExpense> expenses) {
    this.expenses = expenses;
  }

  /** @return the stopControlData */
  public List<TripStopData> getStopControlData() {
    return stopControlData;
  }

  /** @param stopControlData the stopControlData to set */
  public void setStopControlData(List<TripStopData> stopControlData) {
    this.stopControlData = stopControlData;
  }

  /** @return the baggageData */
  public List<BaggageData> getBaggageData() {
    return baggageData;
  }

  /** @param baggageData the baggageData to set */
  public void setBaggageData(List<BaggageData> baggageData) {
    this.baggageData = baggageData;
  }

  /** @return the packages */
  public String getPackages() {
    return packages;
  }

  /** @param packages the packages to set */
  public void setPackages(String packages) {
    this.packages = packages;
  }

  /** @return the routeName */
  public String getRouteName() {
    return routeName;
  }

  /** @param routeName the routeName to set */
  public void setRouteName(String routeName) {
    this.routeName = routeName;
  }

  /** @return the departureDate */
  public Date getDepartureDate() {
    return departureDate;
  }

  /** @param departureDate the departureDate to set */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /** @return the estimatedArrivalDate */
  public Date getEstimatedArrivalDate() {
    return estimatedArrivalDate;
  }

  /** @param estimatedArrivalDate the estimatedArrivalDate to set */
  public void setEstimatedArrivalDate(Date estimatedArrivalDate) {
    this.estimatedArrivalDate = estimatedArrivalDate;
  }

  /** @return the payed */
  public Boolean getPayed() {
    return payed;
  }

  /** @param payed the payed to set */
  public void setPayed(Boolean payed) {
    this.payed = payed;
  }

  /** @return the hasAllStamps */
  public Boolean getHasAllStamps() {
    return hasAllStamps;
  }

  /** @param hasAllStamps the hasAllStamps to set */
  public void setHasAllStamps(Boolean hasAllStamps) {
    this.hasAllStamps = hasAllStamps;
  }

  /** @return the hasAllPlaces */
  public Boolean getHasAllPlaces() {
    return hasAllPlaces;
  }

  /** @param hasAllPlaces the hasAllPlaces to set */
  public void setHasAllPlaces(Boolean hasAllPlaces) {
    this.hasAllPlaces = hasAllPlaces;
  }

  /** @return the tripData */
  public TripData getTripData() {
    return tripData;
  }

  /** @param tripData the tripData to set */
  public void setTripData(TripData tripData) {
    this.tripData = tripData;
  }

  /** @return the driver1 */
  public EmployeeData getDriver1() {
    return driver1;
  }

  /** @param driver1 the driver1 to set */
  public void setDriver1(EmployeeData driver1) {
    this.driver1 = driver1;
  }

  /** @return the driver2 */
  public EmployeeData getDriver2() {
    return driver2;
  }

  /** @param driver2 the driver2 to set */
  public void setDriver2(EmployeeData driver2) {
    this.driver2 = driver2;
  }

  /** @return the busName */
  public String getBusName() {
    return busName;
  }

  /** @param busName the busName to set */
  public void setBusName(String busName) {
    this.busName = busName;
  }

  public BigDecimal getAdvance() {
    return advance;
  }

  public void setAdvance(BigDecimal advance) {
    this.advance = advance;
  }

  public BigDecimal getDieselCost() {
    return dieselCost;
  }

  public void setDieselCost(BigDecimal dieselCost) {
    this.dieselCost = dieselCost;
  }

  public List<SimpleTripData> getTrips() {
    return trips;
  }

  public void setTrips(List<SimpleTripData> trips) {
    this.trips = trips;
  }
}
