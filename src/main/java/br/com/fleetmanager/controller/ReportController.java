package br.com.fleetmanager.controller;

import br.com.fleetmanager.Main;
import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.utils.Constants;
import javafx.application.HostServices;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;

import java.io.File;
import java.util.Map;

public class ReportController {

    private static final String destFileName = "report.pdf";
    private final Map<String, Object> parameters;
    private final String reportName;

    public ReportController(Map<String, Object> parameters, String reportName) {
        this.parameters = parameters;
        this.reportName = reportName;
    }

    public void GeneratePDF() {
        try {
            File file = GetFilePath();
            if (file == null)
                return;

            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream(Constants.sJReportsFolder + this.reportName + ".jrxml"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                this.parameters, new ConnectionFactory().getNewConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());

            HostServices hostServices = Main.getInstance().getHostServices();
            hostServices.showDocument(file.getAbsolutePath());

        } catch (JRException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private File GetFilePath() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(Main.getPrimaryStage());
    }

}
