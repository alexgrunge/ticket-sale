package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import justcloud.tickets.domain.BaseModel;

@Entity
public class SegmentVar extends BaseModel {

  @ManyToOne private Route route;

  @ManyToOne private ServiceLevelType serviceLevelType;

  @ManyToOne private StopOff startingStop;

  @ManyToOne private StopOff endingStop;

  private String stringVar;

  private BigDecimal numericVar;

  private String varType;

  private String category;

  /** @return the route */
  public Route getRoute() {
    return route;
  }

  /** @param route the route to set */
  public void setRoute(Route route) {
    this.route = route;
  }

  /** @return the serviceLevelType */
  public ServiceLevelType getServiceLevelType() {
    return serviceLevelType;
  }

  /** @param serviceLevelType the serviceLevelType to set */
  public void setServiceLevelType(ServiceLevelType serviceLevelType) {
    this.serviceLevelType = serviceLevelType;
  }

  /** @return the startingStop */
  public StopOff getStartingStop() {
    return startingStop;
  }

  /** @param startingStop the startingStop to set */
  public void setStartingStop(StopOff startingStop) {
    this.startingStop = startingStop;
  }

  /** @return the endingStop */
  public StopOff getEndingStop() {
    return endingStop;
  }

  /** @param endingStop the endingStop to set */
  public void setEndingStop(StopOff endingStop) {
    this.endingStop = endingStop;
  }

  /** @return the stringVar */
  public String getStringVar() {
    return stringVar;
  }

  /** @param stringVar the stringVar to set */
  public void setStringVar(String stringVar) {
    this.stringVar = stringVar;
  }

  /** @return the numericVar */
  public BigDecimal getNumericVar() {
    return numericVar;
  }

  /** @param numericVar the numericVar to set */
  public void setNumericVar(BigDecimal numericVar) {
    this.numericVar = numericVar;
  }

  /** @return the varType */
  public String getVarType() {
    return varType;
  }

  /** @param varType the varType to set */
  public void setVarType(String varType) {
    this.varType = varType;
  }

  /** @return the category */
  public String getCategory() {
    return category;
  }

  /** @param category the category to set */
  public void setCategory(String category) {
    this.category = category;
  }
}
