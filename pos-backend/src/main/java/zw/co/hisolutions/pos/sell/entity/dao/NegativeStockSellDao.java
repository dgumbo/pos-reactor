package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.sell.entity.NegativeStockSell;

/**
 *
 * @author dgumbo
 */
@Repository
public interface NegativeStockSellDao extends JpaRepository<NegativeStockSell, Long>{ 
    public List<NegativeStockSell> findByActiveStatusTrue(); 
    
}
