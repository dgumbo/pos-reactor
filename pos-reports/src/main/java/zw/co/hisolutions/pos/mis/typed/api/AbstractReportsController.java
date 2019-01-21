package zw.co.hisolutions.pos.mis.typed.api;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import zw.co.hisolutions.pos.reports.entity.ColumnMetadata;
import zw.co.hisolutions.pos.sxssf.service.AbstractObjectXSSFTReportService;

/**
 *
 * @author dgumbo
 */
public abstract class AbstractReportsController<T> {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public abstract AbstractObjectXSSFTReportService<T> getService();

    @GetMapping("/getReportColumns")
    public List<ColumnMetadata> getReportColumns() {

        return getService().getReportColumns();
    }

    public abstract List<ColumnMetadata> getReportParams();

}
