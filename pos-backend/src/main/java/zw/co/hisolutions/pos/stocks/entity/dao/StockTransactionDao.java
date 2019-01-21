package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional; 
import zw.co.hisolutions.pos.stocks.entity.StockRequest;
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionType;

/**
 *
 * @author dgumbo
 */
@Repository
public interface StockTransactionDao extends JpaRepository<StockTransaction, Long>  {
    public List<StockTransaction> findByStockTransactionType(StockTransactionType stockTransactionType);
  
    public List<StockTransaction> findByStockTransactionTypeOrderByIdDesc(StockTransactionType stockTransactionType);
 
//    public List<StockTransaction> findByUnitFromIdAndStockTransactionTypeAndTransactionTimeBetweenAndUserContainingOrderByIdDesc(long id, StockTransactionType stockTransactionType, Date startDate, Date endDate, String user);
// 
//    public List<StockTransaction> findByUnitToIdAndStockTransactionTypeAndTransactionTimeBetweenAndUserContainingOrderByIdDesc(long id, StockTransactionType stockTransactionType, Date startDate, Date endDate, String user);
//
//    public List<StockTransaction> findByUnitToIdAndStockTransactionTypeAndTransactionTimeBetweenOrderByIdDesc(long id, StockTransactionType stockTransactionType, Date startDate, Date endDate);
//
//    public List<StockTransaction> findByUnitFromIdAndStockTransactionTypeAndTransactionTimeBetweenOrderByIdDesc(long id, StockTransactionType stockTransactionType, Date startDate, Date endDate);

    @Modifying
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Query(value = "Update st Set st.stock_transaction_status = 'FAILED', st.tota_quantity = 0, st.stock_transaction_cost = 0 From stock_transaction st Left Join stock_transaction_line AS stl ON stl.stock_transaction_id = st.id Where stl.id Is Null and st.stock_transaction_status <>'FAILED'", nativeQuery = true)
    public int collectFailedTransactions();

//    public List<StockTransaction> findByStockTransactionTypeAndTransactionTimeBetweenAndUserContainingOrderByIdDesc(StockTransactionType stockTransactionType, Date startDate, Date endDate, String user);

    public List<StockTransaction> findByStockTransactionTypeAndTransactionTimeBetweenOrderByIdDesc(StockTransactionType stockTransactionType, Date startDate, Date endDate);

//    public List<StockTransaction> findTop1ByStockTransactionTypeAndCreatedByUserOrderByIdDesc(StockTransactionType stockTransactionType, String username);
 
    

}
