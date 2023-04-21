package justcloud.tickets.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import justcloud.tickets.domain.*;
import justcloud.tickets.domain.repository.IndividualRepository;
import justcloud.tickets.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private TransactionTemplate transactionTemplate;

  @Autowired private UserRepository userRepository;

  @Autowired private IndividualRepository individualRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return transactionTemplate.execute(
        new TransactionCallback<UserDetails>() {
          @Override
          public UserDetails doInTransaction(TransactionStatus status) {
            User user = userRepository.findByUsername(username);
            if (user == null) {
              throw new UsernameNotFoundException(username);
            }
            Individual individual = individualRepository.findByUser(user);
            if (individual != null && (individual.getActive() == null || !individual.getActive())) {
              throw new UsernameNotFoundException(username);
            }
            return new PersonUserDetails(user);
          }
        });
  }

  private static final class PersonUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private PersonUserDetails(User user) {
      this.username = user.getUsername();
      this.password = user.getPassword();
      List<Role> roles = user.getRoles();
      List<Permission> permissions =
          roles.stream()
              .flatMap((role) -> Stream.of(role.getPermissions().toArray(new Permission[0])))
              .collect(Collectors.toList());
      List<String> authorityStrings =
          roles.stream()
              .map((role) -> "ROLE_" + role.getId().toUpperCase())
              .map((roleName) -> roleName.replaceAll("-", "_"))
              .collect(Collectors.toList());
      authorityStrings.addAll(
          permissions.stream()
              .map((role) -> "PERM_" + role.getId().toUpperCase())
              .map((roleName) -> roleName.replaceAll("-", "_"))
              .collect(Collectors.toList()));
      authorities = AuthorityUtils.createAuthorityList(authorityStrings.toArray(new String[0]));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
    }

    @Override
    public String getUsername() {
      return username;
    }

    @Override
    public String getPassword() {
      return password;
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
