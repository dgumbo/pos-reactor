package zw.co.hisolutions.pos.stocks.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated; 
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "stock_receive")
public class StockReceive extends BaseEntity {

    @OneToMany(targetEntity = StockReceiveItem.class, cascade = CascadeType.ALL)
    List<StockReceiveItem> StockReceiveItems = new ArrayList<StockReceiveItem>();
     
    @OneToOne ( targetEntity = StockTransaction.class )
    private StockTransaction stockTransaction ;

    private String remarks;
    
    @Column(name = "receive_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StockTransactionStatus receiveStatus;

    @Enumerated(EnumType.STRING)
    private WorkFlowType workFlowType;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date receiveDate  ;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = true)
    private Date receiveDateTime = new Date() ;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = true)
    private Date startTime  ;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = true)
    private Date endTime  ;

    @Column(name = "total_cost", precision = 12, scale = 4)
    private BigDecimal totalCost;

    @Transient
    private boolean editable = false;

    @Transient
    private boolean cancellable = false;

    @Transient
    private String cancelRemarks = "";

    @Override
    public String toString() {
        return "StockReceive {" + "id=" + id + ", totalCost = " + totalCost + ", receiptDateTime = " + receiveDateTime + ", remarks = " + remarks + ", workFlowType = " + workFlowType + ", activeStatus=" + activeStatus + '}';
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        final StockReceive other = (StockReceive) obj;
        if (obj == null || getClass() != obj.getClass() || !Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
