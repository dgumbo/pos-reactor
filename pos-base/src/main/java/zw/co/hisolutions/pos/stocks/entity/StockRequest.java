package zw.co.hisolutions.pos.stocks.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient; 
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.hisolutions.pos.common.entity.BaseEntity; 

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "stock_request")
public class StockRequest extends BaseEntity { 
    
    @ManyToOne(optional = true)
    private StockSupplier stockSupplier;  
    
    private String remarks;
    
    @Enumerated(EnumType.STRING)
    private WorkFlowType workFlowType;
    
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date requestDate = new Date();
    
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date closeDate;
    
    @Column(name = "_index")
    private Long index;  
    @Column(name="stock_request_total",precision = 12,scale = 4)  
    private BigDecimal stockRequestTotal;
    
    @Transient
    private boolean editable=false;
    @Transient
    private boolean cancellable=false;
    @Transient
    private String cancelRemarks="";
     
    
    @Transient
    List<StockRequestItem> stockRequestItems = new ArrayList<StockRequestItem>();

    @Transient
    List<String> stockRequestsIdList = new ArrayList<>();

    @Transient
    List<StockSupplier> stockSuppliers = new ArrayList<StockSupplier>();
    
//    @Transient
//    List<Unit> issueUnits = new ArrayList<Unit>();
     

    @Transient
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate = new Date();
    
    @Transient
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate = new Date(); 
 
    
    @Override
    public String toString() {
        return "StockRequest{" + "id=" + id + ", remarks = " + remarks + " , activeStatus=" + activeStatus + '}';
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
        
        final StockRequest other = (StockRequest) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
