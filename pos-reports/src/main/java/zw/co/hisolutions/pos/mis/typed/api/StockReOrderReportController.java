package zw.co.hisolutions.pos.mis.typed.api;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.mis.typed.entity.MisReportInformation; 
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;
import zw.co.hisolutions.pos.mis.typed.service.StockReOrderReportService;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/reports/stocks/re-order")
public class StockReOrderReportController extends AbstractReportsController {

    private final StockReOrderReportService stockOutReportService;

    public StockReOrderReportController(StockReOrderReportService salesReceiptsReportService) {
        this.stockOutReportService = salesReceiptsReportService;
    }

    @GetMapping("/getReportParams")
    @Override
    public List<TypedParameterMetadata> getReportParams() { 
       return stockOutReportService.getReportParams() ;
    }  

    @GetMapping("/getReportPreview")
    public MisReportInformation<StockReOrderView> getReportPreview(@RequestParam(name="stockItemName" ) String stockItemName) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

                
        return stockOutReportService.getReportPreviewInformation( stockItemName);
    }

    @GetMapping("/downloadExcelReport")
    @ResponseBody
    public void downloadExcelReport(HttpServletResponse response, @RequestParam(name="stockItemName" ) String stockItemName) {
       

        stockOutReportService.downloadExcelReport( stockItemName,response);
    }

    @GetMapping("/disposeFileContent")
    public ResponseEntity<SXSSFWorkbook> disposeFileContent(@RequestParam(name="stockItemName" ) String stockItemName) {

        return stockOutReportService.disposeFileContent( stockItemName);
    } 

    @Override
    public AbstractObjectXSSFTReportService getService() {
        return stockOutReportService;
    }

}
