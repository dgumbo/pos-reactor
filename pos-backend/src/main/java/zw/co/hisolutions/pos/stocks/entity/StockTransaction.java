package zw.co.hisolutions.pos.stocks.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
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
@Table(name = "stock_transaction")
public class StockTransaction extends BaseEntity implements Cloneable { 
    
    @OneToMany(targetEntity = StockTransactionLine.class, cascade = CascadeType.ALL)
    private List<StockTransactionLine> stockTransactionLines;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private StockTransactionType stockTransactionType;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date transactionTime  ; 

    @Enumerated(EnumType.STRING)
    @NotNull
    private StockTransactionStatus stockTransactionStatus = StockTransactionStatus.CREATED;
    
    @Lob
    @Column(name = "remarks")
    private String remarks; 
    
    @Column(name = "total_quantity", nullable = false)
    private long totalQuantity;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime = new Date();
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime = new Date();  
    
    @Override
    public String toString() {
        return "StockTransaction{" + "id=" + id + ", transactionTime=" + transactionTime + ", activeStatus=" + activeStatus + ", stockTransactionLines =" + stockTransactionLines + '}';
    } 
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
    
    @Override
    public StockTransaction clone()  {
        try {
            StockTransaction t = (StockTransaction) super.clone();
            t.setStockTransactionStatus(StockTransactionStatus.CREATED);
            t.setStockTransactionType(StockTransactionType.REVERSAL);
            return t;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
