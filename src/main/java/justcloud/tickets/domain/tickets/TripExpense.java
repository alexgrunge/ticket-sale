package justcloud.tickets.domain.tickets;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import justcloud.tickets.domain.BaseModel;

@Entity
@Table(name = "trip_new_expense")
public class TripExpense extends BaseModel {

  public enum ExpenseType {
    WITH_RECEIPT,
    NO_RECEIPT
  }

  @Enumerated(EnumType.STRING)
  private ExpenseType expenseType;

  private BigDecimal amount;
  private String comments;

  @Column(name = "expense_type_data")
  private String type;

  /** @return the expenseType */
  public ExpenseType getExpenseType() {
    return expenseType;
  }

  /** @param expenseType the expenseType to set */
  public void setExpenseType(ExpenseType expenseType) {
    this.expenseType = expenseType;
  }

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
}
