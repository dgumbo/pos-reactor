package zw.co.hisolutions.pos.stocks.service;

import java.math.BigDecimal;
import java.util.List;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;

public interface StockItemService extends GenericService<StockItem, Long> {
 
    StockItem findByName(String name);

    void deleteProduct(StockItem product);

    List<StockItem> findProductByProductCategory(Long productCategoryId);
    List<StockItem> findProductByProductCategory(ProductCategory productCategory); 
 

    StockItem updateLastReceiptCostRate(StockItem product, BigDecimal unitCost);

}
