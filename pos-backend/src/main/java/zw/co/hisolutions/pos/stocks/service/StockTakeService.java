package zw.co.hisolutions.pos.stocks.service;

import java.util.List;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockTake;

/**
 *
 * @author dgumbo
 */
public interface StockTakeService extends GenericService<StockTake, Long> {

    StockTake partialSaveStockTake(StockTake stockTake);

    StockTake finalizeStockTake(StockTake stockTake);

    List<StockItem> getAllStock();

    StockTake getPendingStockTake();

    StockTake getAnyPendingStock();

}
