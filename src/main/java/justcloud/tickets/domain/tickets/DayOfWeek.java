package justcloud.tickets.domain.tickets;

import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;

public enum DayOfWeek {
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY,
  SUNDAY;

  public static DayOfWeek extractDayOfWeek(Date date) {
    DateTime dateTime = new DateTime(date);
    String name = dateTime.dayOfWeek().getAsText(Locale.US).toUpperCase();

    return DayOfWeek.valueOf(name);
  }
}
