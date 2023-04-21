package justcloud.tickets.domain;

import java.util.*;
import javax.persistence.*;

@Entity
public class Role extends BaseModel {

  private String name;
  private String description;
  private String redirectUrl;
  private Integer redirectUrlOrder = 0;
  private Boolean active = true;

  @OneToMany
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_permissions_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private List<Permission> permissions;

  public Integer getRedirectUrlOrder() {
    return redirectUrlOrder;
  }

  public void setRedirectUrlOrder(Integer redirectUrlOrder) {
    this.redirectUrlOrder = redirectUrlOrder;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public Boolean getActive() {
    return active;
  }

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

  /** @return the active */
  public Boolean isActive() {
    return active;
  }

  /** @param active the active to set */
  public void setActive(Boolean active) {
    this.active = active;
  }

  /** @return the permissions */
  public List<Permission> getPermissions() {
    return permissions;
  }

  /** @param permissions the permissions to set */
  public void setPermissions(List<Permission> permissions) {
    this.permissions = permissions;
  }
}
