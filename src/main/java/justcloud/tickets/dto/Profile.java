package justcloud.tickets.dto;

import java.util.List;

public class Profile {

  private String username;
  private String email;
  private String name;
  private String lastName;

  private String redirectUrl;

  private List<String> roles;
  private List<String> permissions;

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  /** @return the username */
  public String getUsername() {
    return username;
  }

  /** @param username the username to set */
  public void setUsername(String username) {
    this.username = username;
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

  /** @return the roles */
  public List<String> getRoles() {
    return roles;
  }

  /** @param roles the roles to set */
  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  /** @return the permissions */
  public List<String> getPermissions() {
    return permissions;
  }

  /** @param permissions the permissions to set */
  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
