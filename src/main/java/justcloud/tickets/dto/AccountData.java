package justcloud.tickets.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AccountData {

  private String name;
  private BigDecimal amount;
  private Date dateCreated;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** @return the amount */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @param amount the amount to set */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
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
