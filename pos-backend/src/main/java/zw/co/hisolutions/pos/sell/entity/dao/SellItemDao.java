package zw.co.hisolutions.pos.sell.entity.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.hisolutions.pos.sell.entity.SellItem;

/**
 *
 * @author gdenzilg
 */
@Repository
public interface SellItemDao extends JpaRepository<SellItem, Long> {

    public List<SellItem> findBySellId(Long id);

    public List<SellItem> findBySellIdAndActiveStatusTrue(Long id);

}
