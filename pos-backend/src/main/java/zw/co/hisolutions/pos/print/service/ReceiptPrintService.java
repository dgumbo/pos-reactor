package zw.co.hisolutions.pos.print.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument; 
import zw.co.hisolutions.pos.sell.entity.Sell;

/**
 *
 * @author dgumbo
 */
public interface ReceiptPrintService {

    PDDocument printReceiptPDF( Sell sell) throws IOException;

    String getMimeType(ByteArrayInputStream in) throws IOException; 
}
