package zw.co.hisolutions.pos.reports.helper.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import zw.co.hisolutions.pos.reports.entity.ReportData;

/**
 *
 * @author dgumbo
 */
@Getter
public class WorkbookHelper<E> {

    protected SXSSFWorkbook sxssfWorkbook;
    private final XSSFWorkbook workbook;
    private Map<String, CellStyle> styles;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dtf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
    private XSSFCellStyle dateStyle;
    private XSSFCellStyle dateTimeStyle;
    private XSSFCellStyle decimalStyle;
    private XSSFCellStyle integerSstyle;
    private final boolean[] showTotal;
    private final double[] total;
    Integer[] reportWidths;
    protected int subTotalsRow;

    public WorkbookHelper(int reportColumnCount) {
        workbook = new XSSFWorkbook();
        createStyless();
        defineStyles();
        this.showTotal = new boolean[reportColumnCount];
        this.total = new double[reportColumnCount];
        for (int cc = 0; cc < reportColumnCount; cc++) {
            showTotal[cc] = false;
            total[cc] = 0;
            cc++;
        }
        this.reportWidths = new Integer[reportColumnCount];
    }

    public XSSFSheet createSheet(String name) {
        XSSFSheet sheet = workbook.createSheet(name);
//        sheet.protectSheet("Password1");
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        return sheet;
    }

    /* Populate The Workbook With Data*/
    public void populateMainWorksheet(ReportData reportResult, String sheetName, XSSFRow headerRow) throws NumberFormatException {
        convertToSXSSFWorkbook();
        SXSSFSheet sheet = sxssfWorkbook.getSheet(sheetName);
        int currentRow = headerRow.getRowNum() + 1;
        int reportRowCount = reportResult.getData().length;
        int reportColumnCount = reportResult.getColumnNames().length;

        for (int i = 0; i < reportRowCount; i++) {
            Object[] colData = reportResult.getData()[i];
            Row row = sheet.createRow(currentRow);
            //Cell cell;
            for (int k = 0; k < reportColumnCount; k++) {
                Object data;
                Cell cell = row.createCell(k);
                if (colData[k] != null) {
                    data = colData[k];
                    //System.out.println("colData[k] : " + colData[k] + ", colData[k] instanceof : " + colData[k].getClass());

                    if (reportWidths[k] < data.toString().length() + 3) {
                        reportWidths[k] = data.toString().length() + 3;
                    }

                    if (colData[k] instanceof Date) {
                        String colDataStrValue = colData[k].toString();
                        try {
                            // System.out.println("colData[k].toString() : " + colDataStrValue);
                            String formatedDateStr = convertStringToFormattedDateString(colDataStrValue);
                            cell.setCellValue(formatedDateStr);

                        } catch (Exception e) {
                        }
                        cell.setCellStyle(dateStyle);
                    } else if (colData[k] instanceof Double || colData[k] instanceof BigDecimal) {
                        double val = Double.parseDouble(colData[k].toString());
                        cell.setCellValue(val);
                        total[k] += val;
                        showTotal[k] = true;
                        cell.setCellStyle(decimalStyle);
                    } else if (colData[k] instanceof Integer || colData[k] instanceof BigInteger || colData[k] instanceof Long) {
                        long val = Long.parseLong(colData[k].toString());
                        cell.setCellValue(val);
                        total[k] += val;
                        showTotal[k] = true;
                        cell.setCellStyle(integerSstyle);
                    } else {
                        cell.setCellValue(colData[k].toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            }
            currentRow++;
        }
        this.subTotalsRow = currentRow;

        int k = 0;
        for (int reportWidth : reportWidths) {
            int maxWidth = 45 * 256; // 40 Characters to Display as max + 5 for padding
            int newWidth = (reportWidth + 5) * 256 > maxWidth ? maxWidth : (reportWidth + 5) * 256;
            //System.out.println("\nmaxWidth : " + maxWidth);
            //System.out.println("newWidth : " + newWidth);

            //sheet.getColumnStyle(k).setWrapText(true); 
            sheet.setColumnWidth(k, newWidth); //30 characters wide

            k++;
        }
    }

    public void populateMainWorksheet(List<E> reportData, Field[] dataFields, String sheetName, XSSFRow headerRow) {
        convertToSXSSFWorkbook();
        SXSSFSheet sheet = sxssfWorkbook.getSheet(sheetName);
        int currentRow = headerRow.getRowNum() + 1;

        for (E colData : reportData) {
            int f = 0;
            Row row = sheet.createRow(currentRow);
            //Cell cell;
            int k = 0;
            for (Field field : dataFields) {
                Object data = null;
                //Turn off access check for this field
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(colData);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }

                // String fieldName = field.getName() ;
                Cell cell = row.createCell(k);
                colData.getClass();
                if (fieldValue != null) {
                    data = fieldValue;
                    //System.out.println("fieldValue : " + fieldValue + ", fieldValue instanceof : " + fieldValue.getClass());

                    if (reportWidths[k] < data.toString().length() + 3) {
                        reportWidths[k] = data.toString().length() + 3;
                    }

                    if (fieldValue instanceof Date) {
                        //System.out.println("\nfieldValue.toString(): " + fieldValue.toString());
                        try {
                            SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(sdtf.parse(fieldValue.toString()));
                            cell.setCellStyle(dateTimeStyle);
                        } catch (ParseException e) {
                            //System.out.println("\nerror : " + e.getMessage());
                            try {
                                cell.setCellValue(df.parse(fieldValue.toString()));
                                cell.setCellStyle(dateStyle);
                            } catch (ParseException es) {
                                cell.setCellValue(fieldValue.toString());
                            }
                        }
                    } else if (fieldValue instanceof Double || fieldValue instanceof BigDecimal) {
                        double val = Double.parseDouble(fieldValue.toString());
                        cell.setCellValue(val);
                        total[k] += val;
                        showTotal[k] = true;
                        cell.setCellStyle(decimalStyle);
                    } else if (fieldValue instanceof Integer || fieldValue instanceof BigInteger || fieldValue instanceof Long) {
                        long val = Long.parseLong(fieldValue.toString());
                        cell.setCellValue(val);
                        total[k] += val;
                        showTotal[k] = true;
                        cell.setCellStyle(integerSstyle);
                    } else {
                        cell.setCellValue(fieldValue.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
                k++;
            }
            currentRow++;
        }

        this.subTotalsRow = currentRow;

        int k = 0;
        for (int reportWidth : reportWidths) {
            int maxWidth = 45 * 256; // 40 Characters to Display as max + 5 for padding
            int newWidth = (reportWidth + 5) * 256 > maxWidth ? maxWidth : (reportWidth + 5) * 256;
            //System.out.println("\nmaxWidth : " + maxWidth);
            //System.out.println("newWidth : " + newWidth);

            //sheet.getColumnStyle(k).setWrapText(true); 
            sheet.setColumnWidth(k, newWidth); //30 characters wide

            k++;
        }
    }

    public void addMainWorksheetSubtotals(String sheetName, int reportColumnCount) {
        //int subTotalsRow = headerRow.getRowNum() + reportResult.getData().length;
        SXSSFSheet sheet = sxssfWorkbook.getSheet(sheetName);
        //System.out.println("\n\nsubTotalsRow : " + subTotalsRow + "\n\n");
        SXSSFRow row = sheet.createRow(subTotalsRow++);
        for (int k = 0; k < reportColumnCount; k++) {
            SXSSFCell cells = row.createCell(k);
            if (showTotal[k]) {
                cells.setCellStyle(styles.get("total"));
                cells.setCellValue(total[k]);
            }
        }
    }

    private void defineStyles() {
        dateStyle = workbook.createCellStyle();
        dateTimeStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        short datFom = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
        dateStyle.setDataFormat(datFom);
        short datTimeFom = createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeStyle.setDataFormat(datTimeFom);

        decimalStyle = workbook.createCellStyle();
        decimalStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        integerSstyle = workbook.createCellStyle();
        integerSstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
    }

    private void createStyless() {
        styles = new HashMap<>();
        CellStyle style;
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBold(true);
        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = workbook.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(true);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.DOUBLE);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.DOUBLE);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        styles.put("total", style);
    }

    public void convertToSXSSFWorkbook() {
        this.sxssfWorkbook = new SXSSFWorkbook(workbook);
    }

    public SXSSFWorkbook getSXSSFWorkbook() {
        return sxssfWorkbook;
    }

    public XSSFRow createDataHeaderRow(String sheetName, int currentRow, ReportData reportResult, String lastHeaderMergeCellReference) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        //System.out.println("Now Creating Header Row !!!");
        // Create Header Row
        XSSFRow headerRow = sheet.createRow(currentRow);
        Cell headerCell;
        int currentColumn = 0;
        for (String reportTitle : reportResult.getColumnNames()) {
            headerCell = headerRow.createCell(currentColumn);
            headerCell.setCellValue(reportTitle);
            reportWidths[currentColumn] = reportTitle.length();
            CellStyle cellStyle = styles.get("header");
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            headerCell.setCellStyle(cellStyle);
            currentColumn++;
        }

        CellRangeAddress currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.setAutoFilter(currentRange); // Set Auto Filter on Header Row   
        sheet.createFreezePane(0, currentRow + 1);
        return headerRow;
    }

    public XSSFRow createDataHeaderRow(String sheetName, int currentRow, Field[] dataFields, String lastHeaderMergeCellReference) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        //System.out.println("Now Creating Header Row !!!");
        // Create Header Row
        XSSFRow headerRow = sheet.createRow(currentRow);
        Cell headerCell;
        int currentColumn = 0;
        for (Field fieldName : dataFields) {
            String reportTitle = StringUtils.capitalise(fieldName.getName()).replaceAll("([A-Z])", " $1").trim();

            headerCell = headerRow.createCell(currentColumn);
            headerCell.setCellValue(reportTitle);
            reportWidths[currentColumn] = reportTitle.length();
            CellStyle cellStyle = styles.get("header");
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            headerCell.setCellStyle(cellStyle);
            currentColumn++;
        }

        CellRangeAddress currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.setAutoFilter(currentRange); // Set Auto Filter on Header Row   
        sheet.createFreezePane(0, currentRow + 1);
        return headerRow;
    }

    private String convertStringToFormattedDateString(String colDataStrValue) {
        Date dateValue = null;
        String formatedDate;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateValue = sdf.parse(colDataStrValue);
            formatedDate = dtf.format(dateValue);
        } catch (ParseException ex) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateValue = sdf.parse(colDataStrValue);
                formatedDate = dtf.format(dateValue);
            } catch (ParseException exx) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    dateValue = sdf.parse(colDataStrValue);
                    formatedDate = dtf.format(dateValue);
                } catch (ParseException exxx) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dateValue = sdf.parse(colDataStrValue);
                        formatedDate = dtf.format(dateValue);
                    } catch (ParseException exxxx) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            dateValue = sdf.parse(colDataStrValue);
                            formatedDate = df.format(dateValue);
                        } catch (ParseException exxxxx) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                dateValue = sdf.parse(colDataStrValue);
                                formatedDate = df.format(dateValue);
                            } catch (ParseException exxxxxx) {
                                formatedDate = colDataStrValue;
                                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exxxxxx);
                            }
                        }
                    }
                }
            }
        }

        return formatedDate;
    }
}
