package zw.co.hisolutions.pos.mis.typed.thermal.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView;
import zw.co.hisolutions.pos.pdfprint.service.PDFPrinterService;
import zw.co.hisolutions.pos.mis.typed.thermal.service.StockReOrderThermalPdfService;

/**
 *
 * @author dgumbo
 */
@Service
public class StockReOrderThermalPdfServiceImpl implements StockReOrderThermalPdfService {

    private final SimpleDateFormat SDTF = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
    private final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat STF = new SimpleDateFormat("HH:mm");

    private final Tika TIKA = new Tika();

    private final float POINTS_PER_INCH = 72;
    private final Float MM_PER_INCH = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    private final int LINES_HEIGHT = 10;
    private final int DOCUMENT_WIDTH = MM_PER_INCH.intValue() * 110;
    private final int DOCUMENT_HEIGHT = MM_PER_INCH.intValue() * 300;

    private final int CONTENT_START_X = 8;
    private final int DOCUMENT_MARGIN = CONTENT_START_X;
    private final int DOCUMENT_START_X = CONTENT_START_X;

    private final int CURRENT_STOCK_COLUMN_RATIO = 2;
    private final int REQUIRED_QUANTITY_COLUMN_RATIO = 1;
    private final int UNIT_COST_COLUMN_RATIO = 1;
    private final int LINE_TOTAL_COST_COLUMN_RATIO = 1;
    private final int GLOBAL_RATIO = CURRENT_STOCK_COLUMN_RATIO + REQUIRED_QUANTITY_COLUMN_RATIO + UNIT_COST_COLUMN_RATIO + LINE_TOTAL_COST_COLUMN_RATIO;// + CONTENT_START_X + CONTENT_START_X;

    private final int CURRENT_STOCK_POS = DOCUMENT_MARGIN + ((DOCUMENT_WIDTH - DOCUMENT_MARGIN * 2) * CURRENT_STOCK_COLUMN_RATIO / GLOBAL_RATIO);
    private final int REQUIRED_QUANTITY_POS = CURRENT_STOCK_POS + ((DOCUMENT_WIDTH - DOCUMENT_MARGIN * 2) * REQUIRED_QUANTITY_COLUMN_RATIO / GLOBAL_RATIO);
    private final int UNIT_COST_POS = REQUIRED_QUANTITY_POS + ((DOCUMENT_WIDTH - DOCUMENT_MARGIN * 2) * UNIT_COST_COLUMN_RATIO / GLOBAL_RATIO);
    private final int LINE_TOTAL_COST_POS = UNIT_COST_POS + ((DOCUMENT_WIDTH - DOCUMENT_MARGIN * 2) * LINE_TOTAL_COST_COLUMN_RATIO / GLOBAL_RATIO);

    private final int GLOBAL_FONT_SIZE = 10;

    private PDFont MAIN_FONT;
    private PDFont MAIN_FONT_BOLD;
    private PDFont MAIN_FONT_ITALIC;
    private PDFont MAIN_FONT_BOLD_ITALIC;

    public StockReOrderThermalPdfServiceImpl() {
    }

    @Override
    public PDDocument printReceiptPDF(List<StockReOrderView> stockReOrderViewList) throws IOException {
        PDDocument pdfDocument = new PDDocument();

        this.MAIN_FONT = PDType1Font.HELVETICA;
        this.MAIN_FONT_BOLD = PDType1Font.HELVETICA_BOLD;
        this.MAIN_FONT_ITALIC = PDType1Font.HELVETICA_OBLIQUE;
        this.MAIN_FONT_BOLD_ITALIC = PDType1Font.HELVETICA_BOLD_OBLIQUE;

//        this.MAIN_FONT = PDType0Font.load(pdfDocument, receiptFontLoaderService.getMainFontFile());
//        this.MAIN_FONT_BOLD = PDType0Font.load(pdfDocument, receiptFontLoaderService.getMainFontBoldFile());
//        this.MAIN_FONT_ITALIC = PDType0Font.load(pdfDocument, receiptFontLoaderService.getMainFontItalicFile());
//        this.MAIN_FONT_BOLD_ITALIC = PDType0Font.load(pdfDocument, receiptFontLoaderService.getMainFontBoldItalicFile());
        /**
         * A rectangle the size of U.S. Letter, 8.5" x 11".
         */
        int linesHeight = (LINES_HEIGHT + 4) * stockReOrderViewList.size();

        PDRectangle pdfPageSize = new PDRectangle(DOCUMENT_WIDTH, DOCUMENT_HEIGHT + linesHeight);
        PDPage pdfPage = new PDPage(pdfPageSize);
        pdfDocument.addPage(pdfPage);
        PDPageContentStream contents = new PDPageContentStream(pdfDocument, pdfPage);

        int nextStartY = DOCUMENT_HEIGHT + linesHeight;
        nextStartY -= LINES_HEIGHT * 2;

        nextStartY = printLetterHead(contents, nextStartY);

        nextStartY = printServicesListHeaderRow(contents, nextStartY);
        nextStartY -= LINES_HEIGHT;
        for (StockReOrderView srov : stockReOrderViewList) {
            nextStartY = printReceiptRow(contents, nextStartY, srov);
        }
        nextStartY -= LINES_HEIGHT;
//        addLineBreak(contents, nextStartY);

        printServicesListSummary(contents, stockReOrderViewList, nextStartY);

        contents.close();

        return pdfDocument;
    }

    private int printLetterHead(PDPageContentStream contents, int headerStartY) throws IOException {
        headerStartY -= 10;
        final int letterHeadRowHeight = 10;

        PDFPrinterService headerPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE);

        int cNameStartX = (DOCUMENT_WIDTH - headerPrinter.widthOfText("COMPANY NAME")) * 45 / 100;
        headerPrinter.putText(cNameStartX, headerStartY, "COMPANY NAME");
        headerStartY -= letterHeadRowHeight;

        int cAddressStartX = (DOCUMENT_WIDTH - headerPrinter.widthOfText("COMPANY ADDRESS")) * 45 / 100;
        headerPrinter.putText(cAddressStartX, headerStartY, "COMPANY ADDRESS");
        headerStartY -= letterHeadRowHeight;

        int sNameStartX = (DOCUMENT_WIDTH - headerPrinter.widthOfText("STORE NAME")) * 45 / 100;
        headerPrinter.putText(sNameStartX, headerStartY, "STORE NAME");
        headerStartY -= letterHeadRowHeight;

        int sAddressStartX = (DOCUMENT_WIDTH - headerPrinter.widthOfText("STORE ADDRESS")) * 45 / 100;
        headerPrinter.putText(sAddressStartX, headerStartY, "STORE ADDRESS");
        headerStartY -= letterHeadRowHeight;

        return headerStartY;
    }

    private int printServicesListHeaderRow(PDPageContentStream contents, int nextStartY) throws IOException {
        nextStartY -= 18;

        PDFPrinterService headerPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE);

        headerPrinter.putTextToTheRight(CURRENT_STOCK_POS - 2, nextStartY, "Current Stock");
        headerPrinter.putTextToTheRight(REQUIRED_QUANTITY_POS - 2, nextStartY, "Req");
        headerPrinter.putTextToTheRight(UNIT_COST_POS, nextStartY, "Unit");
        headerPrinter.putTextToTheRight(LINE_TOTAL_COST_POS, nextStartY, "Total");

        nextStartY -= 9;

        addLineBreak(contents, nextStartY);

        return nextStartY;
    }

    private void addLineBreak(PDPageContentStream contents, int lineBreakStartY) throws IOException {

        PDFPrinterService lineBreakPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE);

        int width = DOCUMENT_WIDTH - DOCUMENT_START_X * 2;
        int brokenWitdh = 0;
        String broken = "-";
        while (brokenWitdh <= width) {
            broken += "-";
            brokenWitdh = lineBreakPrinter.widthOfText(broken);
        }

        int brokenStart = (DOCUMENT_WIDTH - lineBreakPrinter.widthOfText(broken)) * 45 / 100;
        lineBreakPrinter.putText(brokenStart, lineBreakStartY, broken);
    }

    private int printReceiptRow(PDPageContentStream contents, int startY, StockReOrderView srov) throws IOException {
        int topMargin = 4;
        int rowHeight = LINES_HEIGHT;

        int descriptionTdWidth = DOCUMENT_WIDTH - DOCUMENT_MARGIN * 2;

        PDFPrinterService textPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE);

        List<String> lines = new ArrayList();

        List<String> breaks = Arrays.asList(srov.getStockItem().split("\\n"));
        for (String br : breaks) {
            StringBuilder sb = new StringBuilder();
            for (String s : br.split(" ")) {
                s = s.toUpperCase();
                if (textPrinter.widthOfText(sb.toString() + s + " ") > descriptionTdWidth) {
                    lines.add(sb.toString());
                    sb = new StringBuilder();
                    sb.append("  ");
                }
                sb.append(s).append(" ");
            }

            if (sb.length() >= 1) {
                lines.add(sb.toString());
            }
        }

        startY -= rowHeight * 2 / 5;
        for (String line : lines) {
            textPrinter.putText(CONTENT_START_X, startY, line);
            startY -= rowHeight;
        }

        BigDecimal unitCost = srov.getUnitCost().setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCost = srov.getTotalCost().setScale(2, RoundingMode.HALF_UP);

        textPrinter.putTextToTheRight(CURRENT_STOCK_POS - 2, startY, ((Long) srov.getCurrentStock()).toString());
        textPrinter.putTextToTheRight(REQUIRED_QUANTITY_POS - 2, startY, ((Long) srov.getRequiredQuantity()).toString());
        textPrinter.putTextToTheRight(UNIT_COST_POS, startY, "$" + unitCost.toString());
        textPrinter.putTextToTheRight(LINE_TOTAL_COST_POS, startY, "$" + totalCost.toString());

        startY -= 1;
        startY -= rowHeight;
        startY -= rowHeight * 1 / 5;
        return startY;
    }

    private int printServicesListSummary(PDPageContentStream contents, List< StockReOrderView> srovList, int summaryStartY) throws IOException {
        int summaryRowHeight = 10;

        addLineBreak(contents, summaryStartY);
        summaryStartY -= summaryRowHeight * 3 / 2;

        int startX = CONTENT_START_X;
        int endX = DOCUMENT_WIDTH - CONTENT_START_X;

        BigDecimal sellAmount = BigDecimal.ZERO;
        for (StockReOrderView srov : srovList) {
            float newTotal = sellAmount.floatValue() + srov.getTotalCost().floatValue();
            sellAmount = new BigDecimal(newTotal).setScale(2, RoundingMode.HALF_UP);
        }

        PDFPrinterService summaryTotalPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE);
        PDFPrinterService summaryPrinter = new PDFPrinterService(contents, MAIN_FONT, GLOBAL_FONT_SIZE - 1);

        // /* Print Total Lines */ 
        summaryPrinter.putText(startX, summaryStartY, "LINES");
        summaryPrinter.putTextToTheRight(endX, summaryStartY, ((Integer) srovList.size()).toString());
        summaryStartY -= summaryRowHeight;
        // /* End Print Total Lines */

        // /* Print Items Count */ 
        summaryTotalPrinter.putText(startX, summaryStartY, "TOTAL");
        summaryTotalPrinter.putTextToTheRight(endX, summaryStartY, "$ " + sellAmount.toString());
        summaryStartY -= summaryRowHeight;
        // /* End Print Items Count */
//
//        // /* Print VAT */
//        summaryPrinter.putText(startX, summaryStartY  , "CHANGE");
//        summaryPrinter.putTextToTheRight(endX, summaryStartY  , "$ " + changeAmount.toString());
//        summaryStartY -= summaryRowHeight;
//        // /* End Print VAT */ 
//
//        // /* Print Total */
//        summaryPrinter.putText(startX, summaryStartY  , "ITEM#");
//        summaryPrinter.putTextToTheRight(endX, summaryStartY , ((Integer) receipt.getReceiptLines().size()).toString());
//        // /* End Print Total */

        addLineBreak(contents, summaryStartY);
        summaryStartY -= summaryRowHeight * 2;
        summaryTotalPrinter.putText(startX, summaryStartY, " ");

        return summaryStartY;
    }

    @Override
    public String getMimeType(ByteArrayInputStream in) throws IOException {
        return TIKA.detect(in);
    }

}
