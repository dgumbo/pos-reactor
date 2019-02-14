package zw.co.hisolutions.pos.stocks.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.masters.service.ScheduleOfPriceService;
import zw.co.hisolutions.pos.stocks.api.StockItemController;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
import zw.co.hisolutions.pos.stocks.entity.StockItem; 
import zw.co.hisolutions.pos.stocks.entity.StockStatus;
import zw.co.hisolutions.pos.stocks.service.StockItemService;
import zw.co.hisolutions.pos.stocks.entity.dao.StockItemDao;

@Service
@Transactional
public class StockItemServiceImpl implements StockItemService {

    private final StockItemDao productDao;
    private final ScheduleOfPriceService scheduleOfPriceService;

    public StockItemServiceImpl(StockItemDao productDao, ScheduleOfPriceService scheduleOfPriceService) {
        this.productDao = productDao;
        this.scheduleOfPriceService = scheduleOfPriceService;
    }

    @Override
    public StockItem findByName(String name) {
        StockItem p = productDao.getByName(name);
//        if (p != null) setAvailableStock(p)   ;
        return p;
    }

    @Override
    public void deleteProduct(StockItem product) {
        productDao.delete(product);
    }

    @Override
    public JpaRepository<StockItem, Long> getDao() {
        return productDao;
    }

    @Override
    public Class getController() {
        return StockItemController.class;
    }

    @Override
    public StockItem save(StockItem stockItem) throws IllegalArgumentException {
        return create(stockItem);
    }

    @Override
    public StockItem create(StockItem stockItem) throws IllegalArgumentException {
        CurrentStock cs = new CurrentStock();
        cs.setBatchNumber("BN");
        cs.setQuantity(0);
        cs.setStockItem(stockItem);
        cs.setConsignment(false);
        cs.setCashOnly(false);
        cs.setStockStatus(StockStatus.AVAILABLE);

        CurrentStock ccs = cs.cloneSplit();
        ccs.setConsignment(true);

        List<CurrentStock> currentStockList = Arrays.asList(cs, ccs);

        stockItem.setCurrentStockList(currentStockList);

        StockItem rStockItem = StockItemService.super.create(stockItem);

        if (!stockItem.getScheduleOfPrices().isEmpty()) {
            stockItem.getScheduleOfPrices().forEach(sop -> {
                sop.setProduct(rStockItem);
                sop = scheduleOfPriceService.create(sop);
            });
        }

        return rStockItem;
    }

    @Override
    public List<StockItem> findProductByProductCategory(Long productCategoryId
    ) {
        List<StockItem> productList = productDao.getByProductCategoryId(productCategoryId);
//        productList.stream().forEach(p ->  setAvailableStock(p)  ) ;
        return productList;
    }
 

    @Override
    public StockItem updateLastReceiptCostRate(StockItem product, BigDecimal unitCost
    ) {
        if (product != null && product.getId() > 0) {
            product.setLastReceiptCostRate(unitCost);
            return save(product);
        }

        return null;
    }

    @Override
    public List<StockItem> findProductByProductCategory(ProductCategory productCategory) {
        
        List<StockItem> productList = productDao.getByProductCategory(productCategory);
//        productList.stream().forEach(p ->  setAvailableStock(p)  ) ;
        return productList;
    }
}
