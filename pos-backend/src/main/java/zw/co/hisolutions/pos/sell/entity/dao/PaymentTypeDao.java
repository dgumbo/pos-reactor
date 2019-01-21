package zw.co.hisolutions.pos.sell.entity.dao;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;  
import zw.co.hisolutions.pos.masters.entity.PaymentType;

/**
 *
 * @author dgumbo
 */
@Repository
public interface PaymentTypeDao extends JpaRepository<PaymentType, Long>  { 
    
    @Modifying
    @Query("Update PaymentType pt Set pt.defaultPaymentType = false")
    int removeDefaultForAllPaymentTypes();
}
