package justcloud.tickets.service;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdGenerationService {

  private SecureRandom random = new SecureRandom();

  @Autowired private HashFunction hashFunction;

  public String generateId() {
    BigInteger num = new BigInteger(64, random);
    HashCode hash = hashFunction.newHasher().putBytes(num.toByteArray()).hash();
    return hash.toString();
  }
}
