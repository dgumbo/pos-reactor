package zw.co.hisolutions.pos.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author dgumbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemHolder { 
  private StockItem product  ;
  private String batchNumber;
  private int quantity ;  
}
