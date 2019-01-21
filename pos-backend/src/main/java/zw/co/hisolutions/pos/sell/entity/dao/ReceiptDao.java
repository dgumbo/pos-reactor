package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.sell.entity.Receipt;

/**
 *
 * @author dgumbo
 */
@Repository
public interface ReceiptDao extends JpaRepository<Receipt, Long>  {

    List<Receipt> findByActiveStatusTrue();

    Receipt findBySellId(Long id);

//    List<Receipt> findByUnitIdAndReceiptDateBetween(long id, Date startDate, Date endDate);

    List<Receipt> findBySellIdAndActiveStatusTrue(Long id);

 //   List<Receipt> findTop50ByUnitIdAndReceiptDateBetween(long id, Date startDate, Date endDate);

//    public int countByReceiptNumberStartingWith(String prefix);
}
