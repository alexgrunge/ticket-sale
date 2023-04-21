package justcloud.tickets.domain.sales;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import justcloud.tickets.domain.BaseModel;

/** Created by iamedu on 6/25/16. */
@Entity
public class CashCheckpoint extends BaseModel {

  private BigDecimal previousAmount;
  private BigDecimal newAmount;

  @NotNull @ManyToOne private SalesShift salesShift;

  public BigDecimal getPreviousAmount() {
    return previousAmount;
  }

  public void setPreviousAmount(BigDecimal previousAmount) {
    this.previousAmount = previousAmount;
  }

  public BigDecimal getNewAmount() {
    return newAmount;
  }

  public void setNewAmount(BigDecimal newAmount) {
    this.newAmount = newAmount;
  }

  public SalesShift getSalesShift() {
    return salesShift;
  }

  public void setSalesShift(SalesShift salesShift) {
    this.salesShift = salesShift;
  }
}
