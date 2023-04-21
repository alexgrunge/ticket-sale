package justcloud.tickets.search;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Document(indexName = "ticket-sales", shards = 1, replicas = 1)
@Setting(settingPath = "/elasticsearch/settings.json")
@Mapping(mappingPath = "/elasticsearch/mappings/routeSegment.json")
@ToString
public class RouteSegment {

  @Id private String id;

  private String routeId;

  private Boolean reverse;

  private BigDecimal price;

  private StopData startingStop;
  private StopData endingStop;

  private String serviceLevelTypeId;

  private SegmentData extra;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the routeId */
  public String getRouteId() {
    return routeId;
  }

  /** @param routeId the routeId to set */
  public void setRouteId(String routeId) {
    this.routeId = routeId;
  }

  /** @return the reverse */
  public Boolean getReverse() {
    return reverse;
  }

  /** @param reverse the reverse to set */
  public void setReverse(Boolean reverse) {
    this.reverse = reverse;
  }

  /** @return the price */
  public BigDecimal getPrice() {
    return price;
  }

  /** @param price the price to set */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** @return the startingStop */
  public StopData getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(StopData startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public StopData getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(StopData endingStop) {
    this.endingStop = endingStop;
  }

  /** @return the serviceLevelTypeId */
  public String getServiceLevelTypeId() {
    return serviceLevelTypeId;
  }

  /** @param serviceLevelTypeId the serviceLevelTypeId to set */
  public void setServiceLevelTypeId(String serviceLevelTypeId) {
    this.serviceLevelTypeId = serviceLevelTypeId;
  }

  /** @return the extra */
  public SegmentData getExtra() {
    return extra;
  }

  /** @param extra the extra to set */
  public void setExtra(SegmentData extra) {
    this.extra = extra;
  }

  @ToString
  @EqualsAndHashCode
  public static class SegmentData {

    private Long kilometers;
    private Long stops;
    private Long time;
    private Long previousTime;
    private Long shownTime;

    public SegmentData() {}

    public SegmentData(Long time, Long kilometers, Long stops, Long previousTime, Long shownTime) {
      setTime(time);
      setKilometers(kilometers);
      setStops(stops);
      setPreviousTime(previousTime);
      setShownTime(shownTime);
    }

    /** @return the kilometers */
    public Long getKilometers() {
      return kilometers;
    }

    /** @param kilometers the kilometers to set */
    public void setKilometers(Long kilometers) {
      this.kilometers = kilometers;
    }

    /** @return the stops */
    public Long getStops() {
      return stops;
    }

    /** @param stops the stops to set */
    public void setStops(Long stops) {
      this.stops = stops;
    }

    /** @return the time */
    public Long getTime() {
      return time;
    }

    /** @param time the time to set */
    public void setTime(Long time) {
      this.time = time;
    }

    /** @return the previousTime */
    public Long getPreviousTime() {
      return previousTime;
    }

    /** @param previousTime the previousTime to set */
    public void setPreviousTime(Long previousTime) {
      this.previousTime = previousTime;
    }

    /** @return the shownTime */
    public Long getShownTime() {
      return shownTime;
    }

    /** @param shownTime the shownTime to set */
    public void setShownTime(Long shownTime) {
      this.shownTime = shownTime;
    }
  }

  @ToString
  @EqualsAndHashCode
  public static class StopData {

    @Field(index = FieldIndex.not_analyzed)
    private String id;

    private String name;

    private Long shownMinutes;
    private Long travelMinutes;
    private Long waitingMinutes;

    private Long orderIndex;

    @CompletionField(payloads = true)
    private Completion suggestion;

    private Location location;

    /** @return the id */
    public String getId() {
      return id;
    }

    /** @param id the id to set */
    public void setId(String id) {
      this.id = id;
    }

    /** @return the name */
    public String getName() {
      return name;
    }

    /** @param name the name to set */
    public void setName(String name) {
      this.name = name;
    }

    /** @return the shownMinutes */
    public Long getShownMinutes() {
      return shownMinutes;
    }

    /** @param shownMinutes the shownMinutes to set */
    public void setShownMinutes(Long shownMinutes) {
      this.shownMinutes = shownMinutes;
    }

    /** @return the travelMinutes */
    public Long getTravelMinutes() {
      return travelMinutes;
    }

    /** @param travelMinutes the travelMinutes to set */
    public void setTravelMinutes(Long travelMinutes) {
      this.travelMinutes = travelMinutes;
    }

    /** @return the waitingMinutes */
    public Long getWaitingMinutes() {
      return waitingMinutes;
    }

    /** @param waitingMinutes the waitingMinutes to set */
    public void setWaitingMinutes(Long waitingMinutes) {
      this.waitingMinutes = waitingMinutes;
    }

    /** @return the orderIndex */
    public Long getOrderIndex() {
      return orderIndex;
    }

    /** @param orderIndex the orderIndex to set */
    public void setOrderIndex(Long orderIndex) {
      this.orderIndex = orderIndex;
    }

    /** @return the suggestion */
    public Completion getSuggestion() {
      return suggestion;
    }

    /** @param suggestion the suggestion to set */
    public void setSuggestion(Completion suggestion) {
      this.suggestion = suggestion;
    }

    /** @return the location */
    public Location getLocation() {
      return location;
    }

    /** @param location the location to set */
    public void setLocation(Location location) {
      this.location = location;
    }
  }
}
