package zw.co.hisolutions.pos.stocks.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "stock_request_item")
public class StockRequestItem extends BaseEntity {

    @ManyToOne
    private StockRequest stockRequest;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private StockItem product;
    
    @ManyToOne
    private UnitOfMeasure unitOfMeasure;
    
    @Column
    private long quantity;
    
    @Column(name = "weighted_average_cost", precision = 12, scale = 4)
    private BigDecimal weightedAverageCost = BigDecimal.ZERO;
    
    @Column(name = "last_receipt_cost_rate", precision = 12, scale = 4)
    private BigDecimal lastReceiptCostRate = BigDecimal.ZERO;
    
    @Column(name = "cost", precision = 12, scale = 4)
    private BigDecimal cost;
    
    @Transient
    List<CurrentStock> currentStocks = new ArrayList<CurrentStock>();
    
    @Transient
    List<StockItem> products = new ArrayList<StockItem>();
    
    @Transient
    private long issued = 0;
    
    @Transient
    private long remaining = 0;

    @Override
    public String toString() {
        return "StockRequestItem{" + "id=" + id + ", quantity=" + quantity + " , activeStatus=" + activeStatus + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
