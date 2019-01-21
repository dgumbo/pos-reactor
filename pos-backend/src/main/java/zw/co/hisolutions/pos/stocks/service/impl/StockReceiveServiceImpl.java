package zw.co.hisolutions.pos.stocks.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zw.co.hisolutions.pos.stocks.api.StockReceiveController;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockReceive;
import zw.co.hisolutions.pos.stocks.entity.StockReceiveItem;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionStatus;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionType;
import zw.co.hisolutions.pos.stocks.entity.dao.StockReceiveDao;
import zw.co.hisolutions.pos.stocks.service.StockReceiveService;
import zw.co.hisolutions.pos.stocks.service.StockTransactionService;
import zw.co.hisolutions.pos.stocks.service.StockItemService;

@Service 
public class StockReceiveServiceImpl implements StockReceiveService {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final StockReceiveDao stockReceiveDao;
    private final StockItemService productService;
    private final StockTransactionService stockTransactionService;

    public StockReceiveServiceImpl(StockReceiveDao StockReceiveDao,  StockItemService productService, StockTransactionService stockTransactionService) {
        this.stockReceiveDao = StockReceiveDao;
        this.productService = productService;
        this.stockTransactionService = stockTransactionService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public StockReceive receiveStock(StockReceive stockReceive) {
        Date stockReceiveDate = new Date() ;
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setTransactionTime(stockReceiveDate);
        List<StockTransactionLine> stockTransactionLines = new ArrayList();
        stockTransaction.setStockTransactionLines(stockTransactionLines);
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_RECEIVE);

        List<StockReceiveItem> StockReceiveItems = stockReceive.getStockReceiveItems();
        for (StockReceiveItem sri : StockReceiveItems) {
            sri.setStockReceive(stockReceive);

            StockItem product = sri.getProduct();
            StockTransactionLine stl = stockTransactionService.createStockReceiveTransactionLine(sri, stockTransaction);

            stockTransactionLines.add(stl);

            productService.updateLastReceiptCostRate(product, sri.getUnitCost());
            sri.setStockTransactionLine(stl);
        }

        StockTransaction st = stockTransactionService.createReceiptTransaction(stockTransaction);

        stockReceive.setStockTransaction(st);
        stockReceive.setReceiveStatus(StockTransactionStatus.COMPLETED);
        stockReceive.setReceiveDate(stockReceiveDate);
        stockReceive.setReceiveDateTime(stockReceiveDate);
        stockReceive = stockReceiveDao.save(stockReceive);

        return stockReceive;
    }

    @Override
    public StockItem adjustUnitCost(StockTransaction stockTransaction) {
//        return productService.updateLastReceiptCostRate(product , sri.getUnitCost() ) ;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JpaRepository<StockReceive, Long> getDao() {
        return stockReceiveDao;
    }

    @Override
    public Class getController() {
        return StockReceiveController.class;
    }
}
