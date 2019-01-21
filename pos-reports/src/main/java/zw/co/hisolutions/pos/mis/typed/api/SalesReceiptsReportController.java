package zw.co.hisolutions.pos.mis.typed.api;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.mis.typed.entity.MisReportInformation;
import zw.co.hisolutions.pos.mis.typed.entity.SalesReceiptsView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;
import zw.co.hisolutions.pos.mis.typed.service.SalesReceiptsReportService;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/reports/sales/receipts")
public class SalesReceiptsReportController extends AbstractReportsController {

    private final SalesReceiptsReportService salesReceiptsReportService;

    public SalesReceiptsReportController(SalesReceiptsReportService salesReceiptsReportService) {
        this.salesReceiptsReportService = salesReceiptsReportService;
    }

    @GetMapping("/getReportParams")
    @Override
    public List<TypedParameterMetadata> getReportParams() { 
       return salesReceiptsReportService.getReportParams() ;
    }

    @GetMapping("/getWorkbook")
    public SXSSFWorkbook getWorkbook(
            @RequestParam(name="fromDate", required = true ) String fromDateStr, 
            @RequestParam(name="toDate", required = true ) String toDateStr) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(SalesReceiptsReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesReceiptsReportService.getSalesReceiptsWorkbook(fromDate, toDate);
    }
    

    @GetMapping("/getReportPreview")
    public MisReportInformation<SalesReceiptsView> getReportPreview(
            @RequestParam(name="fromDate", required = true ) String fromDateStr, 
            @RequestParam(name="toDate", required = true ) String toDateStr) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(SalesReceiptsReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return salesReceiptsReportService.getReportPreviewInformation(fromDate, toDate);
    }

    @GetMapping("/downloadExcelReport")
    @ResponseBody
    public void downloadExcelReport(HttpServletResponse response,
            @RequestParam(name="fromDate", required = true ) String fromDateStr, 
            @RequestParam(name="toDate", required = true ) String toDateStr) {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        salesReceiptsReportService.downloadExcelReport(fromDate, toDate, response);
    }

    @GetMapping("/disposeFileContent")
    public ResponseEntity<SXSSFWorkbook> disposeFileContent(
            @RequestParam(name="fromDate", required = true ) String fromDateStr, 
            @RequestParam(name="toDate", required = true ) String toDateStr) {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return salesReceiptsReportService.disposeFileContent(fromDate, toDate);
    } 

    @Override
    public AbstractObjectXSSFTReportService getService() {
        return salesReceiptsReportService;
    }

}
