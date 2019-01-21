package zw.co.hisolutions.pos.sell.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zw.co.hisolutions.pos.backend.entity.CartHolder;
import zw.co.hisolutions.pos.backend.entity.CartItemHolder;
import zw.co.hisolutions.pos.sell.api_controller.SellController;
import zw.co.hisolutions.pos.sell.entity.Receipt;
import zw.co.hisolutions.pos.sell.entity.ReceiptItem;
import zw.co.hisolutions.pos.sell.entity.Sell;
import zw.co.hisolutions.pos.sell.entity.SellItem;
import zw.co.hisolutions.pos.sell.entity.TransactionStatus;
import zw.co.hisolutions.pos.sell.entity.dao.SellDao;
import zw.co.hisolutions.pos.sell.services.ReceiptService;
import zw.co.hisolutions.pos.sell.services.SellService;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockStatus;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionLine;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionType;
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
import zw.co.hisolutions.pos.stocks.service.StockTransactionService;

/**
 *
 * @author dgumbo
 */
@Service
public class SellServiceImpl implements SellService {

    private final SellDao sellDao;
    private final ReceiptService receiptService;
    private final StockTransactionService stockTransactionService;
    private final CurrentStockService currentStockService;

    public SellServiceImpl(SellDao sellDao, ReceiptService receiptService, StockTransactionService stockTransactionService, CurrentStockService currentStockService) {
        this.sellDao = sellDao;
        this.receiptService = receiptService;
        this.stockTransactionService = stockTransactionService;
        this.currentStockService = currentStockService;
    }

    @Override
    public List<Sell> getAllBySearch(Sell sell) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SellItem> getBySellId(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelSell(Sell sell, String reason) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public Sell saveCart(CartHolder cartHolder) {
        if (cartHolder.getItems() == null || cartHolder.getItems().isEmpty()) {
            throw new UnsupportedOperationException("Items Cannot Be Empty"); //To change body of generated methods, choose Tools | Templates.
        }
        if (cartHolder.getPayment().getReceiptItems() == null || cartHolder.getPayment().getReceiptItems().isEmpty()) {
            throw new UnsupportedOperationException("Payment Details Cannot Be Empty"); //To change body of generated methods, choose Tools | Templates.
        }
        checkIfStockAvailableForCartItems(cartHolder.getItems());

        Date sellDate;
        if (cartHolder.isDateSale()) {
            if (cartHolder.getDateSaleDate().after( new Date())){
                throw new UnsupportedOperationException("Cannot Make a future sell"); //To change body of generated methods, choose Tools | Templates.
            }
            
            sellDate = cartHolder.getDateSaleDate();
        } else {
            sellDate = new Date();
        }

        Sell sell = new Sell();
        sell.setSellDate(sellDate);
        sell.setSellDateTime(sellDate);

        List<SellItem> sellItems = new ArrayList();
        sell.setSellItems(sellItems);
        
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setTransactionTime(sellDate);
        
        List<StockTransactionLine> stockTransactionLines = new ArrayList();
        stockTransaction.setStockTransactionLines(stockTransactionLines);
        stockTransaction.setStockTransactionType(StockTransactionType.STOCK_SELL);

        sell.setItemCount(cartHolder.getItemsCount());
        sell.setTransactionStatus(TransactionStatus.COMPLETED);
        BigDecimal totalSellAmount = BigDecimal.ZERO;

        for (CartItemHolder item : cartHolder.getItems()) {
            StockItem product = item.getProduct();
            SellItem sellItem = new SellItem();
            sellItems.add(sellItem);
            sellItem.setSell(sell);
            sellItem.setProduct(product);
            sellItem.setCashOnly(true);
            sellItem.setQuantity(item.getQuantity());
            BigDecimal vat = BigDecimal.ZERO;
            BigDecimal totalCost = product.getBondPrice().multiply(new BigDecimal(item.getQuantity()));
            totalCost = totalCost.add(vat);
            sellItem.setVat(vat);
            sellItem.setTotalCost(totalCost);
            sellItem.setUnitCost(product.getBondPrice());

            totalSellAmount = totalSellAmount.add(sellItem.getTotalCost());

            StockTransactionLine stl = createStockSellTransactionLine(sellItem, stockTransaction, item.getBatchNumber());
            sellItem.setStockTransactionLine(stl);
            stockTransactionLines.add(stl);
        }
        sell.setSellAmount(totalSellAmount);

        stockTransaction = stockTransactionService.createSellStockTransaction(stockTransaction);
        sell.setStockTransaction(stockTransaction);

        Receipt receipt = cartHolder.getPayment(); 

        BigDecimal tenderedAmount = BigDecimal.ZERO;
        for (ReceiptItem ri : receipt.getReceiptItems()) {
            tenderedAmount = tenderedAmount.add(ri.getConvertedAmount());
            ri.setReceipt(receipt);
        }

        BigDecimal changeAmount = tenderedAmount.subtract(totalSellAmount);

        receipt.setTenderedAmount(tenderedAmount);
        receipt.setPaidAmount(totalSellAmount);
        receipt.setChangeAmount(changeAmount);

        receipt.setPayableAmount(totalSellAmount);

        receipt.setReceiptDate(sellDate);
        receipt.setReceiptDateTime(sellDate);

        receipt = receiptService.save(receipt, receipt.getReceiptItems());
        receipt.setSell(sell);

        sell.setReceipt(receipt);
        sell = save(sell);

        return sell;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public Sell save(Sell sell) {

        if (sell.getSellItems() == null || sell.getSellItems().isEmpty()) {
            throw new UnsupportedOperationException("Items Cannot Be Empty"); //To change body of generated methods, choose Tools | Templates.
        }

        sell.getSellItems().stream().forEach(line -> {
            line.setSell(sell);
        });

        Sell savedSell = sellDao.save(sell);

        return savedSell;
    }

    private StockTransactionLine createStockSellTransactionLine(SellItem sellItem, StockTransaction stockTransaction, String batchNumber) {
        StockItem stockItem = sellItem.getProduct();
        List<CurrentStock> productAvailableStocksByBatch = currentStockService.getAllByStockItemAndStockStatus(stockItem, StockStatus.AVAILABLE);

        CurrentStock oldSplitStock = productAvailableStocksByBatch
                .stream()
                .filter(as -> (as.getBatchNumber() == null ? batchNumber == null : as.getBatchNumber().equals(batchNumber)))
                .findAny()
                .get();

        oldSplitStock.setStockStatus(StockStatus.SPLIT_ON_SELL);

        CurrentStock stockTransactedOn = oldSplitStock.cloneSplit();
        stockTransactedOn.setQuantity(sellItem.getQuantity());
        stockTransactedOn.setStockStatus(StockStatus.SOLD);
        stockTransactedOn.setUnitCost(sellItem.getUnitCost());

        CurrentStock newAvailableStock = oldSplitStock.cloneSplit();
        newAvailableStock.setQuantity(oldSplitStock.getQuantity() - sellItem.getQuantity());
        newAvailableStock.setStockStatus(StockStatus.AVAILABLE);
        newAvailableStock.setExpiryDate(oldSplitStock.getExpiryDate());
        newAvailableStock.setUnitCost(sellItem.getUnitCost());

        oldSplitStock = currentStockService.update(oldSplitStock.getId(), oldSplitStock);
        stockTransactedOn = currentStockService.create(stockTransactedOn);
        newAvailableStock = currentStockService.create(newAvailableStock);

        StockTransactionLine stl = new StockTransactionLine();
        stl.setProduct(stockItem);
        stl.setQuantity(sellItem.getQuantity());
        stl.setStockTransaction(stockTransaction);
        stl.setBatchNumber(batchNumber);
        stl.setConsignment(newAvailableStock.isConsignment());
        stl.setCurrentStockBefore(oldSplitStock);
        stl.setCurrentStockAfter(newAvailableStock);
        stl.setCurrentStockTransactedOn(stockTransactedOn);
        stl.setUnitCost(sellItem.getUnitCost());
        stl.setLastReceiptCostRate(sellItem.getUnitCost());
        stl.setWeightedAverageCost(sellItem.getUnitCost());
        stl.setPrevLastReceiptCostRate(stockItem.getLastReceiptCostRate());
        stl.setPrevWeightedAverageCost(sellItem.getProduct().getWeightedAverageCost());
        stl.setFreeQuantity(0);
        stl.setVat(BigDecimal.ZERO);
        return stl;
    }

    @Override
    public JpaRepository<Sell, Long> getDao() {
        return sellDao;
    }

    @Override
    public Class getController() {
        return SellController.class;
    }

    private void checkIfStockAvailableForCartItems(List<CartItemHolder> items) {
        items.forEach(item -> {
            List<CurrentStock> stocks = currentStockService
                    .getAllByStockItemAndStockStatus(item.getProduct(), StockStatus.AVAILABLE);
            stocks.removeIf(s -> s.getQuantity() <= 0);
            int availableQuantity = 0;
            for (CurrentStock s : stocks) {
                availableQuantity += s.getQuantity();
            }

            /* Throw Error If Available Stock is Less Than Line Item Quantity */
            if (availableQuantity < item.getQuantity()) {
                throw new IllegalArgumentException("\n\nRequired Quantity [" + item.getQuantity() + "] for Stock Item [" + item.getProduct().getName() + "], is Greater Than Available Quantity [" + availableQuantity + "]  !\n");
            }
        });
    }
}
