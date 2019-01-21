package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.sell.entity.StockItemNegativeStockSell; 

/**
 *
 * @author dgumbo
 */
@Repository
public interface StockItemNegativeStockSellDao extends JpaRepository<StockItemNegativeStockSell, Long>{ 
    public List<StockItemNegativeStockSell> findByActiveStatusTrue(); 
    
}
