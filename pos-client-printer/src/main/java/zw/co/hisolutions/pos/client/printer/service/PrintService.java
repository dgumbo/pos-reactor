package zw.co.hisolutions.pos.client.printer.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.client.printer.model.PrintMsg;
import zw.co.hisolutions.pos.client.printer.model.PrintMsgRequestType;
//Added By Comfort

import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterResolution;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import zw.co.hisolutions.pos.client.printer.Utils;

/**
 *
 * @author com4t
 */
@Slf4j
@Service
public class PrintService {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PrintMsg processRequest(PrintMsg msg) {
        PrintMsg printMsg = new PrintMsg();

        if (msg.getRequestType() == PrintMsgRequestType.GET_PRINTERS) {
            printMsg.setPrinters(getPrinterNames());
            printMsg.setSuccessStatus(true);
            printMsg.setRequestType(PrintMsgRequestType.RESPONSE);
            return printMsg;
        } else if (msg.getRequestType() == PrintMsgRequestType.PRINTER_JOB) {
            printMsg.setSuccessStatus(printDocModified3(msg.getSelectedPrinter(), msg.getFilename(), Base64.getDecoder().decode(msg.getDocument().getBytes()), msg.getJsonData()));
            printMsg.setRequestType(PrintMsgRequestType.RESPONSE);
            return printMsg;
        }

        return printMsg;
    }

    private List<String> getPrinterNames() {
        javax.print.PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
        List<String> printers = new ArrayList<>();
        for (javax.print.PrintService service : pservices) {
            if (service != null) {
                printers.add(service.getName());
            }
        }
        return printers;
    }

    private boolean printDocModified2(String printerName, String fileName, byte[] bytes, String jsonData) {
        log.info("printer name: {}, filename: {}", printerName, fileName);
        try {
            //PrinterJob printerJob = PrinterJob.getPrinterJob();

            // PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
            //printerJob.defaultPage(pageFormat);
            PdfOutputModel reportModel = gson.fromJson(jsonData, PdfOutputModel.class);
            if (reportModel.getParameter().containsKey("barcode")) {
                Object val = reportModel.getParameter().get("barcode");
                /*  if (val instanceof String) {
                    Image barcodeImage = Utils.getBarcode((String) val);
                    reportModel.getParameter().put("barcode", barcodeImage);
                }*/
            }
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            //jasperReportsContext.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
            //  jasperReportsContext.setProperty("net.sf.jasperreports.default.font.name", "Sans Serif");
            JasperPrint jprint = null;
            /* (JasperPrint) JasperFillManager
                    .fillReport(new ByteArrayInputStream(bytes), reportModel
                            .getParameter(), new JRBeanCollectionDataSource(reportModel
                            .getFileldslist()));
            jprint = null; */
            if (fileName.equalsIgnoreCase("LABEL") || fileName.equalsIgnoreCase("TRAILER")) {
                String jrxmlFileName = "F:/LABEL.jrxml";
                // JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
                JasperReport report = JasperCompileManager.compileReport(jrxmlFileName);
                log.debug("..................Orientation{}", report.getOrientationValue());
                jprint = (JasperPrint) JasperFillManager
                        .fillReport(report, reportModel
                                .getParameter(), new JRBeanCollectionDataSource(reportModel
                                        .getFileldslist()));
            }
            //JasperPrintManager.printReport(jprint, false);
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            // printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
            printRequestAttributeSet.add(new Copies(1));

            PrinterName printerName1 = new PrinterName(printerName, null); //gets printer 

            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
            printServiceAttributeSet.add(printerName1);
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            /* exporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);*/
            SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
            configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
            configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
            configuration.setDisplayPageDialog(false);
            configuration.setDisplayPrintDialog(false);

            exporter.setExporterInput(new SimpleExporterInput(jprint));
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } catch (Exception ex) {
            log.error("Error:", ex);
        }
        return false;
    }

    private boolean printDocModified(String printerName, String fileName, byte[] bytes) {
        //log.error("log .. {} {}", bytes.length , bytes);
        javax.print.PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
        for (javax.print.PrintService service : pservices) {
            if (service != null) {
                if (service.getName().contains(printerName)) {
                    try {

                        if (fileName.equalsIgnoreCase("FINAL_RECEIPT") || fileName.equalsIgnoreCase("RECEIPT")) {
                            PDDocument document = PDDocument.load(bytes);//.load(new File("C:/"+fileName+".pdf"));
                            PrinterJob job1 = PrinterJob.getPrinterJob();
                            job1.setJobName("RECEIPT");
                            //job.setPageable(new PDFPageable(document));
                            PrintRequestAttributeSet aset1 = new HashPrintRequestAttributeSet();
                            aset1.add(OrientationRequested.PORTRAIT);
                            aset1.add(PrintQuality.HIGH);
                            aset1.add(Chromaticity.COLOR);
                            aset1.add(MediaSizeName.INVOICE);
                            job1.setPrintable(new PDFPrintable(document, Scaling.SCALE_TO_FIT, false, 2400.0f, true));
                            job1.setPrintService(service);
                            aset1.add(new PrinterResolution(203, 203, ResolutionSyntax.DPI));
                            log.info("print called: RECEIPT");
                            job1.print(aset1);
                            log.info("printed .......:");
                            document.close();
                            job1 = null;
                            job1.cancel();
                        } else if (fileName.equalsIgnoreCase("LABEL") || fileName.equalsIgnoreCase("TRAILER")) {
                            PDDocument document = PDDocument.load(bytes);//.load(new File("C:/"+fileName+".pdf"));
                            PrinterJob job2 = PrinterJob.getPrinterJob();
                            job2.setJobName("LABEL");
                            //job.setPageable(new PDFPageable(document));
                            PrintRequestAttributeSet aset2 = new HashPrintRequestAttributeSet();
                            aset2.add(OrientationRequested.PORTRAIT);
                            aset2.add(PrintQuality.HIGH);
                            aset2.add(Chromaticity.COLOR);
                            aset2.add(new MediaPrintableArea(0, 0, 80, 140, MediaPrintableArea.MM));
                            job2.setPrintable(new PDFPrintable(document, Scaling.SCALE_TO_FIT, false, 2400.0f, true));
                            job2.setPrintService(service);
                            log.info("print called: LABEL");
                            job2.print(aset2);
                            log.info("printed .......:");
                            document.close();
                            job2 = null;
                            job2.cancel();
                        } else {
                            PDDocument document3 = PDDocument.load(bytes);//.load(new File("C:/"+fileName+".pdf"));
                            PrinterJob job3 = PrinterJob.getPrinterJob();
                            job3.setJobName("CLAIMFORM");
                            //job.setPageable(new PDFPageable(document));
                            PrintRequestAttributeSet aset3 = new HashPrintRequestAttributeSet();
                            aset3.add(OrientationRequested.PORTRAIT);
                            aset3.add(PrintQuality.HIGH);
                            aset3.add(Chromaticity.COLOR);
                            job3.setPrintable(new PDFPrintable(document3));
                            aset3.add(MediaSizeName.ISO_A4);
                            job3.setPrintService(service);
                            log.info("print called: CLAIMFORM");
                            job3.print(aset3);
                            log.info("printed .......:");
                            document3.close();
                            job3 = null;
                            job3.cancel();
                        }

                    } catch (Exception e) {
                        log.error("Print Error:", e);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean printDocModified3(String printerName, String fileName, byte[] bytes, String jsonData) {
        try {
            PdfOutputModel reportModel = gson.fromJson(jsonData, PdfOutputModel.class);
            if (reportModel.getParameter().containsKey("barcode") && (!fileName.equalsIgnoreCase("LABEL") || !fileName.equalsIgnoreCase("TRAILER"))) {
                Object val = reportModel.getParameter().get("barcode");
                if (val instanceof String) {
                    Image barcodeImage = Utils.getBarcode((String) val);
                    reportModel.getParameter().put("barcode", barcodeImage);
                }
            }
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            // jasperReportsContext.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
            //jasperReportsContext.setProperty("net.sf.jasperreports.default.font.name", "Sans Serif");
            JasperPrint jprint = (JasperPrint) JasperFillManager
                    .fillReport(new ByteArrayInputStream(bytes), reportModel
                            .getParameter(), new JRBeanCollectionDataSource(reportModel
                                    .getFileldslist()));
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add(new Copies(1));
            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
            printServiceAttributeSet.add(new PrinterName(printerName, null));
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
            configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
            configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
            configuration.setDisplayPageDialog(false);
            configuration.setDisplayPrintDialog(false);

            exporter.setExporterInput(new SimpleExporterInput(jprint));
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } catch (Exception ex) {
            log.error("Error:", ex);

        }
        return false;
    }
}
