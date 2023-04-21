package justcloud.tickets.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

@Service
public class JasperService {

  public JasperReport loadReport(String report) throws Exception {
    InputStream jasperStream = getClass().getResourceAsStream("/" + report + ".jasper");
    return (JasperReport) JRLoader.loadObject(jasperStream);
  }

  public byte[] getPdfBytes(String report, Map<String, Object> parameters, JRDataSource ds)
      throws Exception {
    InputStream jasperStream = getClass().getResourceAsStream("/" + report + ".jasper");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, ds);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    JRPdfExporter exporter = new JRPdfExporter();

    SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
    SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(baos);

    exporter.setExporterInput(exporterInput);
    exporter.setExporterOutput(exporterOutput);
    exporter.exportReport();
    baos.flush();

    return baos.toByteArray();
  }

  public byte[] getPdfBytes(String report, Map<String, Object> parameters) throws Exception {
    return getPdfBytes(report, parameters, new JREmptyDataSource());
  }
}
