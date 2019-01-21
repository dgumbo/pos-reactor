package zw.co.hisolutions.pos.mis.typed.entity;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;

/**
 *
 * @author dgumbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypedParameterMetadata {
    private String position;
    private String name;
    private String title;
    private ReportParameterType type;
}
