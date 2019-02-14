package zw.co.hisolutions.pos.stocks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author denzil
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class MinimumOrderQuantity extends BaseEntity {
   
    @JoinColumn(name = "stock_item_id", nullable = false, unique = true) 
    @OneToOne(targetEntity = StockItem.class)
    private StockItem stockItem; 
    
    @Min(1)
    @Column(name = "min_order_quantity", nullable = false) 
    private long minOrderQuantity;  
    
    @Min(1)
    @Column(name = "max_order_quantity", nullable = false) 
    private long maxOrderQuantity;  

    @Column(name = "description")
    private String description;
}
