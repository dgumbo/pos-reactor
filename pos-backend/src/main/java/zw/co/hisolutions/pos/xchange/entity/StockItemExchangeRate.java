package zw.co.hisolutions.pos.xchange.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;  
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author dgumbo
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor 
@Table (indexes = {@Index(unique = true, name = "idx_u_nc_global_exchange_rate_stock_item_currency", columnList = "stock_item, currency" )})
public class StockItemExchangeRate extends BaseEntity { 
    
    @Column(name="stock_item", nullable = false)
    private StockItem stockItem ;
    
    @Column(name="currency", nullable = false) 
    private Currency currency ;
    
    @Column(nullable = false)
    private BigDecimal currencyRate ;
    
    @Column(nullable = false)
    private BigDecimal bondRate ; 
    
}

