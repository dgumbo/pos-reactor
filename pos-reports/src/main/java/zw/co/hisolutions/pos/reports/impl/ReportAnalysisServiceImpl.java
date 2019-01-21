package zw.co.hisolutions.pos.reports.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zw.co.hisolutions.pos.reports.dao.ReportAnalysisColumnDao;
import zw.co.hisolutions.pos.reports.dao.ReportAnalysisSheetDao;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.service.ReportAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import zw.co.hisolutions.pos.reports.entity.ReportColumnAnalysisSector;
import zw.co.hisolutions.pos.reports.entity.ReportColumnNumberFormat;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;

@Service
@Slf4j
public class ReportAnalysisServiceImpl implements ReportAnalysisService {

    private final ReportAnalysisSheetDao reportAnalysisSheetDao;
    private final ReportAnalysisColumnDao reportAnalysisColumnDao;
    private final ReportConfigService reportConfigService;

    @Autowired
    public ReportAnalysisServiceImpl(ReportConfigService reportConfigService, ReportAnalysisSheetDao reportAnalysisSheetDao, ReportAnalysisColumnDao reportAnalysisColumnDao) {
        this.reportConfigService = reportConfigService;
        this.reportAnalysisSheetDao = reportAnalysisSheetDao;
        this.reportAnalysisColumnDao = reportAnalysisColumnDao;
    }

    @Override
    public JpaRepository<ReportAnalysisSheet, Long> getDao() {
        return reportAnalysisSheetDao;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public ReportAnalysisSheet save(ReportAnalysisSheet reportAnalysisSheet, List<ReportAnalysisColumn> reportAnalysisColumnList) {

        ReportConfig reportConfig = reportAnalysisSheet.getReportConfig();

        List<ReportAnalysisSheet> existingSheets = reportAnalysisSheetDao.getByReportConfigAndActiveStatusTrue(reportConfig);
        reportAnalysisSheet.setPosition(existingSheets == null ? 1L : 1 + existingSheets.size());

        reportAnalysisSheet = reportAnalysisSheetDao.save(reportAnalysisSheet);

        if (reportAnalysisColumnList == null || reportAnalysisColumnList.isEmpty()) {
            reportAnalysisColumnList = new ArrayList<>();

            log.info("Analysis Column list is empty!!!!!");
            System.out.println("\nAnalysis Column list is empty!!!!!\n");
        }

        /* Check if Returned Column List has only entries from Report Config Columns*/
        List<String> reportConfigColumns = Arrays.asList(reportConfig.getColumns().split(","));
        for (ReportAnalysisColumn analysisColumn : reportAnalysisColumnList) {
            String testString = analysisColumn.getName().trim().toLowerCase();
            if (!(reportConfigColumns
                    .stream()
                    .anyMatch(p -> p.trim()
                    .toLowerCase()
                    .equals(testString)
                    ))) {
//                reportConfigColumns.forEach(rac -> {
//                    System.out.println("\nreportConfigColumn : " + rac + ", analysisColumn.getName() : " + testString);
//                    System.out.println("\"" + rac + "\"" + ".equals(\"" + testString + "\") : " + rac.equals(testString));
//                });
                analysisColumn.setActiveStatus(false);
            }
        }

        final int pos = reportAnalysisColumnList.size() + 1;
        for (ReportAnalysisColumn column : reportAnalysisColumnList) {
            column.setReportAnalysisSheet(reportAnalysisSheet);
            if (column.getPosition() == 0) {
                column.setPosition(pos);
            }
            if (column.getNumberFormat() == null) {
                column.setNumberFormat(ReportColumnNumberFormat.None);
            }
            if (column.getAnalysisSector() == null) {
                column.setAnalysisSector(ReportColumnAnalysisSector.None);
            }
            if (column.getAggregateFunction() == null) {
                column.setAggregateFunction(DataConsolidateFunction.SUM);
            }
        }

//        System.out.println("\nrac2.size():" + reportAnalysisColumnList.size());
//        reportAnalysisColumnList.forEach(rac -> {
//            System.out.println("\nrac2.getName():" + rac.getName() + ", rac2.getAnalysisSector():" + rac.getAnalysisSector() + ", rac2.isActiveStatus():" + rac.isActiveStatus());
//        });
//        reportAnalysisColumnList = reportAnalysisColumnDao.save(reportAnalysisColumnList);

        log.info("transaction save");
        reportAnalysisSheet.setAnalysisColumnList(reportAnalysisColumnList);
        return reportAnalysisSheet;
    }

    @Override
    public ReportAnalysisSheet addNewReportAnalysisSheet(Long reportConfigId, String sheetName) {
        ReportConfig reportConfig = getReportConfigByID(reportConfigId);

        Long position = reportAnalysisSheetDao.count() + 1;
        if (sheetName.length() > 50) {
            sheetName = sheetName.substring(0, 49);
        }

        ReportAnalysisSheet analysisSheet = new ReportAnalysisSheet();
        analysisSheet.setReportConfig(reportConfig);
        analysisSheet.setName(sheetName);
        analysisSheet.setDisplayName(sheetName);
        analysisSheet.setPosition(position);
        analysisSheet.setSheetProtected(false);

        return reportAnalysisSheetDao.save(analysisSheet);
    }

    @Override
    public ReportAnalysisSheet getSheetByNameAndReportConfig(String name, Long reportConfigId) {
        ReportConfig reportConfig = getReportConfigByID(reportConfigId);

        ReportAnalysisSheet reportAnalysisSheet = reportAnalysisSheetDao.findByNameAndReportConfig(name, reportConfig);
        List<ReportAnalysisColumn> reportAnalysisSheetColumnList = reportAnalysisColumnDao.getAllByReportAnalysisSheetAndActiveStatusTrue(reportAnalysisSheet);

        if (reportAnalysisSheetColumnList == null) {
            reportAnalysisSheetColumnList = new ArrayList();
        }

        for (ReportAnalysisColumn column : reportAnalysisSheetColumnList) {
            column.setName(column.getName().trim());
            reportAnalysisSheetColumnList.set(reportAnalysisSheetColumnList.indexOf(column), column);
        }

        List<String> columnNameList = Arrays.asList(reportConfig.getColumns().split(","));

        for (String column : columnNameList) {
            if (reportAnalysisSheetColumnList.stream().anyMatch(col -> col.getName().trim().equals(column.trim())) == false) {
                ReportAnalysisColumn newCol = new ReportAnalysisColumn();
                newCol.setAnalysisSector(ReportColumnAnalysisSector.None);
                newCol.setActiveStatus(true);
                newCol.setName(column);
                newCol.setDisplayName(column);
                newCol.setReportAnalysisSheet(reportAnalysisSheet);

                reportAnalysisSheetColumnList.add(newCol);
            }
        }

        reportAnalysisSheet.setAnalysisColumnList(reportAnalysisSheetColumnList);
        return reportAnalysisSheet;
    }

    public static String getCurrDate() {
        String mydate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        mydate = df.format(c.getTime());
        return mydate;
    }

    @Override
    public List<ReportAnalysisSheet> getReportAnalysisSheetList(Long reportConfigId) {
        ReportConfig reportConfig = getReportConfigByID(reportConfigId);
        return getReportAnalysisSheetList(reportConfig);
    }

    @Override
    public List<ReportAnalysisSheet> getReportAnalysisSheetList(ReportConfig reportConfig) {

        List<ReportAnalysisSheet> reportConfigAnalysisSheetList = reportAnalysisSheetDao.getByReportConfigAndActiveStatusTrue(reportConfig);

        reportConfigAnalysisSheetList.forEach(reportAnalysisSheet -> {
            List<ReportAnalysisColumn> columnList = getReportAnalysisColumnsByReportAnalysisSheet(reportAnalysisSheet);

            columnList.forEach(col -> {
                col.setReportAnalysisSheet(null);
            });

            reportAnalysisSheet.setAnalysisColumnList(columnList);
        });

        return reportConfigAnalysisSheetList;
    }

    @Override
    public List<ReportAnalysisSheet> getReportAnalysisSheetDisabledList(Long reportConfigId) {

        List<ReportAnalysisSheet> reportConfigAnalysisSheetList = reportAnalysisSheetDao.getByReportConfigIdAndActiveStatusFalse(reportConfigId);

        reportConfigAnalysisSheetList.forEach(reportAnalysisSheet -> {
            List<ReportAnalysisColumn> columnList = getReportAnalysisColumnsByReportAnalysisSheet(reportAnalysisSheet);

            columnList.forEach(col -> {
                col.setReportAnalysisSheet(null);
            });

            reportAnalysisSheet.setAnalysisColumnList(columnList);
        });

        return reportConfigAnalysisSheetList;
    }

    @Override
    public ReportAnalysisSheet getReportAnalysisSheetById(Long sheetId) {
        ReportAnalysisSheet reportAnalysisSheet = reportAnalysisSheetDao.getOne(sheetId)  ;
        List<ReportAnalysisColumn> columnList = getReportAnalysisColumnsByReportAnalysisSheet(reportAnalysisSheet);
        reportAnalysisSheet.setAnalysisColumnList(columnList);
        return reportAnalysisSheet;
    }

    @Override
    public List<ReportAnalysisColumn> getReportAnalysisColumnsByReportAnalysisSheet(ReportAnalysisSheet reportAnalysisSheet) {
        String columns = reportAnalysisSheet.getReportConfig().getColumns();
        StringTokenizer st = new StringTokenizer(columns, ","); //$NON-NLS-1$
        List<String> columnList = new ArrayList();

        while (st.hasMoreTokens()) {
            String columnName = st.nextToken().trim();
            columnList.add(columnName);
        }

        List<ReportAnalysisColumn> reportAnalysisSheetColumnList = reportAnalysisColumnDao.getByReportAnalysisSheetAndActiveStatusTrue(reportAnalysisSheet);

        if (reportAnalysisSheetColumnList == null) {
            reportAnalysisSheetColumnList = new ArrayList();
        }

        /* Check if Returned Column List has only entries from Report Config Columns*/
        List<Integer> itemsToRemove = new ArrayList();
        for (ReportAnalysisColumn analysisColumn : reportAnalysisSheetColumnList) {
            String testString = analysisColumn.getName().trim().toLowerCase();
            if (!(columnList
                    .stream()
                    .anyMatch(p -> p.trim()
                    .toLowerCase()
                    .equals(testString)
                    ))) {
                System.out.println("columnToRemove : " + analysisColumn.getName() + ", reportAnalysisSheetColumnList.indexOf( analysisColumn) : " + reportAnalysisSheetColumnList.indexOf(analysisColumn));
                itemsToRemove.add(reportAnalysisSheetColumnList.indexOf(analysisColumn));
            }
        }
        for (Integer col : itemsToRemove) {
            reportAnalysisSheetColumnList.remove(col);
        }

        /* Add Missing Column to Returned Column List from Report Config Columns*/
        for (String columnName : columnList) {
            String testString = columnName.trim().toLowerCase();
            if (!(reportAnalysisSheetColumnList
                    .stream()
                    .anyMatch(p -> p
                    .getName()
                    .trim()
                    .toLowerCase()
                    .equals(testString)
                    ))) {
                //System.out.println("columnName : " + columnName);
                ReportAnalysisColumn column = new ReportAnalysisColumn();
                column.setReportAnalysisSheet(reportAnalysisSheet);
                column.setName(columnName);
                column.setDisplayName(columnName);
                column.setActiveStatus(true);
                column.setPosition(reportAnalysisSheetColumnList.size() + 1);
                reportAnalysisSheetColumnList.add(column);
            }
        }

        return reportAnalysisSheetColumnList;
    }

    @Override
    public ReportAnalysisColumn getReportAnalysisColumnByNameAndReportAnalysisSheet(String analysisColumnName, ReportAnalysisSheet reportAnalysisSheet) {
        ReportAnalysisColumn reportAnalysisColumn = reportAnalysisColumnDao.getReportAnalysisColumnByNameAndReportAnalysisSheet(analysisColumnName, reportAnalysisSheet);
        return reportAnalysisColumn;
    }

    @Override
    public List<ReportAnalysisColumn> getReportAnalysisColumnsByReportAnalysisSheet(Long reportConfigId, String reportSheetName) { 
        ReportAnalysisSheet sheet = getSheetByNameAndReportConfig(reportSheetName, reportConfigId);
        
        return getReportAnalysisColumnsByReportAnalysisSheet(sheet) ;
    }

    @Override
    public List<ReportAnalysisColumn> getReportAnalysisColumnsForNewSheet(Long reportConfigId) {
        ReportConfig reportConfig = getReportConfigByID(reportConfigId) ;
        
        String columns = reportConfig.getColumns();
               StringTokenizer st = new StringTokenizer(columns, ","); //$NON-NLS-1$
               List<String> columnList = new ArrayList();

               while (st.hasMoreTokens()) {
                   String columnName = st.nextToken().trim();
                   columnList.add(columnName);
               }

               List<ReportAnalysisColumn> reportAnalysisSheetColumnList = new ArrayList();

               /* Add Missing Column to Returned Column List from Report Config Columns*/
               for (String columnName : columnList) {
                   String testString = columnName.trim().toLowerCase();
                   if (!(reportAnalysisSheetColumnList
                           .stream()
                           .anyMatch(p -> p
                           .getName()
                           .trim()
                           .toLowerCase()
                           .equals(testString)
                           ))) {
                       //System.out.println("columnName : " + columnName);
                       ReportAnalysisColumn column = new ReportAnalysisColumn();
                       column.setName(columnName);
                       column.setDisplayName(columnName);
                       column.setActiveStatus(true);
                       column.setPosition(reportAnalysisSheetColumnList.size() + 1);
                       reportAnalysisSheetColumnList.add(column);
                   }
               }

        return reportAnalysisSheetColumnList;
    }

    @Override
    public ReportConfig getReportConfigByID(Long id) {
        return reportConfigService.getByID(id); // .findOne(id);
    }

}
