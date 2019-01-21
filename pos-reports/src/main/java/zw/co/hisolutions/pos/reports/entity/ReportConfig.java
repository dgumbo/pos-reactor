package zw.co.hisolutions.pos.reports.entity;
 
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient; 
import javax.validation.constraints.NotNull;
import lombok.Data; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;
 

@Entity
@Data
@Table(name="report_config", indexes = {@Index(name = "idx_nci_u_report_config_name", unique = true, columnList = "name")})
public class ReportConfig extends BaseEntity {
    
    @ManyToOne 
    private ReportSubCategory reportSubCategory;
    
    @NotNull(message="Report Name is Mandatory")
    @Column(name="name",length = 50)
    private String name;
    
    @Lob
    @Column(name="native_query")
    private String nativeQuery;
    
    @Lob
    @Column(name="report_columns")
    private String columns; 
     
    @Transient
    private List<ReportConfigParameter> reportConfigParameters;
     
    @Transient
    private List<ReportAnalysisSheet> reportAnalysisPages;
      
    public List<ReportAnalysisSheet> getReportAnalysisSheets(){
        return reportAnalysisPages ; 
    };
    
    public void getReportAnalysisSheets(List<ReportAnalysisSheet> reportAnalysisSheets){
        this. reportAnalysisPages = reportAnalysisSheets ; 
    };
     
    @Override
    public boolean isActiveStatus(){
        return activeStatus;
    }
    
    @Override
    public String toString() {
        return "ReportConfig{" + "id=" + id + ", name=" + name + ", activeStatus=" + activeStatus + '}';
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
