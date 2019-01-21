//package zw.co.hisolutions.pos.reports.outputs.definitions;
//
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.commons.lang.StringUtils;
//import org.apache.poi.hssf.usermodel.HSSFDataFormat;
//import org.apache.poi.ss.SpreadsheetVersion;
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.PrintSetup;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.AreaReference;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.ss.util.CellReference;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service; 
//import zw.co.hisolutions.pos.mis.typed.dao.DayendCashRegisterViewDao;
//import zw.co.hisolutions.pos.mis.typed.entity.DayendCashRegisterView;
//
///**
// *
// * @author dgumbo
// */
//@Service
//public class DayendCashRegisterService {
//    
//    @Autowired
//    DayendCashRegisterViewDao dao;
//    
//    private List<DayendCashRegisterView> getDeclaredAmount(String unitCode,String user,Date fromDate,Date endDate){
//        //List<DayendCashRegisterView> declaredAmount = dao.getDeclaredAmount(unitCode, user, fromDate, endDate);
//        List<DayendCashRegisterView> receiptedAmount = dao.getReceiptedAmount(unitCode, user, fromDate, endDate);
//        
//        // declaredAmount.addAll(receiptedAmount);
//        return receiptedAmount ;
//    }
//    
//    public XSSFWorkbook getWorkBook(String unitCode,String username,Date fromDate, Date endDate) {
//        
//        String sheetName = "Dayend Cash Register Data";
//        String reportName = "Dayend Cash Register Report";
//        List<DayendCashRegisterView> reportData = getDeclaredAmount(unitCode, username, fromDate, endDate);
//
////        UserLogin user = getCurrentAuditor();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        DateFormat dtf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
//        int reportRowCount = reportData.size() ;
//        int reportColumnCount = DayendCashRegisterView.class.getDeclaredFields().length ;
//        //String lastCellReference = CellReference.convertNumToColString(reportColumnCount);
//        String lastHeaderMergeCellReference = CellReference.convertNumToColString(reportColumnCount - 1);
//        lastHeaderMergeCellReference = reportColumnCount <= 2 ? "D" : lastHeaderMergeCellReference;
//
//        Integer[] reportWidths = new Integer[reportColumnCount];
//        XSSFWorkbook wb = new XSSFWorkbook();
//        Map<String, CellStyle> styles = createStyles(wb);
//        XSSFSheet sheet = wb.createSheet(sheetName);
//        sheet.protectSheet("unlockedpassword");
//        PrintSetup printSetup = sheet.getPrintSetup();
//        printSetup.setLandscape(true);
//        sheet.setFitToPage(true);
//        sheet.setHorizontallyCenter(true);
//
//        int currentRow = 0;
//        int currentColumn ;
//        CellRangeAddress currentRange;
//
//  //      currentRow = setSheetHeaders(sheet, reportName, currentRow, styles, lastHeaderMergeCellReference);
//
//        //System.out.println("Now Creating Header Row !!!");
//        // Create Header Row  
//        Row headerRow = sheet.createRow(currentRow);
//        Cell headerCell;
//        currentColumn = 0;
//        boolean[] showTotal = new boolean[reportColumnCount];
//        double[] total = new double[reportColumnCount];
//        for ( Field fieldName : DayendCashRegisterView.class.getDeclaredFields()) {
//            String reportTitle = StringUtils.capitalise(fieldName.getName()).replaceAll("([A-Z])", " $1").trim() ;
//            
//            headerCell = headerRow.createCell(currentColumn);
//            headerCell.setCellValue(reportTitle);
//            reportWidths[currentColumn] = reportTitle.length();
//            CellStyle cellStyle = styles.get("header");
//            cellStyle.setAlignment(HorizontalAlignment.LEFT);
//            headerCell.setCellStyle(cellStyle);
//            showTotal[currentColumn] = false;
//            total[currentColumn] = 0;
//            currentColumn++;
//        }
//        currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
//        sheet.setAutoFilter(currentRange); // Set Auto Filter on Header Row   
//        sheet.createFreezePane(0,currentRow+1);
//        currentRow++;
//
//        //System.out.println("Now Adding Report Data !!!"); 
//        CellStyle dateStyle = wb.createCellStyle();
//        CreationHelper createHelper = wb.getCreationHelper();
//        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
//
//        CellStyle decimalStyle = wb.createCellStyle();
//        decimalStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
//
//        CellStyle integerSstyle = wb.createCellStyle();
//        integerSstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
//
//        currentColumn = 0;
//        for ( DayendCashRegisterView colData : reportData) {
//            int f = 0;
//            Row row = sheet.createRow(currentRow);
//            //Cell cell;
//            int k =  0;
//            for( Field field : DayendCashRegisterView.class.getDeclaredFields()) {
//                Object data = null;
//                //Turn off access check for this field
//                field.setAccessible(true);
//                Object fieldValue = null;
//                try {
//                    fieldValue = field.get(colData);
//                } catch (IllegalArgumentException | IllegalAccessException ex) {
//                    Logger.getLogger(DayendCashRegisterService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                // System.out.println("fieldName : " + field + ", fieldValue : " + fieldValue.toString());
//                
//                String fieldName = field.getName() ;
//                Cell cell = row.createCell(k);
//                colData.getClass() ;
//                if (fieldValue != null) {
//                    data = fieldValue;
//                    //System.out.println("fieldValue : " + fieldValue + ", fieldValue instanceof : " + fieldValue.getClass());
//
//                    if (reportWidths[k] < data.toString().length() + 3) {
//                        reportWidths[k] = data.toString().length() + 3;
//                    }
//
//                    if (fieldValue instanceof Date) {
//                        try {
//                            cell.setCellValue(df.parse(fieldValue.toString()));
//                        } catch (ParseException e) {
//                        }
//                        cell.setCellStyle(dateStyle);
//                    } else if (fieldValue instanceof Double || fieldValue instanceof BigDecimal) {
//                        double val = Double.parseDouble(fieldValue.toString());
//                        cell.setCellValue(val);
//                        total[k] += val;
//                        showTotal[k] = true;
//                        cell.setCellStyle(decimalStyle);
//                    } else if (fieldValue instanceof Integer || fieldValue instanceof BigInteger || fieldValue instanceof Long) {
//                        long val = Long.parseLong(fieldValue.toString());
//                        cell.setCellValue(val);
//                        total[k] += val;
//                        showTotal[k] = true;
//                        cell.setCellStyle(integerSstyle);
//                    } else {
//                        cell.setCellValue(fieldValue.toString());
//                    }
//                } else {
//                    cell.setCellValue("");
//                }
//                k++;
//            }
//            currentRow++;
//        }
//
//        //CellRangeAddress dataRange = CellRangeAddress.valueOf(("$A$" + (iTitleRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
//        String sRange = ("A" + (headerRow.getRowNum() + 1) + ":" + lastHeaderMergeCellReference + "" + (currentRow)).replace(" ", "");
//        AreaReference areaReference = new AreaReference(sRange, SpreadsheetVersion.EXCEL2007);
//
//        Row row = sheet.createRow(currentRow++);
//        for (int k = 0; k < reportColumnCount; k++) {
//            Cell cells = row.createCell(k);
//            if (showTotal[k]) {
//                cells.setCellStyle(styles.get("total"));
//                cells.setCellValue(total[k]);
//            }
//        }
//
//        int k = 0;
//        for (int reportWidth : reportWidths) {
//            int maxWidth = 45 * 256; // 40 Characters to Display as max + 5 for padding
//            int newWidth = (reportWidth + 5) * 256 > maxWidth ? maxWidth : (reportWidth + 5) * 256;
//            //System.out.println("\nmaxWidth : " + maxWidth);
//            //System.out.println("newWidth : " + newWidth);
//
//            //sheet.getColumnStyle(k).setWrapText(true); 
//            sheet.setColumnWidth(k, newWidth); //30 characters wide
//
//            k++;
//        }
//
//        //wb = populatePivotAnalysisWorksheets(reportConfig, wb, sheet, areaReference, report, styles,holder, configParameters);
//
//        return wb;
//    }
//
//    private  static Map<String, CellStyle> createStyles(Workbook wb) {
//        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
//        CellStyle style;
//        Font titleFont = wb.createFont();
//        titleFont.setFontHeightInPoints((short) 18);
//        titleFont.setBold(true);
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFont(titleFont);
//        styles.put("title", style);
//
//        Font monthFont = wb.createFont();
//        monthFont.setFontHeightInPoints((short) 11);
//        monthFont.setColor(IndexedColors.WHITE.getIndex());
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setFont(monthFont);
//        style.setWrapText(true);
//        styles.put("header", style);
//
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setWrapText(true);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(BorderStyle.THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        styles.put("cell", style);
//
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
//        styles.put("formula", style);
//
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
//        styles.put("formula_2", style);
//
//        style = wb.createCellStyle();
//        style.setAlignment(HorizontalAlignment.RIGHT);
//        style.setWrapText(true);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(BorderStyle.DOUBLE);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(BorderStyle.DOUBLE);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
//        styles.put("total", style);
//
//        return styles;
//    }
//
////    private UserLogin getCurrentAuditor() {
////        try {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            if (authentication != null && authentication.isAuthenticated()) {
////                if (authentication.getPrincipal() instanceof UserLogin) {
////                    return (UserLogin) authentication.getPrincipal();
////                }
////            }
////        } catch (Exception e) {
////            //log.error("Authentication:", e);
////        }
////        return null;
////    }
//    
//    public String capitalizeFirstLetter(String original) {
//        if (original == null || original.length() == 0) {
//            return original;
//        }
//        return original.substring(0, 1).toUpperCase() + original.substring(1);
//    }
//}
