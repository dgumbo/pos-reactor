package zw.co.hisolutions.pos.backend.rest.views.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.masters.service.ProductCategoryService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
import zw.co.hisolutions.pos.stocks.service.StockItemService; 

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/views/rest")
public class ViewsController { 
    private final StockItemService productService;
    private final ProductCategoryService productCategoryService;

    @Autowired
    public ViewsController(StockItemService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }  

    @GetMapping(value = "/getProductCategoryByName/{name}")
    public ResponseEntity<?> getProductCategoryByName (@PathVariable String name) {
        ProductCategory productCategory = productCategoryService.findByName(name); 
        return new ResponseEntity<>(productCategory, HttpStatus.OK);
    } 

    @GetMapping(value = "/getAllProductCategory")
    public ResponseEntity<?> getAllServiceCategories () {
        List<ProductCategory> serviceCategories = productCategoryService.findAll(); 
        return new ResponseEntity<>(serviceCategories, HttpStatus.OK);
    } 

    @GetMapping(value = "/getProductByName/{name}")
    public ResponseEntity<?> getProductByName (@PathVariable String name) {
        StockItem product = productService.findByName(name); 
        return new ResponseEntity<>(product, HttpStatus.OK);
    } 

    @GetMapping(value = "/getAllProduct")
    public ResponseEntity<?> getAllProduct () {
        List<StockItem> products = productService.findAll(); 
        return new ResponseEntity<>(products, HttpStatus.OK);
    } 

    @GetMapping(value = "/getProductByProductCategoryId/{id}")
    public ResponseEntity<?> getAllProductByProductCategoryId (@PathVariable Long id) {
        List<StockItem> products = productService.findProductByProductCategory(id); 
        return new ResponseEntity<>(products, HttpStatus.OK);
    } 

    @GetMapping(value = "/getProductById/{id}")
    public ResponseEntity<?> getProductById (@PathVariable Long id) {
        StockItem product = productService.find(id); 
        return new ResponseEntity<>(product, HttpStatus.OK);
    } 

    @GetMapping(value = "/getProductCategoryById/{id}")
    public ResponseEntity<?> getProductCategoryById (@PathVariable Long id) {
        ProductCategory productCategory = productCategoryService.find(id); 
        return new ResponseEntity<>(productCategory, HttpStatus.OK);
    } 
}
