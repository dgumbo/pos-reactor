package zw.co.hisolutions.pos.init.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;

/**
 *
 * @author dgumbo
 */
@Service
public class InitStockLedgerReport {

    private final ReportConfigService reportConfigService;

    public InitStockLedgerReport(ReportConfigService reportConfigService) {
        this.reportConfigService = reportConfigService;
    }

    public ReportConfig initReport() {
        String reportName = "Stock Ledger Report";
        ReportConfig reportConfig = reportConfigService.getByName(reportName);

        if (reportConfig == null) {
            reportConfig = new ReportConfig();
            String reportColumns = "Stock Item, Batch_Number, Consignment Stock, Transaction Date, Transaction, Quantity, Stock Item, Batch_Number, Consignment Stock, Transaction Time, LRCR Total, Expiry Date, Transaction Time, Transaction Count";

            ReportConfigParameter startDateParam = new ReportConfigParameter();
            startDateParam.setName("Start Date");
            startDateParam.setParameter("parameter1");
            startDateParam.setParameterType(ReportParameterType.SINGLE_DATE_TIME);
            startDateParam.setReportConfig(reportConfig);
            startDateParam.setColumnName("StartDate");

            ReportConfigParameter endDateParam = new ReportConfigParameter();
            endDateParam.setName("End Date");
            endDateParam.setParameter("parameter2");
            endDateParam.setParameterType(ReportParameterType.SINGLE_DATE_TIME);
            endDateParam.setReportConfig(reportConfig);
            endDateParam.setColumnName("EndDate");

            ReportConfigParameter stockTypeParam = new ReportConfigParameter();
            stockTypeParam.setName("Stock Type");
            stockTypeParam.setParameter("parameter3");
            stockTypeParam.setParameterType(ReportParameterType.STRING_LIKE);
            stockTypeParam.setReportConfig(reportConfig);
            stockTypeParam.setColumnName("StockType");

            List<ReportConfigParameter> reportConfigParameters = Arrays.asList(
                    startDateParam,
                    endDateParam,
                    stockTypeParam
            );

            reportConfig.setName(reportName);
            reportConfig.setColumns(reportColumns);
            reportConfig.setActiveStatus(true);
            reportConfig.setNativeQuery(buildNativeSqlQuery());
            reportConfig.setReportConfigParameters(reportConfigParameters);

            reportConfig = reportConfigService.save(reportConfig);
        }

        return reportConfig;
    }

    private String buildNativeSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("Declare @StartDate DateTime = :StartDate  "
                + "\n Declare @EndDate DateTime = :EndDate   "
                + "\n Declare @StockItem Varchar(255) = :StockType   ;   "
                + "\n  "
                + "\n Select 	[Stock Item], [Batch_Number], [Consignment Stock], [Transaction Date], [Transaction], Quantity Quantity,\n"
                + "	 \n"
                + "	SUM (	Case \n"
                + "				When [Lag Stock Transaction] = 'STOCK_UPDATE' And [Transaction] In ('CLOSSING_STOCK') Then 0\n"
                + "				When [Lag Stock Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) \n"
                + "				When [Transaction] In ('OPENING_STOCK', 'CLOSSING_STOCK') Then 0 \n"
                + "				When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT') Then 0 \n"
                + "				When [Transaction] In ('GOODS_RECEIVED_VOUCHER') Then 0 /* Needs Further Testing As This Might be neccesary only When First Trans is GRV */\n"
                + "				Else Coalesce(Quantity, 0) \n"
                + "			End \n"
                + "			+\n"
                + "			Case \n"
                + "				When RN = 1 Then Coalesce(Quantity, 0) \n"
                + "				When [Transaction] In ('CLOSSING_STOCK') Then 0 \n"
                + "				When [Transaction] In ('BOOK_STOCK', 'STOCK_UPDATE') Then -Coalesce(Quantity, 0)\n"
                + "				When [Transaction] In ('ADJUSTMENT_RECEIPT', 'DEPARTMENT_RECEIPT', 'GOODS_RECEIVED_VOUCHER') Then Coalesce(Quantity, 0) \n"
                + "				Else 0 \n"
                + "			End\n"
                + "			+\n"
                + "			Case \n"
                + "				When [Transaction] = 'STOCK_UPDATE' Then Coalesce(Quantity, 0) - Coalesce(RunningQuantityTotal, 0) + Coalesce(Quantity, 0)\n"
                + "				Else 0 \n"
                + "			End\n"
                + "		) OVER (Partition By  [Stock Item], [Batch_Number], [Consignment Stock] ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, [Transaction Time]) \n"
                + "	AS [Running Stock] ,	\n" 
                + "	[LRCR Total],	 \n"
                + "	[Expiry Date],	\n"
                + "	[Transaction Time],\n"
                + "	[Transaction Count]\n"
                + "/* --	, *\n"
                + "Into #Results */\n"
                + "From (\n"
                + "	Select StockLedger.stock_item  [Stock Item]\n"
                + "		, StockLedger.batch_number [Batch_Number] \n"
                + "		, Case When StockLedger.cs_consignment = 1 Then 'Yes' Else 'No' End [Consignment Stock]\n"
                + "		, StockLedger.cs_consignment\n"
                + "		, Convert(Date, StockLedger.transaction_time) [Transaction Date] \n"
                + "		, [Transaction] \n"
                + "		, Quantity \n"
                + "		, [LRCR Total]\n"
                + "		, expiry_date [Expiry Date] \n"
                + "		, StockLedger.transaction_time [Transaction Time]  \n"
                + "		, Row_number() OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) \n"
                + "			AS RN \n"
                + "		, SUM (	Quantity ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) \n"
                + "			AS RunningQuantityTotal 	 \n"
                + "		, LAG (	Quantity ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) \n"
                + "			AS LagQuantity \n"
                + "		, LAG (	[Transaction]  ) OVER (Partition By  stock_item,   batch_number, cs_consignment ORDER BY  Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, StockLedger.transaction_time) \n"
                + "			AS [Lag Stock Transaction]  \n"
                + "		, Sum( [Transaction Count] ) Over(Partition By  stock_item,   batch_number, cs_consignment Order By [Transaction])  [Total Transaction Count]\n"
                + "		, [Transaction Count] \n"
                + "	From (\n"
                + "		/* OPENING_STOCK */\n"
                + "		Select @StartDate transaction_time, 'OPENING_STOCK' [Transaction], stock_item_id, quantity\n"
                + "		, Coalesce(cs.quantity, 0) * styp.last_receipt_cost_rate [LRCR Total] \n"
                + "		, cs.batch_number, cs.consignment cs_consignment, expiry_date\n"
                + "		, styp.name stock_item \n"
                + "		, 0 [Transaction Count]\n"
                + "		From current_stock AS cs With(NoLock)   \n"
                + "		Inner Join stock_item As styp With(NoLock) On cs.stock_item_id = styp.id    \n"
                + " \n"
                + "		Where ( \n"
                + "		 ( cs.creation_time <= @StartDate And cs.stock_status = 'AVAILABLE' ) 		 \n"
                + "		  Or  \n"
                + "		  ( cs.creation_time <= @StartDate And cs.stock_status Like 'SPLIT%' And cs.modification_time >= @StartDate )  )  \n"
                + "		And styp.name Like  '%' +  @StockItem + '%' \n"
                + "\n"
                + "		Union\n"
                + "	 \n"
                + "		/* CLOSSING_STOCK */\n"
                + "		Select @EndDate transaction_time, 'CLOSSING_STOCK' [Transaction], stock_item_id, quantity\n"
                + "		, Coalesce(cs.quantity, 0) * styp.last_receipt_cost_rate [LRCR Total] \n"
                + "		, cs.batch_number, cs.consignment cs_consignment, expiry_date\n"
                + "		, styp.name stock_item \n"
                + "		, 0 [Transaction Count]\n"
                + "		From current_stock AS cs With(NoLock)   \n"
                + "		Inner Join stock_item As styp With(NoLock) On cs.stock_item_id = styp.id    \n"
                + " \n"
                + "		Where ( \n"
                + "		 ( cs.creation_time <= @EndDate And cs.stock_status = 'AVAILABLE' ) 		 \n"
                + "		  Or  \n"
                + "		  ( cs.creation_time <= @EndDate And cs.stock_status Like 'SPLIT%' And cs.modification_time >= @EndDate )  )  \n"
                + "		And styp.name Like  '%' +  @StockItem + '%' \n"
                + "\n"
                + "		Union\n"
                + "\n"
                + "		/* Stock Transactions */\n"
                + "		SELECT st.transaction_time transaction_time\n"
                + "		 \n"
                + "		, st.stock_transaction_type [Transaction]\n"
                + "		, stl.product_id\n"
                + "\n"
                + "		, Case \n"
                + "			When st.stock_transaction_type  In ('ADJUSTMENT_ISSUE', 'STOCK_ISSUE', 'STOCK_SELL', 'STOCK_SELL_TRAY') Then -stl.quantity \n"
                + "			Else stl.quantity \n"
                + "		  End Quantity\n"
                + "		, Case \n"
                + "			When st.stock_transaction_type  In ('ADJUSTMENT_ISSUE', 'STOCK_ISSUE', 'STOCK_SELL', 'STOCK_SELL_TRAY') Then -stl.quantity \n"
                + "			Else stl.quantity \n"
                + "		  End * stl.last_receipt_cost_rate [LRCR Total] \n"
                + "	\n"
                + "		, cs.batch_number\n"
                + "		, cs.consignment cs_consignment\n"
                + "		, cs.expiry_date \n"
                + "		, styp.name stock_item  \n"
                + "		, 1 [Transaction Count]\n"
                + "		From stock_transaction As st With(NoLock) \n"
                + "		Inner Join stock_transaction_line AS stl With(NoLock) On stl.stock_transaction_id = st.id \n"
                + "		Left Join current_stock cs With(NoLock) On cs.id = Coalesce(stl.current_stock_id_transaction_on , stl.current_stock_id_before, stl.current_stock_id_after)\n"
                + "		Left Join current_stock split With(NoLock) On split.id = stl.current_stock_id_after\n"
                + "		   \n"
                + "		Inner Join stock_item As styp With(NoLock) On stl.product_id = styp.id\n"
                + "		 \n"
                + "		Where st.transaction_time >= @StartDate \n"
                + "		And st.transaction_time <= @EndDate  \n"
                + "		And st.stock_transaction_type Not In ('BOOK_STOCK')\n"
                + "		And styp.name like '%' + @StockItem + '%' \n"
                + "			\n"
                + "	) StockLedger    \n"
                + "\n"
                + "	Where StockLedger.stock_item like '%' + @StockItem + '%'  \n"
                + ") shshs\n"
                + "\n"
                + "Order By shshs.[Stock Item], shshs.[Batch_Number], [Consignment Stock], Case [Transaction] When 'OPENING_STOCK' Then 1 When 'CLOSSING_STOCK' Then 3 Else 2 End Asc, shshs.[transaction time]");

        return sb.toString();
    }

}
