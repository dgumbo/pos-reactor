package zw.co.hisolutions.pos.mis.typed.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.enums.ContentDisposalType;
import zw.co.hisolutions.pos.mis.typed.entity.MisReportInformation; 
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;
import zw.co.hisolutions.pos.mis.typed.service.StockReOrderReportService;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService; 
import zw.co.hisolutions.pos.mis.typed.thermal.service.StockReOrderThermalPdfService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/reports/stocks/re-order")
public class StockReOrderReportController extends AbstractReportsController { 
    private final StockReOrderReportService stockOutReportService;
    private final StockReOrderThermalPdfService thermalPdfStockReOrderService;

    public StockReOrderReportController(StockReOrderThermalPdfService thermalPdfStockReOrderService, StockReOrderReportService stockOutReportService) {         
        this.thermalPdfStockReOrderService = thermalPdfStockReOrderService;
        this.stockOutReportService = stockOutReportService;
    }

    @ResponseBody
    @GetMapping("/get-pdf") //using Path Variable
    public ResponseEntity<byte[]> viewFile() throws IOException {
//    @GetMapping("/view") //using Path Variable
//    public ResponseEntity<Resource> viewFile( ) { 
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.viewFile()");
        return disposeFileContent(ContentDisposalType.inline);
    }

    @GetMapping("/getReportParams")
    @Override
    public List<TypedParameterMetadata> getReportParams() { 
       return stockOutReportService.getReportParams() ;
    }  

    @GetMapping("/getReportPreview")
    public MisReportInformation<StockReOrderView> getReportPreview(@RequestParam(name="stockItemName" ) String stockItemName) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

                
        return stockOutReportService.getReportPreviewInformation( stockItemName );
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

    private ResponseEntity<byte[]> disposeFileContent(ContentDisposalType contentDisposalType) throws IOException {
        MediaType mediaType = MediaType.ALL;
        long contentLength = 0;

        String filename = "Stock ReOrder.pdf";
        List<StockReOrderView> stockReOrderViewList = stockOutReportService.getReportPreviewInformation(null).getReportData();

        ByteArrayInputStream in = null;

        PDDocument pdfDocument = thermalPdfStockReOrderService.printReceiptPDF(stockReOrderViewList);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            pdfDocument.save(out);

            in = new ByteArrayInputStream(out.toByteArray());
            contentLength = out.size();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pdfDocument.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        try {
            String mimeType = thermalPdfStockReOrderService.getMimeType(in);
            mediaType = MediaType.valueOf(mimeType);
//            System.err.println("\nmediaType :" + mediaType + "\n\n");
        } catch (IOException ex) {
//            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(contentLength)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposalType + "; filename=\"" + filename + "\"")
                .body(IOUtils.toByteArray(in));
    }


    @Override
    public AbstractObjectXSSFTReportService getService() {
        return stockOutReportService;
    }

}
