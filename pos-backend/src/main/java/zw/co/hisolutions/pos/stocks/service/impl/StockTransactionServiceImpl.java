package zw.co.hisolutions.pos.stocks.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockReceiveItem;
import zw.co.hisolutions.pos.stocks.entity.StockStatus; 
import zw.co.hisolutions.pos.stocks.entity.StockTakeLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;
import zw.co.hisolutions.pos.stocks.entity.dao.StockTransactionDao;
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
import zw.co.hisolutions.pos.stocks.service.StockTransactionService;
 
@Service
@Transactional
public class StockTransactionServiceImpl implements StockTransactionService { 
    
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

    private final StockTransactionDao stockTransactionDao;
    private final CurrentStockService currentStockService;

    public StockTransactionServiceImpl(StockTransactionDao stockTransactionDao,CurrentStockService currentStockService) {
        this.stockTransactionDao = stockTransactionDao;
        this.currentStockService = currentStockService;
    } 

    @Override
    public StockTransaction getByID(Long id) {
        return stockTransactionDao.getOne(id);
    }

    @Override
    public StockTransaction createStockReceiveTransaction(StockTransaction stockTransaction) {
             stockTransaction
                .getStockTransactionLines()
                .forEach(stl-> stl.setStockTransaction(stockTransaction));
             return stockTransactionDao.save(stockTransaction) ;
    }

    @Override
    public StockTransaction createSellStockTransaction(StockTransaction stockTransaction) {
             stockTransaction
                .getStockTransactionLines()
                .forEach(stl-> stl.setStockTransaction(stockTransaction));
             return stockTransactionDao.save(stockTransaction) ;
    }

    @Override
    public StockTransaction createStockTakeTransaction(StockTransaction stockTransaction) {
        stockTransaction
                .getStockTransactionLines()
                .forEach(stl-> stl.setStockTransaction(stockTransaction));
        return stockTransactionDao.save(stockTransaction) ;
    }

    @Override
    public StockTransactionLine createStockTakeTransactionLine(StockTakeLine stkl  ) {
         Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse("2099-12-31");
        } catch (ParseException ex) {
            Logger.getLogger(StockReceiveServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        StockItem stockItem = stkl.getStockItem();
        List<CurrentStock> productAvailableStocksByBatch = currentStockService.getAllByStockItemAndStockStatus(stockItem, StockStatus.AVAILABLE);

        Optional<CurrentStock> batchAvailableStockSearch = productAvailableStocksByBatch
                .stream()
                .filter(as -> (as.getBatchNumber() == null ? stkl.getBatchNumber() == null : as.getBatchNumber().equals(stkl.getBatchNumber())))
                .findAny();

        CurrentStock oldSplitStock;
        if (batchAvailableStockSearch.isPresent()) {
            oldSplitStock = batchAvailableStockSearch.get();
        } else {
            oldSplitStock = new CurrentStock();
            oldSplitStock.setStockItem(stockItem);
            oldSplitStock.setQuantity(0);
            oldSplitStock.setBatchNumber(stkl.getBatchNumber());
            oldSplitStock.setConsignment(false);
            oldSplitStock.setUnitCost(stkl.getStockItem().getLastReceiptCostRate());
            oldSplitStock.setCashOnly(false);
            oldSplitStock.setExpiryDate(expiryDate);
        }
        oldSplitStock.setStockStatus(StockStatus.SPLIT_ON_STOCK_TAKE);

        CurrentStock stockTransactedOn = oldSplitStock.cloneSplit();
        stockTransactedOn.setQuantity(stkl.getQuantity());
        stockTransactedOn.setStockStatus( StockStatus.STOCK_TAKE_COUNT ); 
        
        CurrentStock newAvailableStock = oldSplitStock.cloneSplit();
        newAvailableStock.setQuantity( stkl.getQuantity());
        newAvailableStock.setStockStatus(StockStatus.AVAILABLE );  
         
        oldSplitStock = currentStockService.update(oldSplitStock.getId(), oldSplitStock);
        stockTransactedOn = currentStockService.create(stockTransactedOn);
        newAvailableStock = currentStockService.create(newAvailableStock);

        StockTransactionLine stl = new StockTransactionLine();
        stl.setProduct(stockItem);
        stl.setQuantity(stkl.getQuantity());
        stl.setBatchNumber(stkl.getBatchNumber());
        stl.setConsignment(newAvailableStock.isConsignment());
        stl.setCurrentStockBefore(oldSplitStock);
        stl.setCurrentStockAfter(newAvailableStock);
        stl.setCurrentStockTransactedOn(stockTransactedOn);
        stl.setUnitCost(stkl.getStockItem().getLastReceiptCostRate());
        stl.setLastReceiptCostRate(stkl.getStockItem().getLastReceiptCostRate());
        stl.setWeightedAverageCost(stkl.getStockItem().getLastReceiptCostRate());
        stl.setPrevLastReceiptCostRate(stockItem.getLastReceiptCostRate());
        stl.setPrevWeightedAverageCost(stkl.getStockItem().getWeightedAverageCost());
        stl.setFreeQuantity(0);
        stl.setVat(BigDecimal.ZERO);
        return stl;
    } 


    @Override
    public StockTransactionLine createStockReceiveTransactionLine(StockReceiveItem sri ) {
         Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse("2099-12-31");
        } catch (ParseException ex) {
            Logger.getLogger(StockReceiveServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        StockItem stockItem = sri.getProduct() ;
        List<CurrentStock> productAvailableStocksByBatch = currentStockService.getAllByStockItemAndStockStatus(stockItem, StockStatus.AVAILABLE);

        Optional<CurrentStock> batchAvailableStockSearch = productAvailableStocksByBatch
                .stream()
                .filter(as -> (as.getBatchNumber() == null ? sri.getBatchNumber() == null : as.getBatchNumber().equals(sri.getBatchNumber())))
                .findAny();

        CurrentStock oldSplitStock;
        if (batchAvailableStockSearch.isPresent()) {
            oldSplitStock = batchAvailableStockSearch.get();
        } else {
            oldSplitStock = new CurrentStock();
            oldSplitStock.setStockItem(stockItem);
            oldSplitStock.setQuantity(0);
            oldSplitStock.setBatchNumber(sri.getBatchNumber());
            oldSplitStock.setConsignment(false);
            oldSplitStock.setUnitCost(sri.getUnitCost());
            oldSplitStock.setCashOnly(false);
            oldSplitStock.setExpiryDate(expiryDate);
        }

        oldSplitStock.setStockStatus(StockStatus.SPLIT_ON_STOCK_RECEIPT);

        CurrentStock stockTransactedOn = oldSplitStock.cloneSplit();
        stockTransactedOn.setQuantity(sri.getQuantity());
        stockTransactedOn.setStockStatus(StockStatus.RECEIVED);
        stockTransactedOn.setUnitCost(sri.getUnitCost());

        CurrentStock newAvailableStock = oldSplitStock.cloneSplit();
        newAvailableStock.setQuantity(oldSplitStock.getQuantity() + sri.getQuantity());
        newAvailableStock.setStockStatus(StockStatus.AVAILABLE);
        newAvailableStock.setExpiryDate(oldSplitStock.getExpiryDate());
        newAvailableStock.setUnitCost(sri.getUnitCost());

        oldSplitStock = currentStockService.create(oldSplitStock);
        stockTransactedOn = currentStockService.create(stockTransactedOn);
        newAvailableStock = currentStockService.create(newAvailableStock);

        StockTransactionLine stl = new StockTransactionLine();
        stl.setProduct(stockItem);
        stl.setQuantity(sri.getQuantity()); 
        stl.setBatchNumber(sri.getBatchNumber());
        stl.setConsignment(newAvailableStock.isConsignment());
        stl.setCurrentStockBefore(oldSplitStock);
        stl.setCurrentStockAfter(newAvailableStock);
        stl.setCurrentStockTransactedOn(stockTransactedOn);
        stl.setUnitCost(sri.getUnitCost());
        stl.setLastReceiptCostRate(sri.getUnitCost());
        stl.setWeightedAverageCost(sri.getUnitCost());
        stl.setPrevLastReceiptCostRate(stockItem.getLastReceiptCostRate());
        stl.setPrevWeightedAverageCost(sri.getWeightedAverageCost());
        stl.setFreeQuantity(0);
        stl.setVat(BigDecimal.ZERO);
        return stl;
    } 

}
