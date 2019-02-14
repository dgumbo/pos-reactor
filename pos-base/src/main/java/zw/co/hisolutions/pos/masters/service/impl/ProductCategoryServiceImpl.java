package zw.co.hisolutions.pos.masters.service.impl;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Service;   
import zw.co.hisolutions.pos.masters.api_controller.ProductCategoryController;
import zw.co.hisolutions.pos.masters.service.ProductCategoryService;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
import zw.co.hisolutions.pos.stocks.entity.dao.ProductCategoryDao; 
 
@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryDao productTypeDao;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryDao productTypeDao ){ 
        this.productTypeDao = productTypeDao;
    }
    
     @Override
    public ProductCategory findByName(String name) {
        return productTypeDao.getByName(name);
    } 

    @Override
    public JpaRepository<ProductCategory, Long> getDao() {
       return this.productTypeDao;
    }

    @Override
    public Class getController() {
       return ProductCategoryController.class;
    }
}
