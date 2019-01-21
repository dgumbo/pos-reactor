package zw.co.hisolutions.pos.reports.service;

import java.util.List;
import zw.co.hisolutions.pos.common.service.BasicListService; 
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;

public interface ReportAnalysisService extends BasicListService<ReportAnalysisSheet, ReportAnalysisColumn> {

    ReportAnalysisSheet getSheetByNameAndReportConfig(String name, Long reportConfigId); 
    
    List<ReportAnalysisSheet> getReportAnalysisSheetList(Long reportConfigId);

    ReportAnalysisColumn getReportAnalysisColumnByNameAndReportAnalysisSheet(String analysisColumnName, ReportAnalysisSheet reportAnalysisSheet);

    List<ReportAnalysisColumn> getReportAnalysisColumnsByReportAnalysisSheet(ReportAnalysisSheet reportAnalysisSheet);
 
    ReportAnalysisSheet getReportAnalysisSheetById(Long sheetId); 

    ReportConfig getReportConfigByID(Long id); 

    ReportAnalysisSheet addNewReportAnalysisSheet(Long reportConfigId, String sheetName);

    List<ReportAnalysisSheet> getReportAnalysisSheetList(ReportConfig reportConfig);

    //String saveReportAnalysisSheet(ReportAnalysisSheet reportAnalysisSheet, List<ReportAnalysisColumn> analysisColumnList); 

    List<ReportAnalysisColumn> getReportAnalysisColumnsByReportAnalysisSheet(Long reportConfigId, String reportSheetName);

    List<ReportAnalysisColumn> getReportAnalysisColumnsForNewSheet(Long reportConfigId);

    List<ReportAnalysisSheet> getReportAnalysisSheetDisabledList(Long reportConfigId);
}
