package justcloud.tickets.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

  @Value("${sendgrid.username}")
  private String sendGridUser;

  @Value("${sendgrid.password}")
  private String sendGridPassword;

  @Bean
  public SendGrid sendGrid() {
    return new SendGrid(sendGridUser, sendGridPassword);
  }
}
