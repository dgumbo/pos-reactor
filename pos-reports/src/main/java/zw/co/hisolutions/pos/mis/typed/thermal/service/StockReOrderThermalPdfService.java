package zw.co.hisolutions.pos.mis.typed.thermal.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;  
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView; 

/**
 *
 * @author dgumbo
 */
public interface StockReOrderThermalPdfService   { 

    PDDocument printReceiptPDF(List<StockReOrderView> stockReOrderViewList) throws IOException;

    String getMimeType(ByteArrayInputStream in) throws IOException; 
}
