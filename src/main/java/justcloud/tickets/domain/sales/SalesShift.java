package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;
import justcloud.tickets.domain.User;

/** Created by iamedu on 6/25/16. */
@Entity
@Table(name = "sale_shift")
public class SalesShift extends BaseModel {

  private BigDecimal startingAmount;

  @NotNull @ManyToOne private User user;

  private String shiftNumber;

  @NotNull @ManyToOne private SalesTerminal salesTerminal;

  private Boolean finished = false;

  private Long currentSale = 0l;

  private Date finishDate;

  public Long getCurrentSale() {
    return currentSale;
  }

  public void setCurrentSale(Long currentSale) {
    this.currentSale = currentSale;
  }

  public String getShiftNumber() {
    return shiftNumber;
  }

  public void setShiftNumber(String shiftNumber) {
    this.shiftNumber = shiftNumber;
  }

  public BigDecimal getStartingAmount() {
    return startingAmount;
  }

  public void setStartingAmount(BigDecimal startingAmount) {
    this.startingAmount = startingAmount;
  }

  public Date getFinishDate() {
    return finishDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public SalesTerminal getSalesTerminal() {
    return salesTerminal;
  }

  public void setSalesTerminal(SalesTerminal salesTerminal) {
    this.salesTerminal = salesTerminal;
  }

  public Boolean getFinished() {
    return finished;
  }

  public void setFinished(Boolean finished) {
    this.finished = finished;
  }
}
