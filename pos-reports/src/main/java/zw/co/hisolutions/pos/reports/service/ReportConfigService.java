package zw.co.hisolutions.pos.reports.service;

import java.util.List; 
import zw.co.hisolutions.pos.common.service.BasicListService;
//import zw.co.hisolutions.pos.common.service.BasicListService;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;

public interface ReportConfigService extends BasicListService<ReportConfig, ReportConfigParameter> {

    List<ReportConfigParameter> getAllReportConfigParameters();

    ReportConfig getByName(String name);

    List<ReportConfigParameter> getParametersByReportConfig(ReportConfig reportConfig, ParameterHolder holder);

    List<ReportConfigParameter> getParametersByReportConfig(ReportConfig reportConfig);
    List<ReportConfigParameter> getParametersByReportConfig(Long reportConfigId);
    List<ReportConfigParameter> getParametersByReportConfig(String reportConfigName); 

    List< String> getReportConfigParameterOptions( Long reportConfigParameterId);

    public ReportConfig getByID(Long reportConfigId);
}
