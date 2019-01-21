package zw.co.hisolutions.pos.stocks.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;  
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;

/**
 *
 * @author denzil
 */
@Repository
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Long>{ 

    public ProductCategory getByName(String name); 
    
}
