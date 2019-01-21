package zw.co.hisolutions.pos.mis.typed.service;

import java.util.Arrays; 
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.mis.typed.entity.MisReportInformation;
import zw.co.hisolutions.pos.mis.typed.entity.StockOutView;
import zw.co.hisolutions.pos.mis.typed.entity.TypedParameterMetadata;
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
public class StockOutReportService extends AbstractObjectXSSFTReportService<StockOutView> {

    @PersistenceContext
    EntityManager entityManager;

    private final String reportName = "Stock Out Report";

    public MisReportInformation<StockOutView> getReportPreviewInformation(String stockItemName) {

        MisReportInformation<StockOutView> ri = new MisReportInformation();
        ri.setColumnMetadata(getReportColumns());

        List<StockOutView> reportData = getData(stockItemName);

        ri.setReportData(reportData);

        return ri;
    }

    public SXSSFWorkbook getSalesReceiptsWorkbook(String stockItemName) {
        List<StockOutView> data = getData(stockItemName);
        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return workbook;
    }

    public void downloadExcelReport(String stockItemName, HttpServletResponse response) {
        List<StockOutView> data = getData(stockItemName);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        downloadExcelReport(workbook, reportName, response);
    }

    public ResponseEntity<SXSSFWorkbook> disposeFileContent(String stockItemName) {
        List<StockOutView> data = getData(stockItemName);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return disposeFileContent(workbook, reportName);
    }

    @Override
    public Class getDataViewClass() {
        return StockOutView.class;
    }

    @Override
    public List<TypedParameterMetadata> getReportParams() {
        return Arrays.asList(
                new TypedParameterMetadata("1", "Stock Item Name", "stockItemName", ReportParameterType.STRING_LIKE)
        );
    }

    private List<StockOutView> getData(String stockItemName) {

        StringBuilder queryString = new StringBuilder(" Select new " + StockOutView.class.getName() + " ( ");

        queryString.append(" pc.name As ProductCategory ");
        queryString.append(" , si.name As StockItem   ");
        queryString.append(" , cs.quantity As Quantity  ");
        queryString.append(" , si.lastReceiptCostRate As LRCR ");
        queryString.append(" , si.weightedAverageCost As WAC "); 
        queryString.append(" , sop.price As SellingPrice "); 
        queryString.append(" , c.name As Currency ");
        queryString.append(" ) ");

        queryString.append(" From CurrentStock cs  ");
        queryString.append(" Join cs.stockItem si  ");
        queryString.append(" Join si.productCategory pc  ");
        queryString.append(" Left Join ScheduleOfPrice sop On sop.product.id = si.id And sop.chargeType = 'PRIMARY' ");
        queryString.append(" Left Join sop.currency c ");

        queryString.append(" Where cs.quantity = 0  ");
        queryString.append(" And cs.stockStatus = 'AVAILABLE'  ");
        // queryString.append(" And cs.stockStatus = zw.co.hisolutions.pos.stocks.entity.StockStatus.AVAILABLE  ");

        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            queryString.append(" And si.name like :stockItemName  ");
        }

        TypedQuery<StockOutView> qry = entityManager.createQuery(queryString.toString(), StockOutView.class);

        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            qry.setParameter("stockItemName", "%" + stockItemName + "%");
        }

        return qry.getResultList();
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
