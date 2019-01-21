package zw.co.hisolutions.pos.mis.typed.entity;

import zw.co.hisolutions.pos.reports.entity.*;
import java.util.List; 
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
public class MisReportInformation <T> { 
    List<ColumnMetadata> columnMetadata; 
    List<T> reportData;
}
