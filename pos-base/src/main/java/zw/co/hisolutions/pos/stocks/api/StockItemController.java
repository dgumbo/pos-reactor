package zw.co.hisolutions.pos.stocks.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController; 
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.service.StockItemService; 

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/rest/products") 
public class StockItemController extends BasicRestController<StockItem, Long>    {

    private final StockItemService productService; 
 
    public StockItemController(StockItemService productService ) {
        this.productService = productService; 
    } 

    @GetMapping(value = "/getByProductCategoryID/{productCategoryId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getByProductCategoryID (@PathVariable Long productCategoryId) {
        List<StockItem> productList = productService.findProductByProductCategory(productCategoryId);
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping(value = "/getByName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getByName (@PathVariable String name) {
        StockItem product = productService.findByName(name);
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    

    @Override
    public GenericService getService() {
        return this.productService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass() ;
    } 
    

}
