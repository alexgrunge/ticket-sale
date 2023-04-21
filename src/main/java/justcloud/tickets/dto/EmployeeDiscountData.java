package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;

public class EmployeeDiscountData {

  private BigDecimal discountAmount;
  private Date dateCreated;

  /** @return the discountAmount */
  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  /** @param discountAmount the discountAmount to set */
  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
  }

  /** @return the dateCreated */
  public Date getDateCreated() {
    return dateCreated;
  }

  /** @param dateCreated the dateCreated to set */
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
}
