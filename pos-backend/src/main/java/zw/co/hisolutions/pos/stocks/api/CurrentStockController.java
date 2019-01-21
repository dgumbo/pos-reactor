package zw.co.hisolutions.pos.stocks.api;

import java.util.List;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; 
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.service.CurrentStockService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/stocks/current")
public class CurrentStockController  extends BasicRestController<CurrentStock, Long> { 
    private final CurrentStockService currentStockService; 

    public CurrentStockController(  CurrentStockService currentStockService ) {
        this.currentStockService=currentStockService;
    }     

    @GetMapping(value = "/available", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getAllAvailableStockItems () { 
        
        List<CurrentStock> allStock = currentStockService.getAllWithAvailableStock()               ;
          
        return new ResponseEntity<>(allStock, HttpStatus.OK);
    } 

    @GetMapping(value = "/available-stock-items", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getAllStockProducts () {
        List<StockItem> productList = currentStockService.getAllStock();
        
        
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping(value = "/set-product-current-stock", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> setProductCurrentStock (@RequestParam(name="stock-item-id", required = true) long stockItemId, @RequestParam(name="quantity", required = true) long quantity ) { 
        
        CurrentStock currentStock = currentStockService.setProductCurrentStock (stockItemId, quantity)               ;
          
        return new ResponseEntity<>(currentStock, HttpStatus.OK);
    }

    @GetMapping(value = "/set-product-current-stock", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getProductCurrentStock (@RequestParam(name="stock-item-id", required = true) long stockItemId, @RequestParam(name="quantity", required = true) long quantity ) { 
        
        CurrentStock currentStock = currentStockService.setProductCurrentStock (stockItemId, quantity)               ;
          
        return new ResponseEntity<>(currentStock, HttpStatus.OK);
    }
     
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(500000);
    }

    @Override
    public GenericService getService() {
       return currentStockService ;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }
}
