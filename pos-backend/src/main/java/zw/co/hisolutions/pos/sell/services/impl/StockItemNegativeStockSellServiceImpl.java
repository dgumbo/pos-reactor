package zw.co.hisolutions.pos.sell.services.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.sell.api_controller.SellController;
import zw.co.hisolutions.pos.sell.entity.StockItemNegativeStockSell;
import zw.co.hisolutions.pos.sell.entity.dao.StockItemNegativeStockSellDao;
import zw.co.hisolutions.pos.sell.services.StockItemNegativeStockSellService;

/**
 *
 * @author dgumbo
 */
@Service
public class StockItemNegativeStockSellServiceImpl implements StockItemNegativeStockSellService {

    private final StockItemNegativeStockSellDao stockItemNegativeStockSellDao; 

    public StockItemNegativeStockSellServiceImpl(StockItemNegativeStockSellDao stockItemNegativeStockSellDao) {
        this.stockItemNegativeStockSellDao = stockItemNegativeStockSellDao; 
    } 

    @Override
    public Class getController() {
        return SellController.class;
    }

    @Override
    public JpaRepository<StockItemNegativeStockSell, Long> getDao() {
        return stockItemNegativeStockSellDao;
    }
 }
