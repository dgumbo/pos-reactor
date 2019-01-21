package zw.co.hisolutions.pos.reports.entity;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author dgumbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportInformation { 
    List<ColumnMetadata> columnMetadata; 
    List<Map<String, Object>> reportData;
}
