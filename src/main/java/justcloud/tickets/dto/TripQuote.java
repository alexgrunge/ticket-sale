package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;
import justcloud.tickets.search.RouteSegment;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TripQuote {

  private RouteSegment.StopData origin;

  private RouteSegment.StopData destination;

  private RouteSegment.SegmentData extra;

  private BusData bus;

  private Date routeDepartingDate;

  private Date departingDate;

  private Date arrivalDate;

  private Long travelMinutes;

  private BigDecimal basePrice;

  private BigDecimal adultPrice;

  private BigDecimal studentPrice;

  private BigDecimal olderAdultPrice;

  private BigDecimal childPrice;

  private BigDecimal infantPrice;

  private BigDecimal totalPrice;

  private RouteData route;

  private String serviceLevel;

  private String serviceLevelId;

  private Boolean reverse;

  private Long reservedTickets;

  public Long getReservedTickets() {
    return reservedTickets;
  }

  public void setReservedTickets(Long reservedTickets) {
    this.reservedTickets = reservedTickets;
  }

  public String getServiceLevelId() {
    return serviceLevelId;
  }

  public void setServiceLevelId(String serviceLevelId) {
    this.serviceLevelId = serviceLevelId;
  }

  /** @return the origin */
  public RouteSegment.StopData getOrigin() {
    return origin;
  }

  /** @param origin the origin to set */
  public void setOrigin(RouteSegment.StopData origin) {
    this.origin = origin;
  }

  /** @return the destination */
  public RouteSegment.StopData getDestination() {
    return destination;
  }

  /** @param destination the destination to set */
  public void setDestination(RouteSegment.StopData destination) {
    this.destination = destination;
  }

  /** @return the extra */
  public RouteSegment.SegmentData getExtra() {
    return extra;
  }

  /** @param extra the extra to set */
  public void setExtra(RouteSegment.SegmentData extra) {
    this.extra = extra;
  }

  /** @return the routeDepartingDate */
  public Date getRouteDepartingDate() {
    return routeDepartingDate;
  }

  /** @param routeDepartingDate the routeDepartingDate to set */
  public void setRouteDepartingDate(Date routeDepartingDate) {
    this.routeDepartingDate = routeDepartingDate;
  }

  /** @return the departingDate */
  public Date getDepartingDate() {
    return departingDate;
  }

  /** @param departingDate the departingDate to set */
  public void setDepartingDate(Date departingDate) {
    this.departingDate = departingDate;
  }

  /** @return the arrivalDate */
  public Date getArrivalDate() {
    return arrivalDate;
  }

  /** @param arrivalDate the arrivalDate to set */
  public void setArrivalDate(Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  /** @return the travelMinutes */
  public Long getTravelMinutes() {
    return travelMinutes;
  }

  /** @param travelMinutes the travelMinutes to set */
  public void setTravelMinutes(Long travelMinutes) {
    this.travelMinutes = travelMinutes;
  }

  /** @return the basePrice */
  public BigDecimal getBasePrice() {
    return basePrice;
  }

  /** @param basePrice the basePrice to set */
  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  /** @return the studentPrice */
  public BigDecimal getStudentPrice() {
    return studentPrice;
  }

  /** @param studentPrice the studentPrice to set */
  public void setStudentPrice(BigDecimal studentPrice) {
    this.studentPrice = studentPrice;
  }

  /** @return the adultPrice */
  public BigDecimal getAdultPrice() {
    return adultPrice;
  }

  /** @param adultPrice the adultPrice to set */
  public void setAdultPrice(BigDecimal adultPrice) {
    this.adultPrice = adultPrice;
  }

  /** @return the olderAdultPrice */
  public BigDecimal getOlderAdultPrice() {
    return olderAdultPrice;
  }

  /** @param olderAdultPrice the olderAdultPrice to set */
  public void setOlderAdultPrice(BigDecimal olderAdultPrice) {
    this.olderAdultPrice = olderAdultPrice;
  }

  /** @return the childPrice */
  public BigDecimal getChildPrice() {
    return childPrice;
  }

  /** @param childPrice the childPrice to set */
  public void setChildPrice(BigDecimal childPrice) {
    this.childPrice = childPrice;
  }

  /** @return the infantPrice */
  public BigDecimal getInfantPrice() {
    return infantPrice;
  }

  /** @param infantPrice the infantPrice to set */
  public void setInfantPrice(BigDecimal infantPrice) {
    this.infantPrice = infantPrice;
  }

  /** @return the totalPrice */
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  /** @param totalPrice the totalPrice to set */
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  /** @return the route */
  public RouteData getRoute() {
    return route;
  }

  /** @param route the route to set */
  public void setRoute(RouteData route) {
    this.route = route;
  }

  /** @return the serviceLevel */
  public String getServiceLevel() {
    return serviceLevel;
  }

  /** @param serviceLevel the serviceLevel to set */
  public void setServiceLevel(String serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  /** @return the reverse */
  public Boolean getReverse() {
    return reverse;
  }

  /** @param reverse the reverse to set */
  public void setReverse(Boolean reverse) {
    this.reverse = reverse;
  }

  public BusData getBus() {
    return bus;
  }

  public void setBus(BusData bus) {
    this.bus = bus;
  }
}
