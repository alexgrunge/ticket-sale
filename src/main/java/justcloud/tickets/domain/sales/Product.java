package justcloud.tickets.domain.sales;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import justcloud.tickets.domain.BaseModel;

@Entity
@Inheritance
@DiscriminatorColumn(name = "class")
public class Product extends BaseModel {

  private String name;
  private String description;

  @Enumerated(EnumType.STRING)
  private ProductStatus status = ProductStatus.ACTIVE;

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the description */
  public String getDescription() {
    return description;
  }

  /** @param description the description to set */
  public void setDescription(String description) {
    this.description = description;
  }

  /** @return the status */
  public ProductStatus getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(ProductStatus status) {
    this.status = status;
  }
}
