package zw.co.hisolutions.pos.stocks.service;
 
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockReceive;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;

/**
 *
 * @author dgumbo
 */
public interface StockReceiveService extends GenericService<StockReceive, Long> {

    public StockItem adjustUnitCost(StockTransaction stockTransaction);

    public StockReceive receiveStock(StockReceive StockReceive); 
    
}
