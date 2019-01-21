package zw.co.hisolutions.pos.masters.service;
 
import java.math.BigDecimal;
import zw.co.hisolutions.pos.masters.entity.ScheduleOfPrice;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

public interface ScheduleOfPriceService extends GenericService<ScheduleOfPrice, Long>{

    StockItem updateBondPrice(StockItem product, BigDecimal newPrice, long currencyId);
//    public ScheduleOfPrice findByName(String name);   

    public void updateAllBondFromUsdPrice();

    public void updateAllUsdFromBondPrice();
}
