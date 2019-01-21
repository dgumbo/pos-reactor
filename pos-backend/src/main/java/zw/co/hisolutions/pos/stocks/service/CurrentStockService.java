package zw.co.hisolutions.pos.stocks.service;

import java.util.List;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockStatus;

/**
 *
 * @author dgumbo
 */
public interface CurrentStockService extends GenericService<CurrentStock, Long> {
  
    List<CurrentStock> getAllActive();

    List<CurrentStock> getAllWithAvailableStock();

    CurrentStock getAllByStockItemAndBatchNumberAndConsignmentAndStockStatus(StockItem product, String batch_number, boolean consignment_status, StockStatus stock_status);
  
    List<CurrentStock> getAllByStockItemAndStockStatus(StockItem product, StockStatus stockStatus);

    List<StockItem> getAllStock();

    public CurrentStock setProductCurrentStock(long stockItemId, long quantity);
}
