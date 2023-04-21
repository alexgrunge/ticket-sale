package justcloud.tickets.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import justcloud.tickets.domain.Permission;
import justcloud.tickets.domain.Role;
import justcloud.tickets.domain.SocialData;
import justcloud.tickets.domain.SocialDataProvider;
import justcloud.tickets.domain.User;
import justcloud.tickets.domain.repository.RoleRepository;
import justcloud.tickets.domain.repository.SocialDataProviderRepository;
import justcloud.tickets.domain.repository.SocialDataRepository;
import justcloud.tickets.domain.repository.UserRepository;
import justcloud.tickets.dto.Profile;
import justcloud.tickets.dto.SocialUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private SocialDataProviderRepository providerRepository;

  @Autowired private SocialDataRepository socialDataRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private AuthService authService;

  public Map<String, Boolean> findUniques(SocialUserData previewData) {
    Map<String, Boolean> result = new HashMap<>();

    result.put("username", userRepository.findByUsername(previewData.getUsername()) != null);
    result.put("email", userRepository.findByEmail(previewData.getEmail()) != null);

    return result;
  }

  public SocialUserData registerUser(SocialUserData socialUserData) {
    Role clientRole = roleRepository.findOne("client-role");
    User user = new User();

    user.setName(socialUserData.getName());
    user.setLastName(socialUserData.getLastName());
    user.setEmail(socialUserData.getEmail());
    if (socialUserData.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(socialUserData.getPassword()));
    }
    user.setUsername(socialUserData.getUsername());
    user.setRoles(Arrays.asList(clientRole));

    userRepository.save(user);

    if (socialUserData.getProviderId() != null && socialUserData.getProvider() != null) {
      String socialProviderId =
          new StringBuilder(socialUserData.getProvider()).append("-id").toString();
      SocialDataProvider socialProvider = providerRepository.findOne(socialProviderId);
      SocialData socialData = new SocialData();
      socialData.setUser(user);
      socialData.setProviderId(socialUserData.getProviderId());
      socialData.setTokenId(socialUserData.getTokenId());
      socialData.setSocialDataProvider(socialProvider);
      socialDataRepository.save(socialData);
    }

    socialUserData.setToken(authService.createToken(socialUserData.getUsername()));

    return socialUserData;
  }

  public boolean checkPassword(String username, String password) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      return false;
    }
    boolean hasPermission = false;
    for (Role role : user.getRoles()) {
      for (Permission permission : role.getPermissions()) {
        hasPermission |= "approve-discount-id".equals(permission.getId());
        if (hasPermission) {
          break;
        }
      }
    }
    return hasPermission && passwordEncoder.matches(password, user.getPassword());
  }

  public Profile getCurrentProfile() {
    User currentUser = getCurrentUser();
    Profile result = new Profile();

    result.setRedirectUrl(
        currentUser.getRoles().stream()
            .min(
                (role1, role2) ->
                    role1.getRedirectUrlOrder().compareTo(role2.getRedirectUrlOrder()))
            .map(Role::getRedirectUrl)
            .orElse(null));

    result.setUsername(currentUser.getUsername());
    result.setEmail(currentUser.getEmail());
    result.setName(currentUser.getName());
    result.setLastName(currentUser.getLastName());
    result.setRoles(
        currentUser.getRoles().stream()
            .map(
                (role) -> {
                  return role.getName();
                })
            .collect(Collectors.toList()));

    result.setPermissions(
        currentUser.getRoles().stream()
            .flatMap(
                (role) -> {
                  return role.getPermissions().stream();
                })
            .map(
                (permission) -> {
                  return permission.getId();
                })
            .collect(Collectors.toList()));

    return result;
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User getCurrentUser() {
    return userRepository.findByUsername(getCurrentUsername());
  }

  public String getCurrentUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
}
