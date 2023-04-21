package justcloud.tickets.controller;

import justcloud.tickets.dto.PostalCodeData;
import justcloud.tickets.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

  @Autowired private AddressService addressService;

  @RequestMapping("/{postalCode}")
  public PostalCodeData findAddress(@PathVariable("postalCode") String postalCode) {
    return addressService.lookupPostalCode(postalCode);
  }
}
