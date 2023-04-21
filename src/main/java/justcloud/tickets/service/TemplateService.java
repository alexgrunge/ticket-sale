package justcloud.tickets.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

  @Autowired
  @Qualifier("ticketFreemarkerConfig")
  private Configuration config;

  public String buildTemplate(String templateName, Map<String, Object> params) throws Exception {
    Template template = config.getTemplate(templateName + ".ftl");
    StringWriter writer = new StringWriter();
    template.process(params, writer);
    writer.flush();
    return writer.getBuffer().toString();
  }
}
