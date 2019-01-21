package zw.co.hisolutions.pos.mis.typed.service.hqlGen;

import java.util.Date;
import javax.persistence.TypedQuery;
import zw.co.hisolutions.pos.mis.typed.entity.StockReOrderView;

/**
 *
 * @author dgumbo
 */
public interface StockReOrderHql {
    TypedQuery<StockReOrderView> createNQuery(String stockItemName, Date dateBackAgo, int daysBackAgo, long leadDays) ;
}
