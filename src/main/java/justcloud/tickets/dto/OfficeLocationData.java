package justcloud.tickets.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfficeLocationData {
  private String name;
  private String address;
  private BigDecimal latitude;
  private BigDecimal longitude;
}
