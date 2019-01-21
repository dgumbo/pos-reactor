package zw.co.hisolutions.pos.reports.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;  
import zw.co.hisolutions.pos.common.entity.BaseEntity;

@Entity 
@Data
@Table(name = "report_analysis_sheet_column")
public class ReportAnalysisColumn extends BaseEntity {

    @ManyToOne
    @JsonIgnore
    private ReportAnalysisSheet reportAnalysisSheet; 

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @Column(name = "position")
    private int position;

    @Column(name = "filter_value")
    private String filterValue;

    @Column(name = "show_sub_total")
    private boolean showSubTotal = false;
 
    @Enumerated(EnumType.STRING)
    private ReportColumnNumberFormat numberFormat = ReportColumnNumberFormat.None;

    @Enumerated(EnumType.STRING)
    private ReportColumnAnalysisSector analysisSector = ReportColumnAnalysisSector.None ; 
    //private STAxis.Enum analysisSector = null;

    @Enumerated(EnumType.STRING) //DataConsolidateFunction
    private DataConsolidateFunction aggregateFunction = DataConsolidateFunction.SUM;    

    @Override
    public String toString() {
        return "ReportAnalysisColumn {" + "id=" + id + ", name=" + name + ", activeStatus=" + activeStatus + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final BaseEntity other = (BaseEntity) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }
     
    @Override
    public boolean isActiveStatus(){
        return activeStatus;
    } 
}
