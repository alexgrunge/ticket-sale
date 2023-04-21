package justcloud.tickets.dto;

import java.math.BigDecimal;

public class TripExpense {

  private Boolean receipt;
  private BigDecimal amount;
  private BigDecimal dieselLiters;
  private BigDecimal dieselCost;
  private String comments;
  private String type;

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

  /** @return the type */
  public String getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(String type) {
    this.type = type;
  }

  public Boolean getReceipt() {
    return receipt;
  }

  public void setReceipt(Boolean receipt) {
    this.receipt = receipt;
  }

  public BigDecimal getDieselLiters() {
    return dieselLiters;
  }

  public void setDieselLiters(BigDecimal dieselLiters) {
    this.dieselLiters = dieselLiters;
  }

  public BigDecimal getDieselCost() {
    return dieselCost;
  }

  public void setDieselCost(BigDecimal dieselCost) {
    this.dieselCost = dieselCost;
  }
}
