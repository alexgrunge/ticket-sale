package justcloud.tickets.service;

import java.util.List;
import java.util.stream.Collectors;
import justcloud.tickets.domain.Settlement;
import justcloud.tickets.domain.repository.SettlementRepository;
import justcloud.tickets.dto.PostalCodeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  @Autowired private SettlementRepository settlementRepository;

  public PostalCodeData lookupPostalCode(String postalCode) {
    List<Settlement> settlements = settlementRepository.findAllByPostalCode(postalCode);

    return settlements.stream()
        .findFirst()
        .map(
            settlement -> {
              return PostalCodeData.builder()
                  .state(settlement.getState().getName())
                  .municipality(settlement.getMunicipality().getName())
                  .settlements(
                      settlements.stream().map(Settlement::getName).collect(Collectors.toList()))
                  .build();
            })
        .orElse(null);
  }
}
