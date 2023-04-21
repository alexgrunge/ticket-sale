package justcloud.tickets.controller;

import java.util.Map;
import justcloud.tickets.dto.SocialUserData;
import justcloud.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RegistrationController {

  @Autowired private UserService userService;

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public SocialUserData registerUser(@RequestBody SocialUserData socialUserData) {
    return userService.registerUser(socialUserData);
  }

  @RequestMapping(value = "/register/uniques", method = RequestMethod.POST)
  public Map<String, Boolean> findUniques(@RequestBody SocialUserData previewData) {
    return userService.findUniques(previewData);
  }
}
