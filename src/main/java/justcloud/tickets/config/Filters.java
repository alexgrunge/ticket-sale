package justcloud.tickets.config;

import justcloud.tickets.filter.CorsFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class Filters {

  // @Bean
  // public FilterRegistrationBean
  // securityFilter(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter
  // securityFilter) {
  //     FilterRegistrationBean registration = new FilterRegistrationBean();
  //     registration.setFilter(securityFilter);
  //     registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
  //     registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
  //     return registration;
  // }

  @Bean
  public FilterRegistrationBean corsFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new CorsFilter());
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }
}
