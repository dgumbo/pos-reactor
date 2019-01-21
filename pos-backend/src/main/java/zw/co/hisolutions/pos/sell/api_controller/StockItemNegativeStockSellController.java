package zw.co.hisolutions.pos.sell.api_controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.sell.entity.StockItemNegativeStockSell;
import zw.co.hisolutions.pos.sell.services.StockItemNegativeStockSellService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/sell/stock-item-negative-stock-sell")
public class StockItemNegativeStockSellController extends BasicRestController<StockItemNegativeStockSell, Long> {

    private final StockItemNegativeStockSellService stockItemNegativeStockSellService;

    public StockItemNegativeStockSellController(StockItemNegativeStockSellService stockItemNegativeStockSellService) {
        this.stockItemNegativeStockSellService = stockItemNegativeStockSellService;
    } 
    
    @Override
    public GenericService getService() {
        return this.stockItemNegativeStockSellService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

}
