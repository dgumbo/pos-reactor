package zw.co.hisolutions.pos.mis.typed.service;
 
import java.util.Arrays;
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
import zw.co.hisolutions.pos.mis.typed.entity.SalesReceiptsView;
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
public class SalesReceiptsReportService extends AbstractObjectXSSFTReportService<SalesReceiptsView> {

    @PersistenceContext
    EntityManager entityManager;

    private final String reportName = "Sales Receipts Report";

    public MisReportInformation<SalesReceiptsView> getReportPreviewInformation(Date fromDate, Date toDate) { 
        
        MisReportInformation<SalesReceiptsView> ri = new MisReportInformation();
        ri.setColumnMetadata(getReportColumns());

        List<SalesReceiptsView> reportData = getData(fromDate, toDate); 
  
        ri.setReportData(reportData);
        
        return ri;
    }  

    public SXSSFWorkbook getSalesReceiptsWorkbook(Date fromDate, Date toDate) {
        List<SalesReceiptsView> data = getData(fromDate, toDate);
        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return workbook;
    }

    public void downloadExcelReport(Date fromDate, Date toDate, HttpServletResponse response) {
        List<SalesReceiptsView> data = getData(fromDate, toDate);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        downloadExcelReport(workbook, reportName, response);
    }

    public ResponseEntity<SXSSFWorkbook> disposeFileContent(Date fromDate, Date toDate) {
        List<SalesReceiptsView> data = getData(fromDate, toDate);

        SXSSFWorkbook workbook = getWorkBook(reportName, data);

        return disposeFileContent(workbook, reportName);
    } 

    @Override
    public Class getDataViewClass() {
        return SalesReceiptsView.class;
    }

    @Override
    public List<TypedParameterMetadata> getReportParams() { 
        return Arrays.asList(
                new TypedParameterMetadata("1", "From Date", "fromDate", ReportParameterType.SINGLE_DATE_TIME ),
                new TypedParameterMetadata("2", "To Date", "toDate", ReportParameterType.SINGLE_DATE_TIME )
        );
    }

    private List<SalesReceiptsView> getData(Date fromDate, Date endDate) {

        StringBuilder queryString = new StringBuilder(" Select new " + SalesReceiptsView.class.getName() + " ( ");

        queryString.append(" r.receiptDate as date ");
        queryString.append(" , ri.amount as amount ");
        queryString.append(" , pt.name as paymentType  ");
        queryString.append(" , c.name as currency ");
        queryString.append(" , ri.convertedAmount as convertedAmount ");
        queryString.append(" , r.receiptDateTime as dateTime ");
        queryString.append(" ) ");

        queryString.append(" From Receipt r ");
        queryString.append(" Join ReceiptItem ri On r.id = ri.receipt.id ");

        queryString.append(" Join ri.paymentType pt ");
        queryString.append(" Join ri.currency c ");

        queryString.append(" Where r.receiptDateTime >= :fromDate ");
        queryString.append(" And r.receiptDateTime <= :endDate "); 
        
        queryString.append(" Order By r.receiptDateTime Desc ");

        TypedQuery<SalesReceiptsView> qry = entityManager.createQuery(queryString.toString(), SalesReceiptsView.class);

        qry.setParameter("fromDate", fromDate);
        qry.setParameter("endDate", endDate);

        List<SalesReceiptsView> resultList = qry.getResultList();
//        System.out.println("fromDate : " + fromDate);
//        System.out.println("endDate : " + endDate);
//        System.out.println("resultList.size() : " + resultList.size());
        return resultList;
    }

    @Override
    public List<ReportAnalysisSheet> getReportAnalysisSheetList() {
        return Arrays.asList(
                analysisByCurrency(),
                analysisByDateAndCurrency()
        );
    }

    private ReportAnalysisSheet analysisByCurrency() {
        ReportAnalysisSheet ras = new ReportAnalysisSheet();     
        
        ras.setDisplayName("By Currency");
        ras.setName("By Currency");
        ras.setPosition(1L);  
        
        ReportAnalysisColumn rac2 = new ReportAnalysisColumn();
        rac2.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac2.setDisplayName("Currency");
        rac2.setName("currency");
        rac2.setPosition(3);
        rac2.setNumberFormat(ReportColumnNumberFormat.None);
        rac2.setAggregateFunction(DataConsolidateFunction.SUM);
        
        ReportAnalysisColumn rac3 = new ReportAnalysisColumn();
        rac3.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac3.setDisplayName("Amount");
        rac3.setName("amount");
        rac3.setPosition(3);
        rac3.setNumberFormat(ReportColumnNumberFormat.CurrencyKilo);
        rac3.setAggregateFunction(DataConsolidateFunction.SUM);
         
        ReportAnalysisColumn rac4 = new ReportAnalysisColumn();
        rac4.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac4.setDisplayName("Converted Amount");
        rac4.setName("convertedAmount");
        rac4.setPosition(4);
        rac4.setNumberFormat(ReportColumnNumberFormat.CurrencyKilo);
        rac4.setAggregateFunction(DataConsolidateFunction.SUM);
        
        List<ReportAnalysisColumn> analysisColumnList = Arrays.asList( 
                rac2,
                rac3,
                rac4
        );
        ras.setAnalysisColumnList(analysisColumnList);  
        
        return ras;
    }

    private ReportAnalysisSheet analysisByDateAndCurrency() {
        ReportAnalysisSheet ras = new ReportAnalysisSheet();
        ras.setDisplayName("By Date & Currency");
        ras.setName("By Date & Currency");
        ras.setPosition(2L); 
        
        ReportAnalysisColumn rac1 = new ReportAnalysisColumn();
        rac1.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac1.setDisplayName("Date");
        rac1.setName("date");
        rac1.setPosition(1);
        rac1.setNumberFormat(ReportColumnNumberFormat.None);
        rac1.setAggregateFunction(DataConsolidateFunction.SUM);
        
        ReportAnalysisColumn rac2 = new ReportAnalysisColumn();
        rac2.setAnalysisSector(ReportColumnAnalysisSector.Rows);
        rac2.setDisplayName("Currency");
        rac2.setName("currency");
        rac2.setPosition(3);
        rac2.setNumberFormat(ReportColumnNumberFormat.None);
        rac2.setAggregateFunction(DataConsolidateFunction.SUM);
        
        ReportAnalysisColumn rac3 = new ReportAnalysisColumn();
        rac3.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac3.setDisplayName("Amount");
        rac3.setName("amount");
        rac3.setPosition(3);
        rac3.setNumberFormat(ReportColumnNumberFormat.CurrencyKilo);
        rac3.setAggregateFunction(DataConsolidateFunction.SUM);
         
        ReportAnalysisColumn rac4 = new ReportAnalysisColumn();
        rac4.setAnalysisSector(ReportColumnAnalysisSector.Data);
        rac4.setDisplayName("Converted Amount");
        rac4.setName("convertedAmount");
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
