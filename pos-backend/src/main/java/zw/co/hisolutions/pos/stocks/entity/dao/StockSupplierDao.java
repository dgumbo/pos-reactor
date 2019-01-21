package zw.co.hisolutions.pos.stocks.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.stocks.emuns.SupplierClassification;
import zw.co.hisolutions.pos.stocks.entity.StockSupplier;

/**
 *
 * @author dgumbo
 */

@Repository
public interface StockSupplierDao  extends JpaRepository<StockSupplier, Long>{

    StockSupplier getByName (String supplier_name) ;
    
    List<StockSupplier> findByClassification(SupplierClassification classification) ;
}
