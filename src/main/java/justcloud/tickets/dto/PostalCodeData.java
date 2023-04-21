package justcloud.tickets.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostalCodeData {
  private String state;
  private String municipality;
  private List<String> settlements;
}
