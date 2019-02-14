package zw.co.hisolutions.pos.masters.service;
  
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;

public interface ProductCategoryService  extends GenericService<ProductCategory, Long>{
    public ProductCategory findByName(String name);   
}
