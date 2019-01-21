package zw.co.hisolutions.pos.sxssf.service;
 
import org.apache.poi.xssf.streaming.SXSSFWorkbook; 
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig; 
 
public interface SXSSFReportService {  
    SXSSFWorkbook getWorkBook(ReportConfig config, ParameterHolder holder, boolean parametersSet);
}
