package zw.co.hisolutions.pos.sxssf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
 
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.entity.ReportData;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItem;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STItemType; 
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;
import zw.co.hisolutions.pos.reports.entity.ReportColumnAnalysisSector; 
import zw.co.hisolutions.pos.reports.helper.service.impl.WorkbookHelper;
import zw.co.hisolutions.pos.reports.service.ReportAnalysisService;
import zw.co.hisolutions.pos.reports.service.ReportDataService;
import zw.co.hisolutions.pos.sxssf.service.SXSSFReportService;

@Service
@Slf4j
public class XSSFReportServiceImpl implements SXSSFReportService {

    private final ReportConfigService reportConfigService; 
    private final ReportAnalysisService reportAnalysisService;
    private final ReportDataService reportResultService;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dtf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

    public XSSFReportServiceImpl(ReportConfigService reportConfigService,   ReportAnalysisService reportAnalysisService, ReportDataService reportResultService) {
        this.reportConfigService = reportConfigService; 
        this.reportAnalysisService = reportAnalysisService;
        this.reportResultService = reportResultService;
    }

    @Override
    public SXSSFWorkbook getWorkBook(ReportConfig reportConfig, ParameterHolder holder, boolean parametersSet) {

        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);

        ReportData reportResult = reportResultService.getReportResultData(reportConfig, holder, configParameters);

        int reportColumnCount = reportResult.getColumnNames().length;
        String lastHeaderMergeCellReference = CellReference.convertNumToColString(reportColumnCount - 1);
        lastHeaderMergeCellReference = reportColumnCount <= 2 ? "D" : lastHeaderMergeCellReference;

        WorkbookHelper workbookHelper = new WorkbookHelper(reportColumnCount);

        XSSFSheet sheet = workbookHelper.createSheet(reportConfig.getName());

        int currentRow = 0;

        Map<String, CellStyle> styles = workbookHelper.getStyles();
        currentRow = setSheetHeaders(sheet, reportConfig.getName(), currentRow, reportConfig, styles, lastHeaderMergeCellReference, holder, configParameters);

        XSSFRow headerRow = workbookHelper.createDataHeaderRow(reportConfig.getName(), currentRow, reportResult, lastHeaderMergeCellReference);

        populatePivotAnalysisWorksheets(reportConfig, workbookHelper.getWorkbook(), sheet, headerRow, reportResult, styles, holder, configParameters);

        /* break operations here and return unpopulated worksheet */
        workbookHelper.populateMainWorksheet(reportResult, reportConfig.getName(), headerRow);
        /* addMainWorksheetSubtotals */
        workbookHelper.addMainWorksheetSubtotals(reportConfig.getName(), reportColumnCount);
        return workbookHelper.getSXSSFWorkbook();
    }

    private int setSheetHeaders(XSSFSheet sheet, String title, int currentRow, ReportConfig reportConfig, Map<String, CellStyle> styles, String lastHeaderMergeCellReference, ParameterHolder holders, List<ReportConfigParameter> configParameters) {
        //UserLogin user = getCurrentAuditor();

        CellRangeAddress currentRange;
        // Set Report Title Row
        Row titleRow = sheet.createRow(currentRow);
        titleRow.setHeightInPoints(40);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        //titleCell.setCellValue(reportConfig.getName());
        CellStyle titleStyle = styles.get("title");
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);
        currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.addMergedRegion(currentRange); // Merge and Center        
        //sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1")); // Merge and Center
        currentRow++;

        CellStyle boldCellStyle = sheet.getWorkbook().createCellStyle();
        boldCellStyle.setAlignment(HorizontalAlignment.LEFT);
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(true);
        boldCellStyle.setFont(font); 

        // Add Report Filters to Header
        for (ReportConfigParameter reportConfigParameter : configParameters) {
            String value = getDataValue(holders, reportConfigParameter.getParameter());
            value = value == null ? "" : value.trim();

            //System.out.println("reportConfigParameter.getColumnName() : [" + reportConfigParameter.getColumnName() + "], value : [" + value + "]");
            if (!value.isEmpty()) {
                Row reportRunForFilterRow = sheet.createRow(currentRow);
                //   reportRunForUnit.setRowStyle(borderlessCellStyle);
                Cell reportRunForUnitCell1 = reportRunForFilterRow.createCell(0);
                reportRunForUnitCell1.setCellValue(reportConfigParameter.getName() + " : ");
                currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$B$" + (currentRow + 1)).replace(" ", ""));
                sheet.addMergedRegion(currentRange); // Merge and Center

                Cell reportRunForUnitCell2 = reportRunForFilterRow.createCell(2);
                reportRunForUnitCell2.setCellStyle(boldCellStyle);

                if (null == reportConfigParameter.getParameterType()) {
                    reportRunForUnitCell2.setCellValue(value);
                } else {
                    switch (reportConfigParameter.getParameterType()) {
                        case SINGLE_DATE:
                            try {//System.out.println("value : " + value);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date dateValue = sdf.parse(value);
                                reportRunForUnitCell2.setCellValue(df.format(dateValue));
                            } catch (ParseException ex) {
                                reportRunForUnitCell2.setCellValue(value);
                                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case SINGLE_DATE_TIME:
                            Date dateValue;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dateValue = sdf.parse(value);
                                reportRunForUnitCell2.setCellValue(dtf.format(dateValue));
                            } catch (ParseException ex) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    dateValue = sdf.parse(value);
                                    reportRunForUnitCell2.setCellValue(dtf.format(dateValue));
                                } catch (ParseException exx) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                        dateValue = sdf.parse(value);
                                        reportRunForUnitCell2.setCellValue(dtf.format(dateValue));
                                    } catch (ParseException exxx) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                            dateValue = sdf.parse(value);
                                            reportRunForUnitCell2.setCellValue(dtf.format(dateValue));
                                        } catch (ParseException exxxx) {
                                            reportRunForUnitCell2.setCellValue(value);
                                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exxxx);
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            reportRunForUnitCell2.setCellValue(value);
                            break;
                    }
                }

                currentRange = CellRangeAddress.valueOf(("$C$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
                sheet.addMergedRegion(currentRange); // Merge and Center
                currentRow++;
            }
        }

//      Add Space Row Report Title Row and Next
        sheet.createRow(currentRow).createCell(0);
        currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.addMergedRegion(currentRange);
        currentRow++;

//      Add Report Execution User
//        if (user != null) {
//            Row reportRunByRow = sheet.createRow(currentRow);
//            //    reportRunByRow.setRowStyle(borderlessCellStyle);
//            Cell reportRunByCell1 = reportRunByRow.createCell(0);
//            reportRunByCell1.setCellValue("Report Executed By :");
//            currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$B$" + (currentRow + 1)).replace(" ", ""));
//            sheet.addMergedRegion(currentRange); // Merge and Center
//
//            Cell reportRunByCell2 = reportRunByRow.createCell(2);
//            String username = user.getFullname() == null || user.getFullname().trim().isEmpty() ? user.getUsername() : user.getFullname();
////            System.out.println("\n\nuser.getFullname() : " + user.getFullname());
////            System.out.println("user.getUsername() : " + user.getUsername() + "\n\n");
//            reportRunByCell2.setCellValue(username);
//            reportRunByCell2.setCellStyle(boldCellStyle);
//            currentRange = CellRangeAddress.valueOf(("$C$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
//            sheet.addMergedRegion(currentRange); // Merge and Center
//            currentRow++;
//        }

//      Add Report Execution Time
        Row reportRunAtRow = sheet.createRow(currentRow);
        Cell reportRunAtCell1 = reportRunAtRow.createCell(0);
        reportRunAtCell1.setCellValue("Report Executed Time :");
        currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$B$" + (currentRow + 1)).replace(" ", ""));
        sheet.addMergedRegion(currentRange);
        Cell reportRunAtCell2 = reportRunAtRow.createCell(2);
        reportRunAtCell2.setCellValue(dtf.format(new Date()));
        reportRunAtCell2.setCellStyle(boldCellStyle);
        currentRange = CellRangeAddress.valueOf(("$C$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.addMergedRegion(currentRange);
        currentRow++;

//      Add Space Row Report Title Row and Next
        sheet.createRow(currentRow).createCell(0);
        currentRange = CellRangeAddress.valueOf(("$A$" + (currentRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        sheet.addMergedRegion(currentRange);
        currentRow++;

        return currentRow;
    }

    private XSSFWorkbook populatePivotAnalysisWorksheets(ReportConfig reportConfig, XSSFWorkbook workbook, XSSFSheet dataSheet, Row headerRow, ReportData reportResult, Map<String, CellStyle> styles, ParameterHolder holder, List<ReportConfigParameter> configParameters) {
        int lastDataRow = headerRow.getRowNum() + reportResult.getData().length + 1;
        int sourceDataColumnCount = reportResult.getColumnNames().length;
        //String lastCellReference = CellReference.convertNumToColString(reportColumnCount);
        String lastSourceDataHeaderMergeCellReference = CellReference.convertNumToColString(sourceDataColumnCount - 1);
        lastSourceDataHeaderMergeCellReference = sourceDataColumnCount <= 2 ? "D" : lastSourceDataHeaderMergeCellReference;
        //CellRangeAddress dataRange = CellRangeAddress.valueOf(("$A$" + (iTitleRow + 1) + ":$" + lastHeaderMergeCellReference + "$" + (currentRow + 1)).replace(" ", ""));
        String sRange = ("A" + (headerRow.getRowNum() + 1) + ":" + lastSourceDataHeaderMergeCellReference + "" + (lastDataRow)).replace(" ", "");

        AreaReference pivotSourceAreaRef = new AreaReference(sRange, SpreadsheetVersion.EXCEL2007);

        List<ReportAnalysisSheet> analysisSheetList = reportAnalysisService.getReportAnalysisSheetList(reportConfig);
        List<String> reportConfigColumnList = Arrays.asList(reportResult.getColumnNames());

        reportConfigColumnList.forEach(c -> {
            /* Trim column names to remove posible leading or trailing spaces. */
            reportConfigColumnList.set(reportConfigColumnList.indexOf(c), c.trim());
            // System.out.println("report config column : " + c);
        });

        int sourceDataRowCount = pivotSourceAreaRef.getLastCell().getRow() - pivotSourceAreaRef.getFirstCell().getRow();
        int sourceDataColCount = pivotSourceAreaRef.getLastCell().getCol() - pivotSourceAreaRef.getFirstCell().getCol();

        DataFormat format = workbook.createDataFormat();

        analysisSheetList.stream().map(analySheet -> {
            //CTPivotFields pivotFields;
            XSSFSheet pivotSheet = workbook.createSheet(analySheet.getName());
            //sheet.protectSheet("Password1");
            PrintSetup printSetup = pivotSheet.getPrintSetup();
            printSetup.setLandscape(true);
            pivotSheet.setFitToPage(true);
            pivotSheet.setHorizontallyCenter(true);

            List<ReportAnalysisColumn> analysisSheetColumnList = analySheet.getAnalysisColumnList();

            long pivotFieldsCount = analysisSheetColumnList.stream().filter(p
                    -> p.getAnalysisSector() == ReportColumnAnalysisSector.Rows
                    || p.getAnalysisSector() == ReportColumnAnalysisSector.Columns
                    || p.getAnalysisSector() == ReportColumnAnalysisSector.Data
            ).count();

            pivotFieldsCount = pivotFieldsCount <= 5 ? 5 : pivotFieldsCount;
            //  CellReference pivotTargetCellRef = new CellReference("A" + (1 + pivotFieldsCount));
            int reportColumnCount = (int) pivotFieldsCount;
            // String lastCellReference = CellReference.convertNumToColString(reportColumnCount);
            String lastHeaderMergeCellReference = CellReference.convertNumToColString(reportColumnCount);
            lastHeaderMergeCellReference = reportColumnCount <= 4 ? "D" : lastHeaderMergeCellReference;
            //  CellReference pivotTargetCellRef = new CellReference("A" + (1 + pivotFieldsCount));

            int pivotStartPosition = setSheetHeaders(pivotSheet, "Analysis " + pivotSheet.getSheetName(), 0, reportConfig, styles, lastHeaderMergeCellReference, holder, configParameters);
            /* Detemines the start position of the pivot table to accomodate the number of filter columns */
            long pivotFiltersCount = analysisSheetColumnList.stream().filter(p -> p.getAnalysisSector() == ReportColumnAnalysisSector.Filters).count();
            CellReference pivotTargetCellRef = new CellReference("A" + (1 + pivotFiltersCount + (pivotStartPosition + 1)));
            XSSFPivotTable pivotTable = pivotSheet.createPivotTable(pivotSourceAreaRef, pivotTargetCellRef, dataSheet);
            /* Give Pivot Fields Column Names Corrsponding name from the available list */
            for (CTPivotField pivotField : pivotTable.getCTPivotTableDefinition().getPivotFields().getPivotFieldList()) {
                int fieldIndex = pivotTable.getCTPivotTableDefinition().getPivotFields().getPivotFieldList().indexOf(pivotField);

                if (fieldIndex >= 0 && fieldIndex < analysisSheetColumnList.size()) {
                    ReportAnalysisColumn column = analysisSheetColumnList.get(fieldIndex);
                    String columnName = column.getName();
                    pivotField.setName(columnName);
                }
            }
            /* Define Column Display Sort Order*/
            Comparator<ReportAnalysisColumn> analysisColumnSortComparator = (ReportAnalysisColumn c1, ReportAnalysisColumn c2) -> c1.getPosition() < c2.getPosition() ? -1 : (c1.getPosition() > c2.getPosition() ? 1 : 0);
            //pivotFields = pivotTable.getCTPivotTableDefinition().getPivotFields();
            List<ReportAnalysisColumn> pivotRowsList = analysisSheetColumnList.stream().filter(p -> p.getAnalysisSector() == ReportColumnAnalysisSector.Rows).collect(Collectors.toList());
            pivotRowsList.sort(analysisColumnSortComparator);
            for (ReportAnalysisColumn column : pivotRowsList) {
                if (reportConfigColumnList.stream().anyMatch(c -> c.trim().toLowerCase().equals(column.getName().trim().toLowerCase()))) {
                    int columnIndex = reportConfigColumnList.indexOf(column.getName().trim());
                    pivotTable.addRowLabel(columnIndex);
                }
            }
            CTPivotFields pivotFields = pivotTable.getCTPivotTableDefinition().getPivotFields();
            List<ReportAnalysisColumn> pivotColumnsList = analysisSheetColumnList.stream().filter(p -> p.getAnalysisSector() == ReportColumnAnalysisSector.Columns).collect(Collectors.toList());
            pivotColumnsList.sort(analysisColumnSortComparator);
            for (ReportAnalysisColumn column : pivotColumnsList) {
                if (reportConfigColumnList.stream().anyMatch(c -> c.trim().toLowerCase().equals(column.getName().trim().toLowerCase()))) {
                    int columnIndex = reportConfigColumnList.indexOf(column.getName().trim());
                    if (columnIndex <= sourceDataColCount) {
                        addPivotColumn(pivotTable, pivotFields, column, columnIndex, sourceDataRowCount);
                    }
                }
            }
            List<ReportAnalysisColumn> pivotDataList = analysisSheetColumnList.stream().filter(p -> p.getAnalysisSector() == ReportColumnAnalysisSector.Data).collect(Collectors.toList());
            pivotDataList.sort(analysisColumnSortComparator);
            //pivotDataList.forEach(column -> {
            for (ReportAnalysisColumn column : pivotDataList) {
                if (reportConfigColumnList.stream().anyMatch(c -> c.trim().toLowerCase().equals(column.getName().trim().toLowerCase()))) {
                    int columnIndex = reportConfigColumnList.indexOf(column.getName().trim());
                    if (columnIndex <= sourceDataColCount) {
                        //System.out.println("column.getDisplayName() : " + column.getDisplayName());
                        pivotTable.addColumnLabel(column.getAggregateFunction(), columnIndex, column.getDisplayName());

                        if (format.getFormat(column.getNumberFormat().getValue()) > 0) {
                            setFormatDataField(pivotTable, columnIndex, format.getFormat(column.getNumberFormat().getValue()));
                        }
                    }
                }
            }
            /* Report Filters Should be added Last  */
            List<ReportAnalysisColumn> pivotFiltersList = analysisSheetColumnList.stream().filter(p -> p.getAnalysisSector() == ReportColumnAnalysisSector.Filters).collect(Collectors.toList());
            pivotFiltersList.sort(analysisColumnSortComparator);
            for (ReportAnalysisColumn column : pivotFiltersList) {
                if (reportConfigColumnList.stream().anyMatch(c -> c.trim().toLowerCase().equals(column.getName().trim().toLowerCase()))) {
                    int columnIndex = reportConfigColumnList.indexOf(column.getName().trim());
                    pivotTable = addPivotFilter(pivotTable, column, columnIndex, sourceDataRowCount);
                }
            }
            return pivotTable;
        }).map((pivotTable) -> {
            //Set the default style for the pivot table
            CTPivotTableStyle pivotTableStyle = pivotTable.getCTPivotTableDefinition().getPivotTableStyleInfo();
            if (pivotTableStyle == null) {
                pivotTableStyle = pivotTable.getCTPivotTableDefinition().addNewPivotTableStyleInfo();
            }
            return pivotTableStyle;
        }).map((pivotTableStyle) -> {
            //pivotTableStyle.setName("PivotStyleMedium4");
            pivotTableStyle.setName("PivotStyleLight13");
            return pivotTableStyle;
        }).map((pivotTableStyle) -> {
            pivotTableStyle.setShowLastColumn(true);
            return pivotTableStyle;
        }).map((pivotTableStyle) -> {
            pivotTableStyle.setShowColStripes(false);
            return pivotTableStyle;
        }).map((pivotTableStyle) -> {
            pivotTableStyle.setShowRowStripes(true);
            return pivotTableStyle;
        }).map((pivotTableStyle) -> {
            pivotTableStyle.setShowColHeaders(true);
            return pivotTableStyle;
        }).forEachOrdered((pivotTableStyle) -> {
            pivotTableStyle.setShowRowHeaders(true);
        });

        return workbook;
    }

    public XSSFPivotTable addPivotFilter(XSSFPivotTable pivotTable, ReportAnalysisColumn column, int columnIndex, int sourceDataRowCount) {

        CTPageField ctPageField = addCustomReportFilter(pivotTable, column, columnIndex, sourceDataRowCount);

        /* Filter Page Field Based On Filter Value */
        if (column.getFilterValue() != null && !column.getFilterValue().trim().equals("")) {
            List<String> valuesForFiltering = GetFieldValuesForFiltering(pivotTable, columnIndex);

            final String valuesToFilterOn = column.getFilterValue();

            if (checkIfStringExistsInList(valuesForFiltering, valuesToFilterOn)) {
                String value = valuesForFiltering.stream().filter(l -> l.trim().equalsIgnoreCase(valuesToFilterOn.trim())).findAny().get();
                final int selectedIndex = valuesForFiltering.indexOf(value);

                if (valuesToFilterOn.length() >= 2 && !valuesToFilterOn.trim().equals("") && selectedIndex >= 0) {
                    /*
                        Apache poi adds pivot field items of type "default" (<item t="default"/>) here.
                        This is because there are many rows  and, because they don't have a look at the data,
                        they are assuming max different values. This is fine because Excel will rebuild its pivot cache while opening.
                        But if we want preselect items, then this is not fine. Then we must know what items there are that can be preselected.
                        So we need at least as much items as we want preselecting as numbered items: <item x="0"/><item x="1"/><item x="2"/>...
                        And we must build a cache definition which has shared elements for those items.
                     */
                    CTPivotField pivotField = pivotTable.getCTPivotTableDefinition().getPivotFields().getPivotFieldArray(columnIndex);
                    Optional.ofNullable(pivotField.getItems())
                            .ifPresent(items -> {
                                for (int i = 0; i < items.sizeOfItemArray(); i++) {
                                    if (items.getItemArray(i).isSetT()) {
                                        items.getItemArray(i).unsetT();
                                    }
                                    items.getItemArray(i).setX((long) i);
                                }

                                items.setCount(items.sizeOfItemArray());
                                Optional.ofNullable(pivotTable.getPivotCacheDefinition()
                                        .getCTPivotCacheDefinition().getCacheFields())
                                        .map(CTCacheFields::getCacheFieldArray)
                                        .ifPresent(ctCacheFlds
                                                -> Optional.ofNullable(ctCacheFlds[columnIndex])
                                                .map(CTCacheField::getSharedItems)
                                                .ifPresent(ctSharedItms -> {

                                                    valuesForFiltering.forEach(v -> {
                                                        ctSharedItms.addNewS().setV(v);
                                                    });
                                                }));

                                ctPageField.setItem(selectedIndex);
                            });
                }
            }
        }

        return pivotTable;
    }

    public CTPageField addCustomReportFilter(XSSFPivotTable pivotTable, ReportAnalysisColumn column, int columnIndex, int sourceDataRowCount) {

        CTPivotFields pivotFields = pivotTable.getCTPivotTableDefinition().getPivotFields();

        CTPivotField pivotField = CTPivotField.Factory.newInstance();
        CTItems items = pivotField.addNewItems();

        pivotField.setAxis(STAxis.AXIS_PAGE);
        pivotField.setShowAll(false);
        pivotField.setName(column.getName());

        for (int i = 0; i <= sourceDataRowCount; i++) {
            CTItem item = items.addNewItem();
            item.setT(STItemType.DEFAULT);
        }

        items.setCount(items.sizeOfItemArray());
        pivotFields.setPivotFieldArray(columnIndex, pivotField);

        CTPageFields pageFields;
        if (pivotTable.getCTPivotTableDefinition().getPageFields() != null) {
            pageFields = pivotTable.getCTPivotTableDefinition().getPageFields();
            //Another filter has already been created
            pivotTable.getCTPivotTableDefinition().setMultipleFieldFilters(true);
        } else {
            pageFields = pivotTable.getCTPivotTableDefinition().addNewPageFields();
        }
        CTPageField pageField = pageFields.addNewPageField();
        pageField.setHier(-1);
        pageField.setFld(columnIndex);

        pageFields.setCount(pageFields.sizeOfPageFieldArray());
        pivotTable.getCTPivotTableDefinition().getLocation().setColPageCount(pageFields.getCount());

        return pageField;
    }

    private List<String> GetFieldValuesForFiltering(XSSFPivotTable pivotTable, int columnIndex) {
        /* Custom Functionality To Get Field Values For Filtering */

        List<String> fieldValueList = new ArrayList<>();
        AreaReference pivotArea = pivotTable.getPivotCacheDefinition().getPivotArea(pivotTable.getDataSheet().getWorkbook());

        Sheet dataSheet = pivotTable.getDataSheet();
        int firstSoureRow = pivotArea.getFirstCell().getRow();
        int lastSourceRow = pivotArea.getLastCell().getRow();

        for (int r = firstSoureRow + 1; r <= lastSourceRow; r++) {
            String cVal = "";
            try {
                Row row = dataSheet.getRow(r);
                Cell cell = row.getCell(columnIndex);

                if (cell != null) {
                    cVal = cell.getStringCellValue();
                }
            } catch (Exception e) {
            }

            // boolean valExists  ;
            boolean valExists = checkIfStringExistsInList(fieldValueList, cVal);

            if (!cVal.trim().equals("") && !valExists) {
                fieldValueList.add(cVal);
            }
        }
        return fieldValueList;
    }

    private boolean checkIfStringExistsInList(List<String> li, String val) {
        return li.stream().anyMatch(l -> l.trim().equalsIgnoreCase(val.trim()));
    }

    private XSSFPivotTable addPivotColumn(XSSFPivotTable pivotTable, CTPivotFields pivotFields, ReportAnalysisColumn column, int columnIndex, int sourceDataRowCount) {
        CTPivotField pivotField = CTPivotField.Factory.newInstance();
        CTItems items = pivotField.addNewItems();

        pivotField.setAxis(STAxis.AXIS_COL);
        pivotField.setShowAll(false);
        pivotField.setSumSubtotal(column.isShowSubTotal());
        for (int i = 0; i <= sourceDataRowCount; i++) {
            items.addNewItem().setT(STItemType.DEFAULT);
        }
        items.setCount(items.sizeOfItemArray());
        pivotFields.setPivotFieldArray(columnIndex, pivotField);

        CTColFields colFields;
        if (pivotTable.getCTPivotTableDefinition().getColFields() != null) {
            colFields = pivotTable.getCTPivotTableDefinition().getColFields();
        } else {
            colFields = pivotTable.getCTPivotTableDefinition().addNewColFields();
        }

        colFields.addNewField().setX(columnIndex);
        colFields.setCount(colFields.sizeOfFieldArray());

        return pivotTable;
    }

    private XSSFPivotTable setFormatPivotField(XSSFPivotTable pivotTable, long columnIndex, Integer numFmtId) {
        Optional.ofNullable(pivotTable
                .getCTPivotTableDefinition()
                .getPivotFields())
                .map(pivotFields -> pivotFields
                .getPivotFieldArray((int) columnIndex))
                .ifPresent(pivotField -> pivotField
                .setNumFmtId(numFmtId));
        return pivotTable;
    }

    private XSSFPivotTable setFormatDataField(XSSFPivotTable pivotTable, long fieldIndex, long numFmtId) {
        Optional.ofNullable(pivotTable
                .getCTPivotTableDefinition()
                .getDataFields())
                .map(CTDataFields::getDataFieldList)
                .map(List::stream)
                .ifPresent(stream -> stream
                .filter(dataField -> dataField.getFld() == fieldIndex)
                .findFirst()
                .ifPresent(dataField -> dataField.setNumFmtId(numFmtId)));
        return pivotTable;
    }
  
    private String getDataValue(ParameterHolder holder, String parameter) {
        String value = "";
        switch (parameter) {
            case "parameter1":
                value = holder.getParameter1();
        }
        switch (parameter) {
            case "parameter2":
                value = holder.getParameter2();
        }
        switch (parameter) {
            case "parameter3":
                value = holder.getParameter3();
        }
        switch (parameter) {
            case "parameter4":
                value = holder.getParameter4();
        }
        switch (parameter) {
            case "parameter5":
                value = holder.getParameter5();
        }
        switch (parameter) {
            case "parameter6":
                value = holder.getParameter6();
        }
        return value;
    }

}
