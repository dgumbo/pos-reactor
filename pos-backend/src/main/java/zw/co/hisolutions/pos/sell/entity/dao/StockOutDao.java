package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.sell.entity.StockOut;

/**
 *
 * @author dgumbo
 */
@Repository
public interface StockOutDao extends JpaRepository<StockOut, Long>{ 
    public List<StockOut> findByActiveStatusTrue(); 
    
}
