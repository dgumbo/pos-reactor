package zw.co.hisolutions.pos.masters.service.impl;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.masters.api_controller.CurrencyController;
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.masters.entity.dao.CurrencyDao;
import zw.co.hisolutions.pos.masters.service.CurrencyService; 
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author dgumbo
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDao currencyDao;

    public CurrencyServiceImpl(CurrencyDao negativeStockSellDao) {
        this.currencyDao = negativeStockSellDao;
    }

    @Override
    public Currency update(long id, Currency data) throws IllegalArgumentException {
        checkDefaultCurrency(data) ;
        Currency exRate = CurrencyService.super.update(id, data);
//To change body of generated methods, choose Tools | Template
        updateStaticExchangeRates();
        return exRate;
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        CurrencyService.super.delete(id);
        updateStaticExchangeRates();
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Currency save(Currency data) throws IllegalArgumentException {
        checkDefaultCurrency(data) ;
        Currency exRate = CurrencyService.super.save(data);
        updateStaticExchangeRates();
        
        //To change body of generated methods, choose Tools | Templates.
        return exRate;
    }

    @Override
    public Currency create(Currency data) throws IllegalArgumentException {
        checkDefaultCurrency(data) ;
        Currency exRate = CurrencyService.super.create(data);
        updateStaticExchangeRates();
        
        //To change body of generated methods, choose Tools | Templates.
        return exRate;
    }
    
    private void checkDefaultCurrency(Currency currency){
        if(currency.isDefaultCurrency()){
            currencyDao.removeDefaultForAllCurrencies() ;
        }
    }

    @Override
    public Class getController() {
        return CurrencyController.class;
    }

    @Override
    public JpaRepository<Currency, Long> getDao() {
        return currencyDao;
    }

    @Override
    public void updateStaticExchangeRates() {
        findAll()
                .stream()
                .filter(er -> er.isActiveStatus())
                .forEach(currency -> {
                    Double rate = currency.getBondRate() == BigDecimal.ZERO ? 0 : currency.getCurrencyRate().doubleValue() / currency.getBondRate().doubleValue();
                    if (currency.getName().toLowerCase().contains("ecocash")) {
                        new StockItem().setEcocashRate(new BigDecimal(rate));
                    } else if (currency.getName().equalsIgnoreCase("USD")) {
                        new StockItem().setUSDRate(new BigDecimal(rate));
                    }
                });
    }

    @Override
    public Currency getByName(String currencyName) {
        return currencyDao.getByName(currencyName);
    }
}
