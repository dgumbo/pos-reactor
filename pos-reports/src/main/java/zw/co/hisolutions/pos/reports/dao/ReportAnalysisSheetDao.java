package zw.co.hisolutions.pos.reports.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;


@Repository
public interface ReportAnalysisSheetDao extends JpaRepository<ReportAnalysisSheet, Long>{    

    List<ReportAnalysisSheet> getByReportConfigIdAndActiveStatusTrue(Long id);

    List<ReportAnalysisSheet> getByReportConfigAndActiveStatusTrue(ReportConfig reportConfig);

    List<ReportAnalysisSheet> getByReportConfigIdAndActiveStatusFalse(Long reportConfigId);

    List<ReportAnalysisSheet> getByReportConfigAndActiveStatusFalse(ReportConfig reportConfig);
 
    ReportAnalysisSheet findByName(String sheetName);

    ReportAnalysisSheet findByNameAndReportConfig(String analysisSheetName, ReportConfig reportConfig);
 
}
