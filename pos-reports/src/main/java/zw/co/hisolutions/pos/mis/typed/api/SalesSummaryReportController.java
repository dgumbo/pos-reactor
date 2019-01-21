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
import zw.co.hisolutions.pos.mis.typed.entity.SalesSummaryView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;
import zw.co.hisolutions.pos.mis.typed.service.SalesSummaryReportService;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/reports/sales/summary")
public class SalesSummaryReportController extends AbstractReportsController {

    private final SalesSummaryReportService salesSummaryReportService;

    public SalesSummaryReportController(SalesSummaryReportService salesReceiptsReportService) {
        this.salesSummaryReportService = salesReceiptsReportService;
    }

    @GetMapping("/getReportParams")
    @Override
    public List<TypedParameterMetadata> getReportParams() {
        return salesSummaryReportService.getReportParams();
    }

    @GetMapping("/getWorkbook")
    public SXSSFWorkbook getWorkbook() {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse("2018-12-25");
            toDate = dateTimeFormat.parse("2019-01-31");
        } catch (ParseException ex) {
            Logger.getLogger(SalesSummaryReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesSummaryReportService.getSalesReceiptsWorkbook(fromDate, toDate);
    }

    @GetMapping("/getReportPreview")
    public MisReportInformation<SalesSummaryView> getReportPreview(
            @RequestParam(name = "fromDate", required = true) String fromDateStr,
            @RequestParam(name = "toDate", required = true) String toDateStr) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(SalesSummaryReportController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return salesSummaryReportService.getReportPreviewInformation(fromDate, toDate);
    }

    @GetMapping("/downloadExcelReport")
    @ResponseBody
    public void downloadExcelReport(HttpServletResponse response,
            @RequestParam(name = "fromDate", required = true) String fromDateStr,
            @RequestParam(name = "toDate", required = true) String toDateStr) {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        salesSummaryReportService.downloadExcelReport(fromDate, toDate, response);
    }

    @GetMapping("/disposeFileContent")
    public ResponseEntity<SXSSFWorkbook> disposeFileContent(
            @RequestParam(name = "fromDate", required = true) String fromDateStr,
            @RequestParam(name = "toDate", required = true) String toDateStr) {
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = dateTimeFormat.parse(fromDateStr);
            toDate = dateTimeFormat.parse(toDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return salesSummaryReportService.disposeFileContent(fromDate, toDate);
    }

    @Override
    public AbstractObjectXSSFTReportService getService() {
        return salesSummaryReportService;
    }

}
