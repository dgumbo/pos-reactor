package zw.co.hisolutions.pos.masters.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.masters.entity.Currency; 

/**
 *
 * @author dgumbo
 */
@Repository
public interface CurrencyDao extends JpaRepository<Currency, Long> {

    public Currency getByName(String currencyName);
    
    @Modifying
    @Query("Update Currency cur Set cur.defaultCurrency = false")
    int removeDefaultForAllCurrencies();

}
