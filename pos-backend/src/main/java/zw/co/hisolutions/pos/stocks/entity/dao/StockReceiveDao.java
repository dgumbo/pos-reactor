package zw.co.hisolutions.pos.stocks.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.stocks.entity.StockReceive;

/**
 *
 * @author dgumbo
 */ 
@Repository
public interface StockReceiveDao extends JpaRepository<StockReceive, Long>/*, NumeratedDao*/ {

//    public List<StockReceive> findByWorkFlowTypeAndRequestDateBetween(WorkFlowType workFlowType, Date requestDateStart, Date requestDateEnd);
//
//    public List<StockReceive> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetween(long id, WorkFlowType workFlowType, Date startDate, Date endDate);
//
//    public List<StockReceive> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetweenOrderByIdDesc(long id, WorkFlowType workFlowType, Date startDate, Date endDate);

}
