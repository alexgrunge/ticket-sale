package justcloud.tickets.controller;

import justcloud.tickets.dto.Profile;
import justcloud.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PersonController {

  @Autowired private UserService userService;

  @RequestMapping(value = "/currentProfile", method = RequestMethod.GET)
  public Profile getCurrentProfile() {
    return userService.getCurrentProfile();
  }
}
