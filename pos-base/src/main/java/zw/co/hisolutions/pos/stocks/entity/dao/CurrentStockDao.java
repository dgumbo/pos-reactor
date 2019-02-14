package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.stocks.entity.StockStatus;

/**
 *
 * @author denzil
 */
@Repository
public interface CurrentStockDao extends JpaRepository<CurrentStock, Long> {

    CurrentStock getAllByStockItemAndBatchNumberAndConsignmentAndStockStatus(StockItem stockItem, String batch_number, boolean consignment_status, StockStatus stock_status);
//    CurrentStock getAllByStockItemIdAndBatchNumberAndConsignmentAndStockStatus(long stockItem_id, String batch_number, boolean consignment_status, StockStatus stock_status);

    List<CurrentStock> getAllByStockItem(StockItem stockItem);
//    List<CurrentStock> getAllByStockItemId(long id);

    List<CurrentStock> getAllByStockItemAndBatchNumber(StockItem stockItem, String batch_number);
//    List<CurrentStock> getAllByStockItemIdAndBatchNumber(long id, String batch_number);

    List<CurrentStock> getAllByActiveStatus(boolean active_status);

    List<CurrentStock> getAllByActiveStatusAndStockStatusAndQuantityGreaterThan(boolean active_status, StockStatus stockStatus, long min_quantity);

    public List<CurrentStock> getAllByStockItemAndStockStatus(StockItem stockItem, StockStatus stockStatus);

    public List<CurrentStock> getAllByStockItemAndBatchNumberAndStockStatus(StockItem stockItem, String batchNumber, StockStatus stockStatus);

}
