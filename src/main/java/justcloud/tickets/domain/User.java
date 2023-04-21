package justcloud.tickets.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tickets_user")
public class User extends BaseModel {

  @NotBlank private String username;

  @JsonIgnore private String password;

  @NotBlank @Email private String email;

  @NotBlank private String name;
  @NotBlank private String lastName;

  private String secondLastName;

  @OneToMany
  @JoinTable(
      name = "tickets_user_role",
      joinColumns = @JoinColumn(name = "user_roles_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;

  /** @return the username */
  public String getUsername() {
    return username;
  }

  /** @param username the username to set */
  public void setUsername(String username) {
    this.username = username;
  }

  /** @return the password */
  public String getPassword() {
    return password;
  }

  /** @param password the password to set */
  public void setPassword(String password) {
    this.password = password;
  }

  /** @return the email */
  public String getEmail() {
    return email;
  }

  /** @param email the email to set */
  public void setEmail(String email) {
    this.email = email;
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

  /** @return the roles */
  public List<Role> getRoles() {
    return roles;
  }

  /** @param roles the roles to set */
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }
}
