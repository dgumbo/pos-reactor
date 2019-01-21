
package zw.co.hisolutions.pos.reports.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;


@Repository
public interface ReportConfigDao extends JpaRepository<ReportConfig, Long>{
    ReportConfig findByActiveStatusTrue();

    public List<ReportConfig> findByNameAndActiveStatusTrue(String name);
}
