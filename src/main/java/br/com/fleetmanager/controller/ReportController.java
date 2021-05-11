package br.com.fleetmanager.controller;

import br.com.fleetmanager.Launcher;
import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.utils.Constants;
import javafx.application.HostServices;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;

import java.io.File;
import java.util.Map;

public class ReportController {

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

            HostServices hostServices = Launcher.getInstance().getHostServices();
            hostServices.showDocument(file.getAbsolutePath());

        } catch (JRException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private File GetFilePath() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(Launcher.getPrimaryStage());
    }

}
