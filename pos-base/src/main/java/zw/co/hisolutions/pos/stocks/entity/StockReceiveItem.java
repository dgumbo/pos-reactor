package zw.co.hisolutions.pos.stocks.entity;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "stock_receive_item")
public class StockReceiveItem extends BaseEntity {
    
    @JsonIgnore
    @ManyToOne
    private StockReceive stockReceive;
    
    @JsonIgnore
    public StockReceive getStockReceive(){
        return stockReceive ;
    }
     
    @OneToOne ( targetEntity = StockTransactionLine.class )
    private StockTransactionLine stockTransactionLine ;
  
    @ManyToOne
    @NotNull
    private StockItem product ;
 
    @ManyToOne
    private StockSupplier supplier ;  
     
    @ManyToOne
    private UnitOfMeasure unitOfMeasure;
    
    @Column(name="quantity", nullable = false)
    private int quantity;
    
    @Column(name = "unit_cost", precision = 12, scale = 4, nullable = false )
    private BigDecimal unitCost;
    
    @Column(name="batch_number")
    private String batchNumber;
    
    @Column(name = "weighted_average_cost", precision = 12, scale = 4, updatable = false)
    private BigDecimal weightedAverageCost = BigDecimal.ZERO;
    
    @Column(name = "last_receipt_cost_rate", precision = 12, scale = 4, updatable = false)
    private BigDecimal lastReceiptCostRate = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "StockReceiveItem{" + "id=" + id + ", quantity=" + quantity + " , activeStatus=" + activeStatus + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
