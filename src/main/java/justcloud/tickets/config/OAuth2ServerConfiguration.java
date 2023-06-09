package justcloud.tickets.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class OAuth2ServerConfiguration {

  private static final String RESOURCE_ID = "restService";

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http.anonymous()
          .and()
          .authorizeRequests()
          .antMatchers("/barcode/**/**")
          .permitAll()
          .antMatchers("/qr/**/**")
          .permitAll()
          .anyRequest()
          .permitAll();
      // .anyRequest().authenticated();
      // .antMatchers("/greeting").authenticated()
    }
  }

  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration
      extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      // @formatter:off
      endpoints.tokenStore(tokenStore()).authenticationManager(this.authenticationManager);
      // @formatter:on
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients
          .inMemory()
          .withClient("clientapp")
          .authorizedGrantTypes("password", "refresh_token")
          .authorities("USER")
          .scopes("read", "write")
          .resourceIds(RESOURCE_ID)
          .secret("123456");
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
      DefaultTokenServices tokenServices = new DefaultTokenServices();
      tokenServices.setSupportRefreshToken(true);
      tokenServices.setTokenStore(tokenStore());
      return tokenServices;
    }

    @Bean
    public OAuth2RequestFactory oauth2RequestFactory(ClientDetailsService clientDetailsService) {
      return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    public TokenStore tokenStore() {
      return new JdbcTokenStore(dataSource);
    }
  }
}
