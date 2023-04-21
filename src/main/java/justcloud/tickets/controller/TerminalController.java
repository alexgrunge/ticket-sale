package justcloud.tickets.controller;

import justcloud.tickets.dto.SalesTerminalData;
import justcloud.tickets.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

  @Autowired private SaleService saleService;

  @RequestMapping("/{terminalId}")
  private SalesTerminalData findByTerminalId(@PathVariable("terminalId") String terminalId) {
    return saleService.findTerminal(terminalId);
  }
}
