package zw.co.hisolutions.pos.reports.entity;
 
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;


@Entity
@Data
@Table(name="report_parameter") 
public class ReportConfigParameter extends BaseEntity {
    
    @ManyToOne 
    private ReportConfig reportConfig;
    
    @Column(name="name",length = 50)
    private String name;
    
    @Column(name="parameter",length = 50)
    private String parameter;
    
    @Enumerated(EnumType.STRING)
    private ReportParameterType parameterType;
    
    @Column(name="column_name",length = 50)
    private String columnName;
    
    @Lob
    @Column(name="select_Sql")
    private String selectSql;
    
    @Column(name="select_value_field",length = 50)
    private String selectValueField;
    
    @Column(name="is_optional")
    private boolean optional=true;
    
    @Transient
    private String htmlElements; 
    
    @Transient
    private List<String> dropDownSelectOptions; 
    
    @Override
    public boolean isActiveStatus(){
        return activeStatus;
    }
    
   
    @Override
    public String toString() {
        return "ReportConfigParameter{" + "id=" + id + ", name=" + name + ", activeStatus=" + activeStatus + '}';
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
