package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.sell.entity.ReceiptItem; 

/**
 *
 * @author gdenzilg
 */
@Repository
public interface ReceiptItemDao extends JpaRepository<ReceiptItem, Long> {

    public List<ReceiptItem> findByReceiptId(Long id);

    public List<ReceiptItem> findByReceiptIdAndActiveStatusTrue(Long id);

}
