package zw.co.hisolutions.pos.stocks.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.stocks.api.CurrentStockController;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockStatus;
import zw.co.hisolutions.pos.stocks.entity.dao.CurrentStockDao;
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
import zw.co.hisolutions.pos.stocks.service.StockItemService;

@Service
public class CurrentStockServiceImpl implements CurrentStockService {

    final CurrentStockDao currentStockDao;
    final StockItemService stockItemService;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CurrentStockServiceImpl(CurrentStockDao currentStockDao, StockItemService productService) {
        this.currentStockDao = currentStockDao;
        this.stockItemService = productService;
    }

    @Override
    public CurrentStock create(CurrentStock currentStock) {
        if (currentStock.getStockStatus() == StockStatus.AVAILABLE) {
            List<CurrentStock> availableCurrentStockList = currentStockDao.getAllByStockItemAndBatchNumberAndStockStatus(currentStock.getStockItem(), currentStock.getBatchNumber(), StockStatus.AVAILABLE);

            if (availableCurrentStockList != null && !availableCurrentStockList.isEmpty()) {
                throw new UnsupportedOperationException("Currenct Stock Already Exists, Please Revise Workflow"); //To change body of generated methods, choose Tools | Templates.
            }
        }

        return currentStockDao.save(currentStock);
    }

    @Override
    public List<StockItem> getAllStock() {
        List<StockItem> productList = stockItemService.findAll();
        productList.stream().forEach(product -> setAvailableStock(product));
        return productList;
    }

    private void setAvailableStock(StockItem product) {
        List<CurrentStock> currentStockList = getAllByStockItemAndStockStatus(product, StockStatus.AVAILABLE);
        currentStockList = currentStockList == null ? new ArrayList() : currentStockList;
//        System.out.println("\nproduct : " + product + "\n");
        if (product != null) {
            product.setCurrentStockList(currentStockList);
        }
    }

    @Override
    public List<CurrentStock> getAllActive() {
        return currentStockDao.getAllByActiveStatus(true);
    }

    @Override
    public List<CurrentStock> getAllWithAvailableStock() {
        return currentStockDao.getAllByActiveStatusAndStockStatusAndQuantityGreaterThan(true, StockStatus.AVAILABLE, 0L);
    }

    @Override
    public CurrentStock getAllByStockItemAndBatchNumberAndConsignmentAndStockStatus(StockItem product, String batch_number, boolean consignment_status, StockStatus stock_status) {
        return currentStockDao.getAllByStockItemAndBatchNumberAndConsignmentAndStockStatus(product, batch_number, consignment_status, stock_status);
    }

    @Override
    public List<CurrentStock> getAllByStockItemAndStockStatus(StockItem product, StockStatus stockStatus) {
        List<CurrentStock> csl = currentStockDao.getAllByStockItemAndStockStatus(product, stockStatus);
        return csl == null ? new ArrayList() : csl;
    }

    @Override
    public JpaRepository<CurrentStock, Long> getDao() {
        return currentStockDao;
    }

    @Override
    public Class getController() {
        return CurrentStockController.class;
    }

    @Override
    public CurrentStock setProductCurrentStock(long stockItemId, long quantity) {
        String batchNumber = "BN";
        StockItem stockItem = stockItemService.find(stockItemId);

        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse("2099-12-31");
        } catch (ParseException ex) {
            Logger.getLogger(StockReceiveServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        CurrentStock oldSplitStock = null;

        if (stockItem != null) {
            List<CurrentStock> productAvailableStocksByBatch = getAllByStockItemAndStockStatus(stockItem, StockStatus.AVAILABLE);

            Optional<CurrentStock> batchAvailableStockSearch = productAvailableStocksByBatch
                    .stream()
                    .filter(as -> (as.getBatchNumber() == null ? batchNumber == null : as.getBatchNumber().equals(batchNumber)))
                    .findAny();

            if (!batchAvailableStockSearch.isPresent()) {
                oldSplitStock = new CurrentStock();
                oldSplitStock.setStockStatus(StockStatus.AVAILABLE);
                oldSplitStock.setStockItem(stockItem);
                oldSplitStock.setQuantity(quantity);
                oldSplitStock.setBatchNumber(batchNumber);
                oldSplitStock.setConsignment(false);
//            oldSplitStock.setUnitCost( );
                oldSplitStock.setCashOnly(false);
                oldSplitStock.setExpiryDate(expiryDate);

                oldSplitStock = create(oldSplitStock);
            }

        }

        return oldSplitStock;
    }
}
