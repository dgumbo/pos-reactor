package zw.co.hisolutions.pos.stocks.service;

import zw.co.hisolutions.pos.stocks.entity.StockReceiveItem; 
import zw.co.hisolutions.pos.stocks.entity.StockTakeLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;

/**
 *
 * @author dgumbo
 */
public interface StockTransactionService {

     StockTransaction getByID(Long id);

     StockTransaction createStockReceiveTransaction(StockTransaction stockTransaction);

    public StockTransaction createSellStockTransaction(StockTransaction stockTransaction);
  
    public StockTransactionLine createStockTakeTransactionLine(StockTakeLine stkl );

    public StockTransactionLine createStockReceiveTransactionLine(StockReceiveItem sri);

    public StockTransaction createStockTakeTransaction(StockTransaction stockTransaction);
    
}
