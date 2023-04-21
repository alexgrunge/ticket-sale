package justcloud.tickets.util;

import java.util.Optional;

/** Created by iamedu on 8/27/16. */
public class TimeZoneUtils {
  public static String cleanTimezone(String timeZone) {
    return Optional.ofNullable(timeZone)
        .map(
            tz -> {
              if (tz.endsWith("/")) {
                return tz.substring(0, tz.length() - 1);
              } else {
                return tz;
              }
            })
        .orElse(null);
  }
}
