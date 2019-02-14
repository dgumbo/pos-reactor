package zw.co.hisolutions.pos.sell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.common.entity.BaseEntity; 
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "sell_item")
public class SellItem extends BaseEntity { 
     
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    private Sell sell;
       
    @ManyToOne(targetEntity = StockItem.class)
    @NotNull
    private StockItem product;
    
    @OneToOne ( targetEntity = StockTransactionLine.class, optional = false )
    @NotNull
    private StockTransactionLine stockTransactionLine ;
    
    @Column(nullable = false)
    private boolean cashOnly; 
     
    @Column(nullable = false)
    private int quantity;
     
    @Column(nullable = false)
    private BigDecimal unitCost = BigDecimal.ZERO; 
    
    @Column(nullable = false)
    private BigDecimal vat = BigDecimal.ZERO;
    
    @Column(nullable = false)    
    private BigDecimal totalCost = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private BigDecimal vatPercentage = BigDecimal.ZERO;  
}
