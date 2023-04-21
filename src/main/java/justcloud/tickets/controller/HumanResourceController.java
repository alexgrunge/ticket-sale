package justcloud.tickets.controller;

import java.util.Map;
import justcloud.tickets.service.HumanResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HumanResourceController {

  @Autowired private HumanResourceService humanResourceService;

  @RequestMapping(method = RequestMethod.POST, value = "/upload")
  public @ResponseBody Map<String, Object> uploadFile(
      @RequestParam("file") MultipartFile file, @RequestParam("type") String type)
      throws Exception {
    return humanResourceService.processPayments(file.getInputStream(), type);
  }
}
