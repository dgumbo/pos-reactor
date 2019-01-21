package zw.co.hisolutions.pos.sell.services;

import java.util.List;
import zw.co.hisolutions.pos.backend.entity.CartHolder; 
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.sell.entity.Sell;
import zw.co.hisolutions.pos.sell.entity.SellItem;

/**
 *
 * @author dgumbo
 */
public interface SellService  extends GenericService<Sell, Long>  {

    List<Sell> getAllBySearch(Sell sell); 

    List<SellItem> getBySellId(long id);

    void cancelSell(Sell sell, String reason); 

    Sell saveCart(CartHolder cartHolder); 
}
