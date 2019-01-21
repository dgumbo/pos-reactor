package zw.co.hisolutions.pos.stocks.service.impl;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.stocks.api.StockSupplierController;
import zw.co.hisolutions.pos.stocks.emuns.SupplierClassification;
import zw.co.hisolutions.pos.stocks.entity.StockSupplier; 
import zw.co.hisolutions.pos.stocks.entity.dao.StockSupplierDao;
import zw.co.hisolutions.pos.stocks.service.StockSupplierService; 
 
@Service
@Transactional
public class StockSupplierServiceImpl implements StockSupplierService { 
    private final StockSupplierDao stockSupplierDao;  

 
    public StockSupplierServiceImpl(StockSupplierDao stockSupplierDao  ) {
        this.stockSupplierDao = stockSupplierDao; 
    } 

    @Override
    public StockSupplier findByName(String name) {
        return stockSupplierDao.getByName(name);  
    }
 
    @Override
    public void deleteStockSupplier(StockSupplier stockSupplier) {
        stockSupplierDao.delete(stockSupplier);
    }
 
    @Override
    public JpaRepository<StockSupplier, Long> getDao() {
        return stockSupplierDao;
    }

    @Override
    public Class getController() {
        return StockSupplierController.class;
    }   

    @Override
    public List<StockSupplier> findBySupplierClassification(SupplierClassification supplier_classification) {
        return stockSupplierDao.findByClassification(supplier_classification);
    } 
}
