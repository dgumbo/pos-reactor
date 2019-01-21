package zw.co.hisolutions.pos.reports.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;


@Repository
public interface ReportAnalysisColumnDao extends JpaRepository<ReportAnalysisColumn, Long>{    

    public List<ReportAnalysisColumn> getByReportAnalysisSheetIdAndActiveStatusTrue(Long id);

    public List<ReportAnalysisColumn> getByReportAnalysisSheetAndActiveStatusTrue(ReportAnalysisSheet reportAnalysisSheet);

    public List<ReportAnalysisColumn> getAllByReportAnalysisSheetAndActiveStatusTrue(ReportAnalysisSheet reportAnalysisSheet);

    public ReportAnalysisColumn getReportAnalysisColumnByNameAndReportAnalysisSheet(String analysisColumnName, ReportAnalysisSheet reportAnalysisSheet);
 
}
