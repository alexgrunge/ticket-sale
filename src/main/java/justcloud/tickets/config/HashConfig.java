package justcloud.tickets.config;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashConfig {

  @Bean
  public HashFunction hashFunction() {
    return Hashing.murmur3_32();
  }
}
