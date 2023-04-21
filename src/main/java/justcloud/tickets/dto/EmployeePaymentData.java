package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;

/** Created by iamedu on 6/25/16. */
public class EmployeePaymentData {

  private BigDecimal amountPayed;
  private Date dateCreated;

  public BigDecimal getAmountPayed() {
    return amountPayed;
  }

  public void setAmountPayed(BigDecimal amountPayed) {
    this.amountPayed = amountPayed;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
}
