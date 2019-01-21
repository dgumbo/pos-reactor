package zw.co.hisolutions.pos.reports.service;

import java.util.List;
import java.util.Map;   
import zw.co.hisolutions.pos.reports.entity.ReportInformation;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;

public interface MisService { 

    ReportInformation getReportInformation(String reportName, ParameterHolder holder);

    List<Object[]> getReportDataNQuery(String reportName, ParameterHolder holder);

    List<Map<String, Object>> getReportDataJDBCTemplate(String reportName, ParameterHolder holder);

    List<ReportConfig> getReportList();

    ReportInformation getReportPreviewInformation(String reportName, ParameterHolder holder); 
}
