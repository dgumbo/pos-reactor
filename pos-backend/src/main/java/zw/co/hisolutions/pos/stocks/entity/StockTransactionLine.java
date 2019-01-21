package zw.co.hisolutions.pos.stocks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity; 

/**
 *
 * @author dgumbo
 */
@Entity
@Table(name = "stock_transaction_line")
@Data
public class StockTransactionLine extends BaseEntity implements Cloneable {

    @ManyToOne
    @JsonIgnore
    private StockTransaction stockTransaction;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private StockItem product;
 
    @Column(nullable = false) 
    private int quantity;
    
    @Column(name = "unit_cost", precision = 12, scale = 4)
    private BigDecimal unitCost;
 
    public BigDecimal getTotalCost(){
        return (new BigDecimal(quantity)).multiply(unitCost.add(vat)) ;
    }

    private String batchNumber; 

    private Boolean consignment;

    private long freeQuantity;

    @ManyToOne(optional = true)
    @JoinColumn(name = "current_stock_id_transaction_on", referencedColumnName = "id")
    private CurrentStock currentStockTransactedOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "current_stock_id_before", referencedColumnName = "id", nullable = false)
    private CurrentStock currentStockBefore;

    @ManyToOne(optional = false)
    @JoinColumn(name = "current_stock_id_after", referencedColumnName = "id", nullable = false)
    private CurrentStock currentStockAfter; 

    @Column(name = "weighted_average_cost", precision = 12, scale = 4, updatable = false)
    private BigDecimal weightedAverageCost = BigDecimal.ZERO;

    @Column(name = "last_receipt_cost_rate", precision = 12, scale = 4, updatable = false)
    private BigDecimal lastReceiptCostRate = BigDecimal.ZERO;

    @Column(name = "vat", precision = 12, scale = 4)
    private BigDecimal vat = BigDecimal.ZERO;  

    @Column(name = "prev_weighted_average_cost", precision = 12, scale = 4, nullable = true, updatable = false)
    private BigDecimal prevWeightedAverageCost = BigDecimal.ZERO;

    @Column(name = "prev_last_receipt_cost_rate", precision = 12, scale = 4, nullable = true, updatable = false)
    private BigDecimal prevLastReceiptCostRate = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "StockTransactionLine{" + "produt=" + product + ", quantity=" + quantity + '}';
    }

    @PrePersist
    @PreUpdate
    public void init4() {
        consignment = consignment == null ? false : consignment;
        prevWeightedAverageCost = prevWeightedAverageCost == null ? BigDecimal.ZERO : prevWeightedAverageCost;
        prevLastReceiptCostRate = prevLastReceiptCostRate == null ? BigDecimal.ZERO : prevLastReceiptCostRate;
        vat = vat == null ? BigDecimal.ZERO : vat;
        unitCost = unitCost == null ? BigDecimal.ZERO : unitCost;  
        
//        System.err.println("transaction line costs::"
//                + " :" + lastReceiptCostRate + " ::" + weightedAverageCost + "\nProduct:" + product
//                + " :-:" + product.getLastReceiptCostRate() + " ::" + product.getWeightedAverageCost()
//        );
        
        if (!(weightedAverageCost.doubleValue() > 0.00) && product != null) {
            weightedAverageCost = product.getWeightedAverageCost();
        }
        if (!(lastReceiptCostRate.doubleValue() > 0.00) && product != null) {
            lastReceiptCostRate = product.getLastReceiptCostRate();
        }
//        if (!(lastReceiptCostRate.doubleValue() > 0.00) || !(weightedAverageCost.doubleValue() > 0.00)) {
//            throw new RuntimeException("zero transaction cost not acceptable"
//                    + " :" + lastReceiptCostRate + " ::" + weightedAverageCost + "\nProduct:" + product
//            );
//        }
    }

    @PostLoad
    public void init6() {
        consignment = consignment == null ? false : consignment;
        prevWeightedAverageCost = prevWeightedAverageCost == null ? BigDecimal.ZERO : prevWeightedAverageCost;
        prevLastReceiptCostRate = prevLastReceiptCostRate == null ? BigDecimal.ZERO : prevLastReceiptCostRate;
        vat = vat == null ? BigDecimal.ZERO : vat;
        unitCost = unitCost == null ? BigDecimal.ZERO : unitCost;  
//        System.err.println("transaction line costs::"
//                + " :" + lastReceiptCostRate + " ::" + weightedAverageCost + "\nProduct:" + product
//                + " :-:" + product.getLastReceiptCostRate() + " ::" + product.getWeightedAverageCost()
//        );
    }

    @Override
    public StockTransactionLine clone() {
        try {
            return (StockTransactionLine) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

}
