package justcloud.tickets.dto;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class SocialUserData {

  private OAuth2AccessToken token;

  private String providerId;

  private String provider;

  private String tokenId;

  private String username;

  private String password;

  private String email;

  private String name;

  private String lastName;

  /** @return the token */
  public OAuth2AccessToken getToken() {
    return token;
  }

  /** @param token the token to set */
  public void setToken(OAuth2AccessToken token) {
    this.token = token;
  }

  /** @return the providerId */
  public String getProviderId() {
    return providerId;
  }

  /** @param providerId the providerId to set */
  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  /** @return the provider */
  public String getProvider() {
    return provider;
  }

  /** @param provider the provider to set */
  public void setProvider(String provider) {
    this.provider = provider;
  }

  /** @return the tokenId */
  public String getTokenId() {
    return tokenId;
  }

  /** @param tokenId the tokenId to set */
  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

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
}
