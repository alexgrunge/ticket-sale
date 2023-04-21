package justcloud.tickets.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripSeatData {

  private int reservedOlderAdults;
  private int reservedStudents;
  private int maxOlderAdults;
  private int maxStudents;
  private int maxTotal;
}
