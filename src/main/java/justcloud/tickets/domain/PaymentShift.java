package justcloud.tickets.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.sales.SalesTerminal;

/** Created by iamedu on 6/25/16. */
@Entity
public class PaymentShift extends BaseModel {

  private BigDecimal startingAmount;

  @NotNull @ManyToOne private User user;

  @NotNull @ManyToOne private User chiefUser;

  @NotNull @ManyToOne private SalesTerminal salesTerminal;

  private Boolean finished = false;

  private Date finishDate;

  public User getChiefUser() {
    return chiefUser;
  }

  public void setChiefUser(User chiefUser) {
    this.chiefUser = chiefUser;
  }

  public BigDecimal getStartingAmount() {
    return startingAmount;
  }

  public void setStartingAmount(BigDecimal startingAmount) {
    this.startingAmount = startingAmount;
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

  public Date getFinishDate() {
    return finishDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }
}
