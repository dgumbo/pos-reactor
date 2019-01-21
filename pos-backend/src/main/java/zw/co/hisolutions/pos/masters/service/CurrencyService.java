package zw.co.hisolutions.pos.masters.service;

import zw.co.hisolutions.pos.common.service.GenericService;  
import zw.co.hisolutions.pos.masters.entity.Currency;

/**
 *
 * @author dgumbo
 */
public interface CurrencyService  extends GenericService<Currency, Long>  {
 
    public void updateStaticExchangeRates();

    public Currency getByName(String currencyName);
}
