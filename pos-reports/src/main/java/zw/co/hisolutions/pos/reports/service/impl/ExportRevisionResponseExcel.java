package zw.co.hisolutions.pos.reports.service.impl;

//package zw.co.psmi.hms.mis.service.impl;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//
///**
// *
// * @author dgumbo
// */
//public class ExportRevisionResponseExcel extends ExcelExportAbstractUtility<List<Object>> {
//
//    /* 
//     * @see ASTExcelExportUtility#fillData(java.util.List) 
//     */
//    @Override
//    void fillData(List<List<Object>> dataList) {
//
//        CellStyle normalStyle = getNormalStyle(); 
//        int rownum = 1;
//
//        SimpleDateFormat dtFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
//
//        for (List<Object> rev : dataList) { 
//            Row row = sh.createRow(rownum); 
//            Cell cell_0 = row.createCell(0, Cell.CELL_TYPE_STRING); 
//            cell_0.setCellStyle(normalStyle); 
//            cell_0.setCellValue(rev.getRevId()); 
//            Cell cell_1 = row.createCell(1, Cell.CELL_TYPE_STRING); 
//            cell_1.setCellStyle(normalStyle);
//
//            cell_1.setCellValue(rev.getJcrCreated() != null ? dtFormat.format(rev.getJcrCreated()) : " ");
//
//            rownum++;
// 
//        } 
//    } 
//}
