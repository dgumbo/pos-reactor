package zw.co.psmi.hms.psp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.bill.entity.Receipt;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.patient.entity.PrintMsgRequestType;
import zw.co.psmi.hms.patient.entity.PrinterConfig;
import zw.co.psmi.hms.patient.entity.PrintoutType;
import zw.co.psmi.hms.patient.service.*;
import zw.co.psmi.hms.psp.entity.Sell;
import zw.co.psmi.hms.psp.service.SellService;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ui.Model;
import zw.co.psmi.hms.basic.BasePdfOutputService;
import zw.co.psmi.hms.basic.OutputType;
import zw.co.psmi.hms.psp.entity.Quotation;
import zw.co.psmi.hms.psp.service.QuotationService;
import zw.co.psmi.hms.basic.PdfOutputDao;
import zw.co.psmi.hms.basic.PdfOutput;
import zw.co.psmi.hms.bill.service.ReceiptService;
import zw.co.psmi.hms.psp.entity.SellItem;
import zw.co.psmi.hms.psp.service.impl.PspPdfPrintOutTypes;

@Controller
@Slf4j
public class PspPdfOutPutController {

    @Autowired
    private SellService sellService;

    @Autowired
    private BillService billService;
    @Autowired
    private PrinterConfigService printerConfigService;
    @Autowired
    private BasePdfOutputService basePdfOutputService;
    @Autowired
    private PdfOutputDao reportDao;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private ReceiptService receiptService;

    @RequestMapping(value = "/psp/pdfoutput/{id}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    byte[] outputReport(@PathVariable Long id, @PathVariable OutputType type) {
        return basePdfOutputService.outputReport(id, type);
    }

    @RequestMapping(value = "/psp/outputpopup", method = RequestMethod.GET)
    public String outputSell(Model model, @RequestParam(value = "sellId", required = false) Long sellId, @RequestParam(value = "billId", required = false) Long billId) {
        model.addAttribute("bill", billService.getById(billId));
        model.addAttribute("receipts", receiptService.getReceiptByBillId(billId));
        model.addAttribute("sell", this.sellService.getById(sellId));
         List<SellItem> sellItems =  this.sellService.getBySellIdActive(sellId);
                if(sellItems != null)
                   {
                   sellItems = sellItems.stream().filter(si->{ return si.getInstructions() != null &&  si.getInstructions().length() > 10; }).collect(Collectors.toList());
                    if(sellItems != null)
                        {
                         model.addAttribute("sellItems", sellItems);   
                        }
                   }                  
        return "psp/outputpopup";
    }

    @RequestMapping(value = "/psp/instantprint", method = RequestMethod.GET)
    public String instantprint(Model model, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "available", required = false) String available) {
        Sell sell = this.sellService.getById(id);
        Bill bill = sell.getBill();
        List<String> printers = Arrays.asList(available.split(","));
        String data = "";
        for (String printer : printers) {
            PrinterConfig printerConfig = printerConfigService.getByPrinterName(printer);
            if (printerConfig != null) {
                if (printerConfig.getPrintOutName() == PrintoutType.CLAIMFORM && bill.getBillCategory() == BillCategory.INSURANCE) {
                    PdfOutput report = reportDao.findByNameAndActiveStatusTrue(OutputType.PSPCLAIMFORM.name());
                    String jsonData = basePdfOutputService.getJsonData(bill.getId(), OutputType.PSPCLAIMFORM);
                    if (report.getJasperData() != null) {
                        data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='PSPCLAIMFORM****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + bill.getId() + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>PSPCLAIMFORM</td></tr>";
                    }
                }
                if (printerConfig.getPrintOutName() == PrintoutType.CLAIMFORM && bill.getBillCategory() != BillCategory.INSURANCE && bill.getPatient() != null) {
                    PdfOutput report = reportDao.findByNameAndActiveStatusTrue(OutputType.PATIENT_INVOICE.name());
                    String jsonData = basePdfOutputService.getJsonData(bill.getId(), OutputType.PATIENT_INVOICE);
                    if (report.getJasperData() != null) {
                        data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='PATIENT_INVOICE****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + bill.getId() + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>PATIENT_INVOICE</td></tr>";
                    }
                }
                if (printerConfig.getPrintOutName() == PrintoutType.LABEL) {
                    PdfOutput report = reportDao.findByNameAndActiveStatusTrue(OutputType.LABEL.name());
                    String jsonData = basePdfOutputService.getJsonData(id, OutputType.LABEL);
                    if (report.getJasperData() != null) {
                        data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='LABEL****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + id + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>ALL LABELS</td></tr>";
                    }
                    report = reportDao.findByNameAndActiveStatusTrue(OutputType.TRAILER.name());
                    jsonData = basePdfOutputService.getJsonData(id, OutputType.TRAILER);
                    if (report.getJasperData() != null) {
                        data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='TRAILER****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + id + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>TRAILER</td></tr>";
                    }
                List<SellItem> sellItems =  this.sellService.getBySellIdActive(id);
                if(sellItems != null)
                   {
                   sellItems = sellItems.stream().filter(si->{ return si.getInstructions() != null &&  si.getInstructions().length() > 10; }).collect(Collectors.toList());
                  if(sellItems != null)
                        {
                        for(SellItem sellItem: sellItems)
                           {
                            report = reportDao.findByNameAndActiveStatusTrue(OutputType.LABEL.name());
                               jsonData = basePdfOutputService.getJsonData(sellItem.getId(), OutputType.LABEL_PAGE);
                               String name = sellItem.getStockType() == null? sellItem.getHmsService().getName():sellItem.getStockType().getName();
                               if (report != null && report.getJasperData() != null) {
                                   data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='LABEL_PAGE****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + sellItem.getId() + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>LABEL - "+name+"</td></tr>";
                               }
                           report = null;  
                           jsonData = "";
                           }
                        }  
                   }
                }
                if (printerConfig.getPrintOutName() == PrintoutType.RECEIPT) {

                    PdfOutput report = reportDao.findByNameAndActiveStatusTrue(OutputType.FINAL_RECEIPT.name());
                    String jsonData = basePdfOutputService.getJsonData(id, OutputType.FINAL_RECEIPT);
                    if (report.getJasperData() != null) {
                        data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='FINAL_RECEIPT****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + id + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>FINAL_RECEIPT</td></tr>";
                    }
                    int count = 0;
                    List<Receipt> receipts = receiptService.getReceiptByBillId(bill.getId());
                    if (receipts.size() > 0) {
                        report = reportDao.findByNameAndActiveStatusTrue(OutputType.RECEIPT.name());
                        for (Receipt receipt : receipts) {
                            if (report.getJasperData() != null) {
                                jsonData = basePdfOutputService.getJsonData(receipt.getId(), OutputType.RECEIPT);
                                data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='RECEIPT****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + id + "****" + new String(Base64.getEncoder().encode(report.getJasperData())) + "****" + jsonData + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>RECEIPT" + count + "</td></tr>";
                            }
                            count++;
                        }
                    }
                }
            }
        }
        if (!data.isEmpty()) {
            model.addAttribute("data", data);
        }
        return "psp/instantprint";
    }

    @RequestMapping(value = "/psp/outputquotation", method = RequestMethod.GET)
    public String outputquotation(Model model, @RequestParam(value = "id", required = false) String id) {
        model.addAttribute("id", id);
        model.addAttribute("type", "QUOTATION");
        return "psp/singleoutputpopup";
    }

    @RequestMapping(value = "/psp/instantprintquotation", method = RequestMethod.GET)
    public String instantprintquotation(Model model, @RequestParam(value = "quotationNumber", required = false) String quotationNumber, @RequestParam(value = "available", required = false) String available) {
        Quotation quotation = quotationService.getByNumber(quotationNumber);
        List<String> printers = Arrays.asList(available.split(","));
        String data = "";
        for (String printer : printers) {
            PrinterConfig printerConfig = printerConfigService.getByPrinterName(printer);
            if (printerConfig.getPrintOutName() == PrintoutType.CLAIMFORM) {
                byte[] dataB = basePdfOutputService.outputReport(quotation.getId(), OutputType.QUOTATION);
                if (dataB != null) {
                    data += "<tr><td><input type='checkbox' checked='checked' name='instantPrint' class='form-control' value='QUOTATION****" + printerConfig.getPrinterName() + "****" + PrintMsgRequestType.PRINTER_JOB + "****" + quotation.getId() + "****" + new String(Base64.getEncoder().encode(dataB)) + "' /></td><td>" + printerConfig.getPrinterName() + "</td><td>QUOTATION</td></tr>";
                }
            }
        }
        if (!data.isEmpty()) {
            model.addAttribute("data", data);
        }
        return "psp/instantprint";
    }
}
