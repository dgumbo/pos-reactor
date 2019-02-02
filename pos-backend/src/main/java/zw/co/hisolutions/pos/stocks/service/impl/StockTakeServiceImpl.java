package zw.co.hisolutions.pos.stocks.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.stocks.api.StockTakeController;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockTake;
import zw.co.hisolutions.pos.stocks.entity.StockTakeLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionStatus;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionType;
import zw.co.hisolutions.pos.stocks.entity.dao.StockTakeDao;
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
import zw.co.hisolutions.pos.stocks.service.StockTakeService;
import zw.co.hisolutions.pos.stocks.service.StockTransactionService;

@Service
@Transactional
public class StockTakeServiceImpl implements StockTakeService {

    private final StockTakeDao stockTakeDao;
    private final StockTransactionService stockTransactionService;
    private final CurrentStockService currentStockService;

    public StockTakeServiceImpl(StockTakeDao stockTakeDao, StockTransactionService stockTransactionService, CurrentStockService currentStockService) {
        this.stockTakeDao = stockTakeDao;
        this.stockTransactionService = stockTransactionService;
        this.currentStockService = currentStockService;
    }

    @Override
    public JpaRepository<StockTake, Long> getDao() {
        return stockTakeDao;
    }

    @Override
    public Class getController() {
        return StockTakeController.class;
    }

    public StockTransaction createStockTakeTransaction(StockTransaction stockTransaction) {
        return stockTransactionService.createStockTakeTransaction(stockTransaction);
    }

    public StockTransactionLine createStockTakeTransactionLine(StockTransaction stockTransaction, StockTakeLine stockTakeLine) {
        return stockTransactionService.createStockTakeTransactionLine(stockTakeLine, stockTransaction);
    }

    @Override
    public StockTake partialSaveStockTake(StockTake stockTake) {
        for (StockTakeLine stkl : stockTake.getStockTakeLines()) {
            stkl.setStockTake(stockTake);
        }

        stockTake.setStockTakeStatus(StockTransactionStatus.PENDING);
        stockTake.setStartTime(new Date());
        stockTake.setStockTakeDate(new Date());
        stockTake = stockTakeDao.save(stockTake);

        return stockTake;
    }

    @Override
    public StockTake finalizeStockTake(StockTake stockTake) {
        StockTransaction stockTransaction = new StockTransaction();
        List<StockTransactionLine> stockTransactionLines = new ArrayList();
        stockTransaction.setStockTransactionLines(stockTransactionLines);
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_TAKE);
        stockTransaction.setEndTime(new Date());
        stockTransaction.setTransactionTime(new Date());

        List<StockTakeLine> stockTakelines = stockTake.getStockTakeLines();
        for (StockTakeLine stkl : stockTakelines) {
            stkl.setStockTake(stockTake);

            StockTransactionLine stl = stockTransactionService.createStockTakeTransactionLine(stkl, stockTransaction);

            stockTransactionLines.add(stl);
            stkl.setStockTransactionLine(stl);
            stkl.setCurrentStockBefore(stl.getCurrentStockBefore());
            stkl.setCurrentStockAfter(stl.getCurrentStockAfter());
        }

        StockTransaction st = stockTransactionService.createReceiptTransaction(stockTransaction);

        stockTake.setEndTime(new Date());
        stockTake.setStockTransaction(st);
        stockTake.setStockTakeStatus(StockTransactionStatus.COMPLETED);
        stockTake = stockTakeDao.save(stockTake);

        return stockTake;
    }

    @Override
    public StockTake create(StockTake data) throws IllegalArgumentException {
        return StockTakeService.super.create(data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StockTake save(StockTake data) throws IllegalArgumentException {
        return StockTakeService.super.save(data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        StockTakeService.super.delete(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StockTake update(long id, StockTake data) throws IllegalArgumentException {
        return StockTakeService.super.update(id, data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StockTake getAnyPendingStock() {
        List<StockTake> pendingList = stockTakeDao.findByStockTakeStatus(StockTransactionStatus.PENDING);

        if (pendingList.isEmpty()) {
            return new StockTake();
        } else {
            return pendingList.get(0);
        }
    }

    @Override
    public List<StockItem> getAllStock() {
        return currentStockService.getAllStock();
    }

    @Override
    public StockTake getPendingStockTake() {
        List<StockTake> pendingStockTakes = stockTakeDao.getByStockTakeStatus(StockTransactionStatus.PENDING);
        return pendingStockTakes.isEmpty() ? null : pendingStockTakes.get(0);
    }
}
