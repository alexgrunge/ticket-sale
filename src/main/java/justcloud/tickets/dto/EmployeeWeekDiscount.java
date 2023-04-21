package justcloud.tickets.dto;

import java.math.BigDecimal;

/** Created by iamedu on 7/8/16. */
public class EmployeeWeekDiscount {
  private String weekName;
  private BigDecimal weekAmount;

  public String getWeekName() {
    return weekName;
  }

  public void setWeekName(String weekName) {
    this.weekName = weekName;
  }

  public BigDecimal getWeekAmount() {
    return weekAmount;
  }

  public void setWeekAmount(BigDecimal weekAmount) {
    this.weekAmount = weekAmount;
  }
}
