package zw.co.hisolutions.pos.reports.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
class ReportCategory extends BaseEntity {
    
    private String name;  
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    @OneToMany(targetEntity=ReportSubCategory.class, cascade=CascadeType.ALL )
    @OrderColumn
    private List<ReportSubCategory> reportSubCategoryList;
}
