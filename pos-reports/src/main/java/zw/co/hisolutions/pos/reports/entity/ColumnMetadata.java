package zw.co.hisolutions.pos.reports.entity;

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
public class ColumnMetadata {
    private String position;
    private String name;
    private String title;
    private String type;
}
