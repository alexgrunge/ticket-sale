package justcloud.tickets.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {

  @Bean
  public Gson gson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create();
  }
}
