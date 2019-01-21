package zw.co.hisolutions.pos.sell.services.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.sell.api_controller.SellController;
import zw.co.hisolutions.pos.sell.entity.StockOut;
import zw.co.hisolutions.pos.sell.entity.dao.StockOutDao;
import zw.co.hisolutions.pos.sell.services.StockOutService;

/**
 *
 * @author dgumbo
 */
@Service
public class StockOutServiceImpl implements StockOutService {

    private final StockOutDao stockOutDao;

    public StockOutServiceImpl(StockOutDao stockOutDao) {
        this.stockOutDao = stockOutDao;
    }

    @Override
    public Class getController() {
        return SellController.class;
    }

    @Override
    public JpaRepository<StockOut, Long> getDao() {
        return stockOutDao;
    }
}
