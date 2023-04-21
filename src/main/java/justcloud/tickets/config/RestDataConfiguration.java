package justcloud.tickets.config;

import justcloud.tickets.domain.OfficeLocation;
import justcloud.tickets.domain.User;
import justcloud.tickets.search.RouteSegment;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
public class RestDataConfiguration extends RepositoryRestMvcConfiguration {

  @Override
  protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(User.class);
    config.exposeIdsFor(OfficeLocation.class);
    config.exposeIdsFor(RouteSegment.StopData.class);
  }
}
