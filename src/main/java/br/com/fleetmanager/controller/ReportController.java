package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.utils.Constants;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.util.Map;

public class ReportController {

    private static String destFileName = "report.pdf";
    private final Map<String, Object> parameters;
    private final String reportName;

    public ReportController(Map<String, Object> parameters, String reportName) {
        this.parameters = parameters;
        this.reportName = reportName;
    }

    public void GerarPDF() {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/jasperReports/syntheticReport.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                this.parameters, new ConnectionFactory().getNewConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
        } catch (JRException throwables) {
            throw new RuntimeException(throwables);
        }
    }

}
