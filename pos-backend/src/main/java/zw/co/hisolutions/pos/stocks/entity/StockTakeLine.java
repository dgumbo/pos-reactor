package zw.co.hisolutions.pos.stocks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

@Entity
@Table(name = "stock_take_line")
@Data
public class StockTakeLine extends BaseEntity { 
    @JsonIgnore
    @ManyToOne
    private StockTake stockTake;

    @OneToOne 
    private CurrentStock currentStockBefore; 
    
    @OneToOne 
    private CurrentStock currentStockAfter; 
  
    @ManyToOne
    @NotNull
    private StockItem stockItem ;

    @NotNull
    private int quantity;  
    
    @NotNull
    private String batchNumber;
    
    private String comment;
    
    @OneToOne ( targetEntity = StockTransactionLine.class )
    private StockTransactionLine stockTransactionLine ;
}
