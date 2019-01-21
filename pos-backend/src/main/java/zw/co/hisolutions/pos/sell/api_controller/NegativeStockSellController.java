package zw.co.hisolutions.pos.sell.api_controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.sell.entity.NegativeStockSell;
import zw.co.hisolutions.pos.sell.services.NegativeStockSellService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/sell/negative-stock-sell")
public class NegativeStockSellController extends BasicRestController<NegativeStockSell, Long> {

    private final NegativeStockSellService negativeStockSellService;

    public NegativeStockSellController(NegativeStockSellService negativeStockSellService) {
        this.negativeStockSellService = negativeStockSellService;
    } 
    
    @Override
    public GenericService getService() {
        return this.negativeStockSellService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

}
