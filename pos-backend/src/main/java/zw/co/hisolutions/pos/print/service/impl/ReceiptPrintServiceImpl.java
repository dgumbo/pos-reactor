package zw.co.hisolutions.pos.print.service.impl;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import zw.co.hisolutions.pos.pdfprint.service.PDFPrinterService;
import zw.co.hisolutions.pos.print.service.ReceiptPrintService;
import zw.co.hisolutions.pos.sell.entity.Sell;
import zw.co.hisolutions.pos.sell.entity.SellItem;

/**
 *
 * @author dgumbo
 */
@Service
public class ReceiptPrintServiceImpl implements ReceiptPrintService {

    private final Tika TIKA = new Tika();

    private final float POINTS_PER_INCH = 72;
    private final Float MM_PER_INCH = 1 / (10 * 2.54f) * POINTS_PER_INCH;

    private final int DOCUMENT_WIDTH = MM_PER_INCH.intValue() * 112;
    private final int DOCUMENT_HEIGHT = MM_PER_INCH.intValue() * 180;

    private final int CONTENT_START_X = 4;

    private final int PRODUCT_COLUMN_RATIO = 25;
    private final int QUANTITY_COLUMN_RATIO = 3;
    private final int PRICE_COLUMN_RATIO = 7;
    private final int TOTAL_COLUMN_RATIO = 7;
    private final int GLOBAL_RATIO = PRODUCT_COLUMN_RATIO + QUANTITY_COLUMN_RATIO + PRICE_COLUMN_RATIO + TOTAL_COLUMN_RATIO;// + CONTENT_START_X + CONTENT_START_X;

    @Override
    public PDDocument printReceiptPDF(Sell sell) throws IOException {
        PDDocument pdfDocument = new PDDocument();

        /**
         * A rectangle the size of U.S. Letter, 8.5" x 11".
         */
        PDRectangle pdfPageSize = new PDRectangle(DOCUMENT_WIDTH, DOCUMENT_HEIGHT);
        PDPage pdfPage = new PDPage(pdfPageSize);
        pdfDocument.addPage(pdfPage);
        PDPageContentStream contents = new PDPageContentStream(pdfDocument, pdfPage);

        int nextStartY = DOCUMENT_HEIGHT - 20;

        nextStartY = printLetterHead(contents, nextStartY);

        nextStartY = printHeader(contents, sell, nextStartY);

        int numPrintedRows = 0;
        nextStartY = printServicesListHeaderRow(contents, nextStartY);

        for (SellItem sellItem : sell.getSellItems()) {
            numPrintedRows++;
            boolean odd = numPrintedRows % 2 == 0;

            nextStartY = printReceiptRow(contents, nextStartY, sellItem, odd);
        }

        /*
        First page with summation 16 rows
        First page without summation 24 rows
        Next page with summation 23 rows
        Next page without summation 31 rows
        16-24 = 12
        24-31 = 18
         */
        nextStartY = printServicesListSummary(contents, sell, nextStartY);

        printFooter(contents);
        contents.close();

        return pdfDocument;
    }

    private int printLetterHead(PDPageContentStream contents, int headerStartY) throws IOException {
        int headerStartX = 10;
        final int letterHeadRowHeight = 10;

        PDFont headerFont = PDType1Font.HELVETICA_BOLD;
        PDFPrinterService headerPrinter = new PDFPrinterService(contents, headerFont, 12);
        headerPrinter.putText(headerStartX, headerStartY, "Katanga Groceries");
        headerStartY -= letterHeadRowHeight;

        PDFont font = PDType1Font.HELVETICA;
        PDFPrinterService textPrinter = new PDFPrinterService(contents, font, 10);

        textPrinter.putText(headerStartX, headerStartY, "Katanga");
        headerStartY -= letterHeadRowHeight;

        textPrinter.putText(headerStartX, headerStartY, "+263 773 632 856");

        return headerStartY;
    }

    private int printHeader(PDPageContentStream contents, Sell sell, int headerStartY) throws IOException {
        headerStartY -= 20;
        int col1EndX = DOCUMENT_WIDTH * 1 / 3;

        final int letterHeadRowHeight = 10;

        PDFont font = PDType1Font.HELVETICA;
        PDFPrinterService textPrinter = new PDFPrinterService(contents, font, 10);

        textPrinter.putTextToTheRight(col1EndX, headerStartY, "Date : ");
        textPrinter.putText(col1EndX, headerStartY, sell.getSellDate().toString());
        headerStartY -= letterHeadRowHeight;

        textPrinter.putTextToTheRight(col1EndX, headerStartY, "Time : ");
        textPrinter.putText(col1EndX, headerStartY, sell.getSellDate().toString());
        headerStartY -= letterHeadRowHeight;

        textPrinter.putTextToTheRight(col1EndX, headerStartY, "Receipt # : ");
        textPrinter.putText(col1EndX, headerStartY, ((Long) sell.getReceipt().getId()).toString());

        return headerStartY;
    }

    private int printServicesListHeaderRow(PDPageContentStream contents, int nextStartY) throws IOException {
        int prodPos = DOCUMENT_WIDTH * PRODUCT_COLUMN_RATIO / GLOBAL_RATIO;
        int quantityPos = prodPos + (DOCUMENT_WIDTH * QUANTITY_COLUMN_RATIO / GLOBAL_RATIO);
        int pricePos = quantityPos + (DOCUMENT_WIDTH * PRICE_COLUMN_RATIO / GLOBAL_RATIO);
        int totalPos = pricePos + (DOCUMENT_WIDTH * TOTAL_COLUMN_RATIO / GLOBAL_RATIO);

        int headerRowHeight = 25;
        nextStartY -= 40;

        contents.addRect(CONTENT_START_X, nextStartY, DOCUMENT_WIDTH - CONTENT_START_X * 2, headerRowHeight);

        Color fillColor = new Color(240, 240, 240);
        Color strokeColor = new Color(238, 238, 238);
        contents.setStrokingColor(strokeColor);
        contents.setNonStrokingColor(fillColor);
        contents.fillAndStroke();

//        contents.moveTo(prodPos, nextStartY + headerRowHeight);
//        contents.lineTo(prodPos, nextStartY);
//        contents.moveTo(quantityPos, nextStartY + headerRowHeight);
//        contents.lineTo(quantityPos, nextStartY);
//        contents.moveTo(pricePos, nextStartY + headerRowHeight);
//        contents.lineTo(pricePos, nextStartY);
//
//        strokeColor = new Color(220, 220, 220);
//        contents.setStrokingColor(strokeColor);
//        contents.stroke();

        PDFont font = PDType1Font.HELVETICA;
        PDFPrinterService headerPrinter = new PDFPrinterService(contents, font, 11);
        headerPrinter.putText(CONTENT_START_X + 3, nextStartY + headerRowHeight * 2 / 6, "Item");
        headerPrinter.putText(prodPos + 5, nextStartY + headerRowHeight * 2 / 6, "#");
        headerPrinter.putTextToTheRight(pricePos - 1, nextStartY + headerRowHeight * 2 / 6, "Price");
        headerPrinter.putTextToTheRight(DOCUMENT_WIDTH - CONTENT_START_X * 2 - 2, nextStartY + headerRowHeight * 2 / 6, "Total");

        return nextStartY;
    }

    private int printReceiptRow(PDPageContentStream contents, int startY, SellItem sellItem, boolean odd) throws IOException {
        int topMargin = 4;
        int rowHeight = 14;

        int prodPos = DOCUMENT_WIDTH * PRODUCT_COLUMN_RATIO / GLOBAL_RATIO;
        int quantityPos = prodPos + (DOCUMENT_WIDTH * QUANTITY_COLUMN_RATIO / GLOBAL_RATIO);
        int pricePos = quantityPos + (DOCUMENT_WIDTH * PRICE_COLUMN_RATIO / GLOBAL_RATIO);
        int totalPos = pricePos + (DOCUMENT_WIDTH * TOTAL_COLUMN_RATIO / GLOBAL_RATIO);

        int descriptionTdWidth = prodPos;

        PDFont font = PDType1Font.HELVETICA;
        PDFPrinterService textPrinter = new PDFPrinterService(contents, font, 10);

        List<String> lines = new ArrayList();

        List<String> breaks = Arrays.asList(sellItem.getProduct().getName().split("\\n"));
        for (String br : breaks) {
            StringBuilder sb = new StringBuilder();
            for (String s : br.split(" ")) {
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

        printRowBackGround(contents, startY, rowHeight * lines.size() + topMargin, odd);

        startY -= topMargin;
        int yPos = startY - rowHeight + rowHeight * 2 / 5;
        for (String line : lines) {
            textPrinter.putText(CONTENT_START_X + 4, yPos, line);
            yPos -= rowHeight;
        }
        startY -= rowHeight * lines.size();

        textPrinter.putTextToTheRight(quantityPos - 2, startY + rowHeight * lines.size() * 2 / 5, ((Integer) sellItem.getQuantity()).toString());
        textPrinter.putTextToTheRight(pricePos - 2, startY + rowHeight * lines.size() * 2 / 5, sellItem.getUnitCost().toString());
        textPrinter.putTextToTheRight(DOCUMENT_WIDTH - CONTENT_START_X * 2 - 2, startY + rowHeight * lines.size() * 2 / 5, sellItem.getTotalCost().toString());

        return startY;
    }

    private void printRowBackGround(PDPageContentStream contents, int rowY, int height, boolean odd) throws IOException {
        int prodPos = DOCUMENT_WIDTH * PRODUCT_COLUMN_RATIO / GLOBAL_RATIO;
        int quantityPos = prodPos + (DOCUMENT_WIDTH * QUANTITY_COLUMN_RATIO / GLOBAL_RATIO);
        int pricePos = quantityPos + (DOCUMENT_WIDTH * PRICE_COLUMN_RATIO / GLOBAL_RATIO);
        int totalPos = pricePos + (DOCUMENT_WIDTH * TOTAL_COLUMN_RATIO / GLOBAL_RATIO);

        Color fillColor = new Color(250, 250, 250);
        Color strokeColor = new Color(238, 238, 238);
        
        contents.setStrokingColor(strokeColor);
        contents.setNonStrokingColor(fillColor);

        contents.addRect(CONTENT_START_X, rowY - height, DOCUMENT_WIDTH - CONTENT_START_X * 2, height);

        if (odd) {
            contents.fillAndStroke();
        } else {
            contents.stroke();
        }

//        contents.moveTo(prodPos, rowY - height);
//        contents.lineTo(prodPos, rowY);
//        contents.moveTo(quantityPos, rowY - height);
//        contents.lineTo(quantityPos, rowY);
//        contents.moveTo(pricePos, rowY - height);
//        contents.lineTo(pricePos, rowY);

//        strokeColor = new Color(220, 220, 220);
//        contents.setStrokingColor(strokeColor);
//        contents.stroke();
    }

    private int printServicesListSummary(PDPageContentStream contents, Sell sell, int summaryStartY) throws IOException {
        int summaryRowHeight = 18;
        summaryStartY -= summaryRowHeight;

        int firstRectEndX = DOCUMENT_WIDTH * 2 / 3;
        int secondRectEndX = DOCUMENT_WIDTH - CONTENT_START_X * 2;

        BigDecimal sellAmount = sell.getSellAmount();
        BigDecimal changeAmount = sell.getReceipt().getChangeAmount();
        BigDecimal paidAmount = sell.getReceipt().getPaidAmount();

        sellAmount = sellAmount.setScale(2, RoundingMode.HALF_EVEN);
        changeAmount = changeAmount.setScale(2, RoundingMode.HALF_EVEN);
        paidAmount = paidAmount.setScale(2, RoundingMode.HALF_EVEN);

        Color strokeColor = new Color(100, 100, 100);
        contents.setStrokingColor(strokeColor);

        Color fillColor = new Color(240, 240, 240);
        contents.setNonStrokingColor(fillColor);

        PDFPrinterService summaryPrinter = new PDFPrinterService(contents, PDType1Font.HELVETICA, 11);

        // /* Print Subtotal */ 
        contents.moveTo(firstRectEndX, summaryStartY);
        contents.lineTo(secondRectEndX, summaryStartY);
        contents.stroke();

        summaryPrinter.putTextToTheRight(firstRectEndX, summaryStartY + summaryRowHeight * 1 / 4, "Sub Total : ");
        summaryPrinter.putTextToTheRight(DOCUMENT_WIDTH - CONTENT_START_X * 2 - 4, summaryStartY + summaryRowHeight * 1 / 4, "$ " + sellAmount.toString());

        summaryStartY -= summaryRowHeight;
        // /* End Print Subtotal */

        // /* Print VAT */
//        contents.addRect(firstRectEndX, summaryStartY, secondRectEndX - 5, summaryRowHeight);
        contents.moveTo(firstRectEndX, summaryStartY);
        contents.lineTo(secondRectEndX, summaryStartY);
        contents.stroke();

        summaryPrinter.putTextToTheRight(firstRectEndX, summaryStartY + summaryRowHeight * 1 / 4, "Vat : ");
        summaryPrinter.putTextToTheRight(DOCUMENT_WIDTH - CONTENT_START_X * 2 - 4, summaryStartY + summaryRowHeight * 1 / 4, "$ " + changeAmount.toString());

        summaryStartY -= summaryRowHeight;
        // /* End Print VAT */ 

        // /* Print Total */
//        contents.addRect(firstRectEndX, summaryStartY, secondRectEndX - 5, summaryRowHeight);
        contents.moveTo(firstRectEndX, summaryStartY);
        contents.lineTo(secondRectEndX, summaryStartY);
        contents.stroke();

        summaryPrinter.putTextToTheRight(firstRectEndX, summaryStartY + summaryRowHeight * 1 / 4, "Total : ");
        summaryPrinter.putTextToTheRight(DOCUMENT_WIDTH - CONTENT_START_X * 2 - 4, summaryStartY + summaryRowHeight * 1 / 4, "$ " + paidAmount.toString());
        // /* End Print Total */

        return summaryStartY;
    }

    private int printFooter(PDPageContentStream contents) throws IOException {
        int bottomMargin = 10;
        int leftMargin = 10;

        PDFPrinterService footerValuePrinter = new PDFPrinterService(contents, PDType1Font.HELVETICA_OBLIQUE, 8);

        String line = "* Thank you for shopping with us.";

        footerValuePrinter.putText(leftMargin, bottomMargin, line);

        return bottomMargin;
    }

    public List<SellItem> getRows(Sell sell) {
        return sell.getSellItems();
    }

    @Override
    public String getMimeType(ByteArrayInputStream in) throws IOException {
        return TIKA.detect(in);
    }

}
