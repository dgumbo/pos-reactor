package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.ProductCategory;

/**
 *
 * @author denzil
 */
@Repository
public interface StockItemDao extends JpaRepository<StockItem, Long>{ 

    public StockItem getByName(String name); 

    public List<StockItem> getByProductCategory(ProductCategory productCategory); 

    public List<StockItem> getByProductCategoryId(Long productCategoryId);
    
}
