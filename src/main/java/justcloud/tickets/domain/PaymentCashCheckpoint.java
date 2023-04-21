package justcloud.tickets.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/** Created by iamedu on 6/25/16. */
@Entity
public class PaymentCashCheckpoint extends BaseModel {

  private BigDecimal previousAmount;
  private BigDecimal newAmount;

  @NotNull @ManyToOne private PaymentShift paymentShift;

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

  public PaymentShift getPaymentShift() {
    return paymentShift;
  }

  public void setPaymentShift(PaymentShift paymentShift) {
    this.paymentShift = paymentShift;
  }
}
