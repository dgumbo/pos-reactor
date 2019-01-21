package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.sell.entity.Sell;

/**
 *
 * @author dgumbo
 */
@Repository
public interface SellDao extends   JpaRepository<Sell, Long> {

    public List<Sell> findTop50ByActiveStatusTrueOrderByIdDesc(); 
 
//    public Sell findByNumber(String sellNumber);
    
 }
