package zw.co.hisolutions.pos.mis.typed.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
import zw.co.hisolutions.pos.mis.typed.entity.SalesSummaryView;
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
public class SalesSummaryReportService extends AbstractObjectXSSFTReportService<SalesSummaryView> {

    @PersistenceContext
    EntityManager entityManager;

    private final String reportName = "Sales Summary Report";

    public MisReportInformation<SalesSummaryView> getReportPreviewInformation(Date fromDate, Date toDate) {

        MisReportInformation<SalesSummaryView> ri = new MisReportInformation();
        ri.setColumnMetadata(getReportColumns());

        List<SalesSummaryView> reportData = getData(null, fromDate, toDate);

        ri.setReportData(reportData);

        return ri;
    }

    public SXSSFWorkbook getSalesReceiptsWorkbook(Date fromDate, Date toDate) {
        List<SalesSummaryView> data = getData(null, fromDate, toDate);
        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return workbook;
    }

    public void downloadExcelReport(Date fromDate, Date toDate, HttpServletResponse response) {
        List<SalesSummaryView> data = getData(null, fromDate, toDate);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        downloadExcelReport(workbook, reportName, response);
    }

    public ResponseEntity<SXSSFWorkbook> disposeFileContent(Date fromDate, Date toDate) {
        List<SalesSummaryView> data = getData(null, fromDate, toDate);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return disposeFileContent(workbook, reportName);
    }

    @Override
    public Class getDataViewClass() {
        return SalesSummaryView.class;
    }

    @Override
    public List<TypedParameterMetadata> getReportParams() {
        return Arrays.asList(
                new TypedParameterMetadata("1", "From Date", "fromDate", ReportParameterType.SINGLE_DATE_TIME),
                new TypedParameterMetadata("2", "To Date", "toDate", ReportParameterType.SINGLE_DATE_TIME)
        );
    }

    private List<SalesSummaryView> getData(String stockItemName, Date fromDate, Date toDate) {

        StringBuilder queryString = new StringBuilder(" Select new " + SalesSummaryView.class.getName() + " ( ");

        /* Sell */
        queryString.append(" s.sellDate as sellDate");

        queryString.append(" , pc.name as productCategory");
        queryString.append(" , styp.name as stockItem");
        queryString.append(" , si.quantity as quantity");
        queryString.append(" , si.unitCost as unitPrice");
        queryString.append(" , si.quantity * si.unitCost as totalPrice");
        // queryString.append(" , s.createdByUser as createUsername");
        queryString.append(" , s.sellDateTime as sellDatetime");
        queryString.append(" ) ");

        /* Sell */
        queryString.append(" From Sell s ");
        /* SellItem */
        queryString.append(" Join SellItem si On s.id = si.sell.id ");
        /* StockType */
        queryString.append(" Join si.product styp ");
        /* Product Category */
        queryString.append(" Join styp.productCategory pc ");

        queryString.append(" Where s.sellDateTime >= :fromDate ");
        queryString.append(" And s.sellDateTime <= :toDate ");

        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            queryString.append(" And styp.name like '%' + :stockItemName + '%' ");
        }

        queryString.append(" Order By s.id Desc ");

        TypedQuery<SalesSummaryView> qry = entityManager.createQuery(queryString.toString(), SalesSummaryView.class);

        qry.setParameter("fromDate", fromDate);
        qry.setParameter("toDate", toDate);

        if (stockItemName != null && !stockItemName.trim().equalsIgnoreCase("") && !stockItemName.trim().equalsIgnoreCase("all")) {
            qry.setParameter("stockItemName", stockItemName);
        }

        List<SalesSummaryView> results = qry.getResultList();

        Comparator<SalesSummaryView> comp = (o1, o2) -> {
            int iSort = 0;
            iSort = o1.getSellDatetime().before(o2.getSellDatetime()) ? 5 : -5;
//            iSort = o1.getCurrentStockDepletionDays() == o2.getCurrentStockDepletionDays()
//                    && o1.getCurrentStock() > o2.getCurrentStock() ? 4 : iSort;
//            iSort = o1.getCurrentStockDepletionDays() == o2.getCurrentStockDepletionDays()
//                    && o1.getCurrentStock() == o2.getCurrentStock()
//                    && o1.getUnitCost().compareTo(o2.getUnitCost()) < 0 ? 3 : iSort;

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
