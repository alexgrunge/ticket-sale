package justcloud.tickets.dto;

import java.math.BigDecimal;

public class TripAdvanceData {

  private BigDecimal amount;
  private String comments;

  /** @return the amount */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @param amount the amount to set */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** @return the comments */
  public String getComments() {
    return comments;
  }

  /** @param comments the comments to set */
  public void setComments(String comments) {
    this.comments = comments;
  }
}
