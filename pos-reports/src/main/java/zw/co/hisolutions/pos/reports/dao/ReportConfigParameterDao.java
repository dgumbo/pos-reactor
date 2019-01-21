package zw.co.hisolutions.pos.reports.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;


@Repository
public interface ReportConfigParameterDao extends JpaRepository<ReportConfigParameter, Long>{    

    public List<ReportConfigParameter> getByReportConfigIdAndActiveStatusTrue(Long reportConfigId);

    public List<ReportConfigParameter> getByReportConfigNameAndActiveStatusTrue(String reportConfigName);

    public List<ReportConfigParameter> getReportConfigParametersById(Long reportConfigId);

    public ReportConfigParameter getById(Long reportConfigParameter);
}
