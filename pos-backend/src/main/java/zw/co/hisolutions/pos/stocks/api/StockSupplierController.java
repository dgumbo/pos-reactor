package zw.co.hisolutions.pos.stocks.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockSupplier;
import zw.co.hisolutions.pos.stocks.service.StockSupplierService;

/**
 *
 * @author dgumbo
 */ 
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/rest/stock-supplier")
public class StockSupplierController extends BasicRestController<StockSupplier , Long>{

    private final  StockSupplierService productTypeService;
     
    public StockSupplierController  (StockSupplierService productTypeService) {
        this.productTypeService = productTypeService;        
    }

    @Override
    public GenericService getService() {
        return this.productTypeService;
    }

    @Override
    public Class getControllerClass() {
       return this.getClass();
    }

}