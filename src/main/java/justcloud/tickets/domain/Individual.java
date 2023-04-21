package justcloud.tickets.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class")
public class Individual extends BaseModel {

  @OneToOne private User user;

  @NotBlank private String name;

  @NotBlank private String lastName;
  private String secondLastName;

  private String rfc;
  private String curp;

  private Boolean active;

  @ManyToOne private Address address;

  /** @return the user */
  public User getUser() {
    return user;
  }

  /** @param user the user to set */
  public void setUser(User user) {
    this.user = user;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the lastName */
  public String getLastName() {
    return lastName;
  }

  /** @param lastName the lastName to set */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /** @return the secondLastName */
  public String getSecondLastName() {
    return secondLastName;
  }

  /** @param secondLastName the secondLastName to set */
  public void setSecondLastName(String secondLastName) {
    this.secondLastName = secondLastName;
  }

  /** @return the rfc */
  public String getRfc() {
    return rfc;
  }

  /** @param rfc the rfc to set */
  public void setRfc(String rfc) {
    this.rfc = rfc;
  }

  /** @return the curp */
  public String getCurp() {
    return curp;
  }

  /** @param curp the curp to set */
  public void setCurp(String curp) {
    this.curp = curp;
  }

  /** @return the address */
  public Address getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(Address address) {
    this.address = address;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
