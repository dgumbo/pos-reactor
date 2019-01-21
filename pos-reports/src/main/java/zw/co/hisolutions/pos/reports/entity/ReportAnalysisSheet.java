package zw.co.hisolutions.pos.reports.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "report_analysis_sheet")
public class ReportAnalysisSheet extends BaseEntity {

    @ManyToOne 
    @JsonProperty(access = Access.WRITE_ONLY)
    private ReportConfig reportConfig;

//    @JsonIgnore
//    public ReportConfig getReportConfig() {
//        return this.reportConfig;
//    }

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @Column(name = "position")
    private Long position;

    @Column(name = "sheet_protected")
    private boolean sheetProtected;

    @Transient
    private List<ReportAnalysisColumn> analysisColumnList;

    @Override
    public boolean isActiveStatus() {
        return activeStatus;
    }

    @Override
    public String toString() {
        return "ReportAnalysisConfig{" + "id=" + id + ", name=" + name + ", activeStatus=" + activeStatus + '}';
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

}
