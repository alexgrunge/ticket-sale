package justcloud.tickets.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class SocialData extends BaseModel {

  @NotBlank private String tokenId;

  @NotBlank private String providerId;

  @OneToOne private User user;

  @ManyToOne private SocialDataProvider socialDataProvider;

  /** @return the tokenId */
  public String getTokenId() {
    return tokenId;
  }

  /** @param tokenId the tokenId to set */
  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  /** @return the providerId */
  public String getProviderId() {
    return providerId;
  }

  /** @param providerId the providerId to set */
  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  /** @return the user */
  public User getUser() {
    return user;
  }

  /** @param user the user to set */
  public void setUser(User user) {
    this.user = user;
  }

  /** @return the socialDataProvider */
  public SocialDataProvider getSocialDataProvider() {
    return socialDataProvider;
  }

  /** @param socialDataProvider the socialDataProvider to set */
  public void setSocialDataProvider(SocialDataProvider socialDataProvider) {
    this.socialDataProvider = socialDataProvider;
  }
}
