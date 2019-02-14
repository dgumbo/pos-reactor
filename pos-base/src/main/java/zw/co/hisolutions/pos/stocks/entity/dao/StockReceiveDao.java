package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.stocks.entity.StockReceive;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionStatus;

/**
 *
 * @author dgumbo
 */ 
@Repository
public interface StockReceiveDao extends JpaRepository<StockReceive, Long>/*, NumeratedDao*/ {

    public List<StockReceive> findByReceiveStatus(StockTransactionStatus stockTransactionStatus);

//    public List<StockReceive> findByWorkFlowTypeAndRequestDateBetween(WorkFlowType workFlowType, Date requestDateStart, Date requestDateEnd);
//
//    public List<StockReceive> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetween(long id, WorkFlowType workFlowType, Date startDate, Date endDate);
//
//    public List<StockReceive> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetweenOrderByIdDesc(long id, WorkFlowType workFlowType, Date startDate, Date endDate);

    public List<StockReceive> getByReceiveStatus(StockTransactionStatus stockTransactionStatus);

}
