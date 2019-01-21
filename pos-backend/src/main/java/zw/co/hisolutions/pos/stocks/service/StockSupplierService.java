package zw.co.hisolutions.pos.stocks.service;

import java.util.List;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.emuns.SupplierClassification;
import zw.co.hisolutions.pos.stocks.entity.StockSupplier; 

public interface StockSupplierService extends GenericService<StockSupplier, Long> {
 
    StockSupplier findByName(String name);

    void deleteStockSupplier(StockSupplier product);

    List<StockSupplier> findBySupplierClassification(SupplierClassification supplier_classification);  

}
