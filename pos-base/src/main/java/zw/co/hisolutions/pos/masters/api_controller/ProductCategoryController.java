package zw.co.hisolutions.pos.masters.api_controller;
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
import zw.co.hisolutions.pos.common.service.GenericService;  
import zw.co.hisolutions.pos.masters.service.ProductCategoryService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/masters/product-category")
public class ProductCategoryController extends BasicRestController<ProductCategory, Long>{

    private final  ProductCategoryService productTypeService;
    
    @Autowired
    public ProductCategoryController  (ProductCategoryService productTypeService) {
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
