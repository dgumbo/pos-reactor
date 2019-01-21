package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;  
import zw.co.hisolutions.pos.stocks.entity.StockTake;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionStatus;

/**
 *
 * @author dgumbo
 */ 
@Repository
public interface StockTakeDao extends JpaRepository<StockTake, Long>  { 
    List<StockTake> getByStockTakeStatus(StockTransactionStatus transactionStatus);
 
}
