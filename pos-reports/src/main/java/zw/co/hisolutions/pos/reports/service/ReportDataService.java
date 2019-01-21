package zw.co.hisolutions.pos.reports.service;

import java.util.List;
import zw.co.hisolutions.pos.reports.entity.ColumnMetadata;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.entity.ReportData;

/**
 *
 * @author dgumbo
 */
public interface ReportDataService {

    ReportData getReportResultData(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters);
    
    ReportData getReportPreviewData(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters);

    List<ColumnMetadata> getReportColumnsProperties(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters);
}
