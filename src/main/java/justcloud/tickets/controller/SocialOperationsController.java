package justcloud.tickets.controller;

import justcloud.tickets.dto.SocialUserData;
import justcloud.tickets.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
public class SocialOperationsController {

  @Autowired private SocialService socialService;

  @RequestMapping("/validate")
  public SocialUserData lookupSocialData(
      @RequestParam("provider") String provider, @RequestParam("tokenId") String tokenId) {
    return socialService.lookupSocialData(tokenId, provider);
  }
}
