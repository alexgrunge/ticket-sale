package justcloud.tickets.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfig {

  @Bean(name = "ticketFreemarkerConfig")
  @Qualifier("ticketFreemarkerConfig")
  public freemarker.template.Configuration freemarkerConfig() {
    freemarker.template.Configuration config =
        new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);

    config.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/templates/");

    return config;
  }
}
