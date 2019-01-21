package zw.co.hisolutions.pos.stocks.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.stocks.entity.StockRequest;

/**
 *
 * @author dgumbo
 */
@Repository
public interface StockRequestDao extends JpaRepository<StockRequest, Long>  {

//    public List<StockRequest> findByWorkFlowTypeAndRequestDateBetween(WorkFlowType workFlowType, Date requestDateStart, Date requestDateEnd);

//    public List<StockRequest> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetween(long id, WorkFlowType workFlowType, Date startDate, Date endDate);
//
//    public List<StockRequest> findByRequestUnitIdAndWorkFlowTypeAndRequestDateBetweenOrderByIdDesc(long id, WorkFlowType workFlowType, Date startDate, Date endDate);

}
