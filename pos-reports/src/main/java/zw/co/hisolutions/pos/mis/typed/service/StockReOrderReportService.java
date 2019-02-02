package zw.co.hisolutions.pos.mis.typed.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.mis.typed.entity.MisReportInformation;
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;  
import zw.co.hisolutions.pos.mis.typed.service.hqlGen.StockReOrderHql;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;
import zw.co.hisolutions.pos.reports.entity.ReportColumnAnalysisSector;
import zw.co.hisolutions.pos.reports.entity.ReportColumnNumberFormat;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService;

/**
 *
 * @author dgumbo
 */ 
@Service
public class StockReOrderReportService extends AbstractObjectXSSFTReportService<StockReOrderView> {

    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    StockReOrderHql stockReOrderHql;

    private final String reportName = "Stock Re-Order Report";

    public MisReportInformation<StockReOrderView> getReportPreviewInformation(String stockItemName) {

        MisReportInformation<StockReOrderView> ri = new MisReportInformation();
        ri.setColumnMetadata(getReportColumns());

        List<StockReOrderView> reportData = getData(stockItemName);

        ri.setReportData(reportData);

        return ri;
    }

    public SXSSFWorkbook getSalesReceiptsWorkbook(String stockItemName) {
        List<StockReOrderView> data = getData(stockItemName);
        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return workbook;
    }

    public void downloadExcelReport(String stockItemName, HttpServletResponse response) {
        List<StockReOrderView> data = getData(stockItemName);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        downloadExcelReport(workbook, reportName, response);
    }

    public ResponseEntity<SXSSFWorkbook> disposeFileContent(String stockItemName) {
        List<StockReOrderView> data = getData(stockItemName);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return disposeFileContent(workbook, reportName);
    }

    @Override
    public Class getDataViewClass() {
        return StockReOrderView.class;
    }

    @Override
    public List<TypedParameterMetadata> getReportParams() {
        return Arrays.asList(
                new TypedParameterMetadata("1", "Stock Item Name", "stockItemName", ReportParameterType.STRING_LIKE)
        );
    }

    private List<StockReOrderView> getData(String stockItemName) {
//System.out.println(" zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()" );

        long leadDays = 8;
        int daysBackAgo = 60;
        Date dateBackAgo;

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -daysBackAgo);
        dateBackAgo = cal.getTime();

        //TypedQuery<StockReOrderView> qry = createNQuery(stockItemName, dateBackAgo, daysBackAgo, leadDays);
        TypedQuery<StockReOrderView> qry = stockReOrderHql.createNQuery(stockItemName, dateBackAgo, daysBackAgo, leadDays);

//        System.out.println("\n\n qry");
//        System.out.println(" " +  qry.toString());
//        System.out.println("\n\n ");
        List<StockReOrderView> results = qry.getResultList()
                .stream()
                // .filter(sro -> sro.getCurrentStock() < sro.getTotalSafetyStock())
                .collect(Collectors.toList());

        Comparator<StockReOrderView> comp = (o1, o2) -> {
            int iSort = 0;
            iSort = o1.getCurrentStockDepletionDays() > o2.getCurrentStockDepletionDays() ? 5 : -5;
            iSort = o1.getCurrentStockDepletionDays() == o2.getCurrentStockDepletionDays()
                    && o1.getCurrentStock() > o2.getCurrentStock() ? 4 : iSort;
            iSort = o1.getCurrentStockDepletionDays() == o2.getCurrentStockDepletionDays()
                    && o1.getCurrentStock() == o2.getCurrentStock()
                    && o1.getUnitCost().compareTo(o2.getUnitCost()) < 0 ? 3 : iSort;

            return iSort;
        };
        results.sort(comp);

        return results;
    }
 
    @Override
    public List<ReportAnalysisSheet> getReportAnalysisSheetList() {
        return Arrays.asList(
                analysisByProductCategoryAndStockItem(),
                analysisByDateAndStockItem()
        );
    }

    private ReportAnalysisSheet analysisByProductCategoryAndStockItem() {
        ReportAnalysisSheet ras = new ReportAnalysisSheet();

        ras.setDisplayName("By Stock Item");
        ras.setName("By Stock Item");
        ras.setPosition(1L);

        ReportAnalysisColumn rac1 = new ReportAnalysisColumn();
        rac1.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac1.setDisplayName("Product Category");
        rac1.setName("productCategory");
        rac1.setPosition(1);
        rac1.setNumberFormat(ReportColumnNumberFormat.None);
        rac1.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac2 = new ReportAnalysisColumn();
        rac2.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac2.setDisplayName("Stock Item");
        rac2.setName("stockItem");
        rac2.setPosition(3);
        rac2.setNumberFormat(ReportColumnNumberFormat.None);
        rac2.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac3 = new ReportAnalysisColumn();
        rac3.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac3.setDisplayName("Quantity");
        rac3.setName("quantity");
        rac3.setPosition(3);
        rac3.setNumberFormat(ReportColumnNumberFormat.IntegerKilo);
        rac3.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac4 = new ReportAnalysisColumn();
        rac4.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac4.setDisplayName("Total Price");
        rac4.setName("totalPrice");
        rac4.setPosition(4);
        rac4.setNumberFormat(ReportColumnNumberFormat.CurrencyKilo);
        rac4.setAggregateFunction(DataConsolidateFunction.SUM);

        List<ReportAnalysisColumn> analysisColumnList = Arrays.asList(
                rac1,
                rac2,
                rac3,
                rac4
        );
        ras.setAnalysisColumnList(analysisColumnList);

        return ras;
    }

    private ReportAnalysisSheet analysisByDateAndStockItem() {
        ReportAnalysisSheet ras = new ReportAnalysisSheet();
        ras.setDisplayName("By Date & Stock Item");
        ras.setName("By Date & Stock Item");
        ras.setPosition(2L);

        ReportAnalysisColumn rac1 = new ReportAnalysisColumn();
        rac1.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac1.setDisplayName("Sell Date");
        rac1.setName("sellDate");
        rac1.setPosition(1);
        rac1.setNumberFormat(ReportColumnNumberFormat.None);
        rac1.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac2 = new ReportAnalysisColumn();
        rac2.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac2.setDisplayName("Stock Item");
        rac2.setName("stockItem");
        rac2.setPosition(3);
        rac2.setNumberFormat(ReportColumnNumberFormat.None);
        rac2.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac3 = new ReportAnalysisColumn();
        rac3.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac3.setDisplayName("Quantity");
        rac3.setName("quantity");
        rac3.setPosition(3);
        rac3.setNumberFormat(ReportColumnNumberFormat.IntegerKilo);
        rac3.setAggregateFunction(DataConsolidateFunction.SUM);

        ReportAnalysisColumn rac4 = new ReportAnalysisColumn();
        rac4.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac4.setDisplayName("Total Price");
        rac4.setName("totalPrice");
        rac4.setPosition(4);
        rac4.setNumberFormat(ReportColumnNumberFormat.CurrencyKilo);
        rac4.setAggregateFunction(DataConsolidateFunction.SUM);

        List<ReportAnalysisColumn> analysisColumnList = Arrays.asList(
                rac1,
                rac2,
                rac3,
                rac4
        );
        ras.setAnalysisColumnList(analysisColumnList);

        return ras;
    }

}
