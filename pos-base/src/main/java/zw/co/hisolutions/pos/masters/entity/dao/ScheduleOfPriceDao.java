package zw.co.hisolutions.pos.masters.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.masters.entity.ScheduleOfPrice;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author denzil
 */
@Repository
public interface ScheduleOfPriceDao extends JpaRepository<ScheduleOfPrice, Long>{ 

    public List<ScheduleOfPrice> findByProductAndActiveStatus(StockItem product, boolean b);

    public ScheduleOfPrice findByProductAndCurrency(StockItem product, Currency currency);

    public List<ScheduleOfPrice> findByCurrency(Currency bondCurency);
 
    
}
