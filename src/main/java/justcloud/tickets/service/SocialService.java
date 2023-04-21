package justcloud.tickets.service;

import static com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import java.util.*;
import justcloud.tickets.domain.SocialData;
import justcloud.tickets.domain.repository.SocialDataRepository;
import justcloud.tickets.domain.repository.UserRepository;
import justcloud.tickets.dto.SocialUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SocialService {

  @Value("${google.clientId}")
  private String clientId;

  @Autowired private SocialDataRepository socialDataRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthService authService;

  public SocialUserData lookupSocialData(String tokenId, String provider) {
    SocialUserData socialUserData;
    if ("facebook".equals(provider)) {
      socialUserData = lookupSocialFacebookData(tokenId);
    } else if ("google".equals(provider)) {
      socialUserData = lookupSocialGoogleData(tokenId);
    } else {
      throw new UnsupportedOperationException("Cannot access provider " + provider);
    }

    SocialData socialData = findExistingSocialData(socialUserData);

    if (socialData != null) {
      socialUserData.setUsername(socialData.getUser().getUsername());
      socialData.setTokenId(socialUserData.getTokenId());
      socialDataRepository.save(socialData);

      // If we have social data the user should exist, let's login
      OAuth2AccessToken token = authService.createToken(socialData.getUser().getUsername());
      socialUserData.setToken(token);
    }

    return socialUserData;
  }

  private SocialData findExistingSocialData(SocialUserData socialUserData) {
    SocialData socialData = socialDataRepository.findByProviderId(socialUserData.getProviderId());

    if (socialData == null) {
      justcloud.tickets.domain.User user =
          userRepository.findByUsername(socialUserData.getUsername());
      if (user == null) {
        user = userRepository.findByEmail(socialUserData.getEmail());
      }
      if (user != null) {
        socialData = socialDataRepository.findByUser(user);
      }
    }

    return socialData;
  }

  private SocialUserData lookupSocialGoogleData(String tokenId) {
    GoogleIdTokenVerifier verifier =
        new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new JacksonFactory())
            .setAudience(Arrays.asList(clientId))
            .setIssuer("accounts.google.com")
            .build();

    try {
      GoogleIdToken token = verifier.verify(tokenId);
      Payload payload = token.getPayload();

      SocialUserData socialUserData = new SocialUserData();
      socialUserData.setTokenId(tokenId);

      socialUserData.setEmail((String) payload.get("email"));
      socialUserData.setName((String) payload.get("given_name"));
      socialUserData.setLastName((String) payload.get("family_name"));
      socialUserData.setProvider("google");

      socialUserData.setProviderId(payload.getSubject());

      return socialUserData;
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  private SocialUserData lookupSocialFacebookData(String tokenId) {
    FacebookClient facebookClient = new DefaultFacebookClient(tokenId, Version.VERSION_2_5);
    User facebookUser =
        facebookClient.fetchObject(
            "me", User.class, Parameter.with("fields", "id,first_name,last_name,email"));
    SocialUserData socialUserData = new SocialUserData();
    socialUserData.setTokenId(tokenId);
    socialUserData.setEmail(facebookUser.getEmail());
    socialUserData.setName(facebookUser.getFirstName());
    socialUserData.setLastName(facebookUser.getLastName());
    socialUserData.setProvider("facebook");
    socialUserData.setProviderId(facebookUser.getId());
    return socialUserData;
  }
}
