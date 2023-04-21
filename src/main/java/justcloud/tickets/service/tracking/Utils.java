package justcloud.tickets.service.tracking;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Utils {

  public static final double degreesToKm;

  @Value("${tracking.atEndRange}")
  double atEndRange;

  @Value("${tracking.maxDistance}")
  double maxDistance;

  @Value("${tracking.outRange}")
  double outRange;

  @Value("${tracking.minDelay}")
  int minDelay;

  static {
    degreesToKm =
        360.0 / (40000.0 * Math.cos(Math.PI * 23.0 / 180.0)); // México está aprox en los 23 grados
  }

  @PostConstruct
  void init() {
    atEndRange *= degreesToKm;
    maxDistance *= degreesToKm;
    outRange *= degreesToKm;
  }

  List<String> split(String str) {
    List<String> res = new LinkedList<String>();
    int start = 0;
    int pos;
    while ((pos = str.indexOf('|', start)) != -1) {
      res.add(str.substring(start, pos).trim());
      start = pos + 1;
    }
    if (start > str.length()) res.add("");
    else res.add(str.substring(start).trim());
    log.debug(str);
    for (String s : res) log.debug("\t'" + s + "'");
    return res;
  }
}
