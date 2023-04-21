package justcloud.tickets.service;

import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired private TokenStore tokenStore;

  @Autowired private UserDetailsService userDetailsService;

  public OAuth2AccessToken createToken(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    Map<String, String> requestParameters = new HashMap<>();
    String clientId = "clientapp";
    boolean approved = true;
    Set<String> scope =
        new HashSet<String>() {
          private static final long serialVersionUID = -1113582265862594563L;

          {
            add("read");
            add("write");
          }
        };
    Set<String> resourceIds = new HashSet<>();
    Set<String> responseTypes =
        new HashSet<String>() {
          private static final long serialVersionUID = -1113582265865921787L;

          {
            add("code");
          }
        };
    Map<String, Serializable> extensionProperties = new HashMap<>();

    OAuth2Request request =
        new OAuth2Request(
            requestParameters,
            clientId,
            userDetails.getAuthorities(),
            approved,
            scope,
            resourceIds,
            null,
            responseTypes,
            extensionProperties);

    org.springframework.security.core.userdetails.User u =
        new org.springframework.security.core.userdetails.User(
            userDetails.getUsername(), "", true, true, true, true, userDetails.getAuthorities());

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(u, null, userDetails.getAuthorities());
    OAuth2Authentication auth = new OAuth2Authentication(request, authenticationToken);

    return getDefaultTokenServices().createAccessToken(auth);
  }

  private DefaultTokenServices getDefaultTokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    defaultTokenServices.setSupportRefreshToken(true);
    defaultTokenServices.setTokenStore(tokenStore);

    return defaultTokenServices;
  }
}
