package zw.co.hisolutions.pos.sell.api_controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.sell.entity.StockOut;
import zw.co.hisolutions.pos.sell.services.StockOutService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/sell/stock-out")
public class StockOutController extends BasicRestController<StockOut, Long> {

    private final StockOutService stockOutService;

    public StockOutController(StockOutService stockOutService) {
        this.stockOutService = stockOutService;
    } 
    
    @Override
    public GenericService getService() {
        return this.stockOutService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

}
