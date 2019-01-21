package zw.co.hisolutions.pos.stocks.service;

import zw.co.hisolutions.pos.stocks.entity.StockReceiveItem;
import zw.co.hisolutions.pos.stocks.entity.StockTake;
import zw.co.hisolutions.pos.stocks.entity.StockTakeLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;

/**
 *
 * @author dgumbo
 */
public interface StockTransactionService {

     StockTransaction getByID(Long id);

     StockTransaction createReceiptTransaction(StockTransaction stockTransaction);

    public StockTransaction createSellStockTransaction(StockTransaction stockTransaction);
  
    public StockTransactionLine createStockTakeTransactionLine(StockTakeLine stkl,StockTransaction stockTransaction);

    public StockTransactionLine createStockReceiveTransactionLine(StockReceiveItem sri, StockTransaction stockTransaction);

    public StockTransaction createStockTakeTransaction(StockTransaction stockTransaction);
    
}
