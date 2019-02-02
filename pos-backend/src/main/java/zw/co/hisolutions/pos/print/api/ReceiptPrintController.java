package zw.co.hisolutions.pos.print.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.enums.ContentDisposalType;
import zw.co.hisolutions.pos.print.service.ReceiptPrintService; 
import zw.co.hisolutions.pos.sell.entity.Sell;
import zw.co.hisolutions.pos.sell.services.SellService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/pdf-receipt")
public class ReceiptPrintController {

    private final String filename = "0101-single-receipt.pdf";
    private final ReceiptPrintService receiptPrintService;
    private final SellService sellService;

    public ReceiptPrintController(ReceiptPrintService receiptPrintService, SellService sellService) {
        this.receiptPrintService = receiptPrintService;
        this.sellService = sellService;
    }

    @ResponseBody
    @GetMapping("/view/{filenames:.+}") //using Path Variable
    public ResponseEntity<byte[]> viewFile(@PathVariable String filenames) throws IOException {
//    @GetMapping("/view") //using Path Variable
//    public ResponseEntity<Resource> viewFile( ) { 
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.viewFile()");
        return disposeFileContent(filename, ContentDisposalType.inline);
    }

    @ResponseBody
    @GetMapping("/view") //using Path Variable
    public ResponseEntity<byte[]> viewFile2() throws IOException {
        System.out.println("zw.co.hisolutions.storage.controllers.StorageController.viewFile()");
        return disposeFileContent(filename, ContentDisposalType.inline);
    }

    private ResponseEntity<byte[]> disposeFileContent(String filenames, ContentDisposalType contentDisposalType) throws IOException {
        MediaType mediaType = MediaType.ALL;
        long contentLength = 0;

        List<Sell> sells = sellService.findAll();
        Sell sell = sells.get(sells.size()-1);

        ByteArrayInputStream in = null;

        PDDocument pdfDocument = receiptPrintService.printReceiptPDF(sell);

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
            String mimeType = receiptPrintService.getMimeType(in);
            mediaType = MediaType.valueOf(mimeType);
//            System.err.println("\nmediaType :" + mediaType + "\n\n");
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(contentLength)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposalType + "; filename=\"" + filename + "\"")
                .body(IOUtils.toByteArray(in));
    }

}
