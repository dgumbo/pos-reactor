//package zw.co.hisolutions.pos.xchange.service.impl;
//
//import java.math.BigDecimal;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;  
//import zw.co.hisolutions.pos.masters.entity.Currency;
//import zw.co.hisolutions.pos.stocks.entity.StockItem; 
//
///**
// *
// * @author dgumbo
// */
//@Service
//public class ExchangeRateServiceImpl implements ExchangeRatesService {
//
//    final GlobalExchangeRateDao globalExchangeRateDao;
//
//    public ExchangeRateServiceImpl(GlobalExchangeRateDao globalExchangeRateDao) {
//        this.globalExchangeRateDao = globalExchangeRateDao;
//    }
//
//    @Override
//    public GlobalExchangeRate update(long id, GlobalExchangeRate data) throws IllegalArgumentException {
//        GlobalExchangeRate exRate = ExchangeRatesService.super.update(id, data);
////To change body of generated methods, choose Tools | Template
//        updateStaticExchangeRates();
//        return exRate;
//    }
//
//    @Override
//    public void delete(Long id) throws IllegalArgumentException {
//        ExchangeRatesService.super.delete(id);
//        updateStaticExchangeRates();
////To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public GlobalExchangeRate save(GlobalExchangeRate data) throws IllegalArgumentException {
//        GlobalExchangeRate exRate = ExchangeRatesService.super.save(data);
//        updateStaticExchangeRates();
////To change body of generated methods, choose Tools | Templates.
//        return exRate;
//    }
//
//    @Override
//    public GlobalExchangeRate create(GlobalExchangeRate data) throws IllegalArgumentException {
//        GlobalExchangeRate exRate = ExchangeRatesService.super.create(data);
//        updateStaticExchangeRates();
////To change body of generated methods, choose Tools | Templates.
//        return exRate;
//    }
//
//    @Override
//    public GlobalExchangeRate findByCurrency(Currency currency) {
//        return globalExchangeRateDao.getByCurrency(currency);
//    }
//
//    @Override
//    public Class getController() {
//        return ExchangeRatesController.class;
//    }
//
//    @Override
//    public JpaRepository<GlobalExchangeRate, Long> getDao() {
//        return globalExchangeRateDao;
//    }
//
//    @Override
//    public void updateStaticExchangeRates() {
//        findAll()
//                .stream()
//                .filter(er -> er.isActiveStatus())
//                .forEach(exchangeRate -> {
//                    Double rate = exchangeRate.getBondRate() == BigDecimal.ZERO ? 0 : exchangeRate.getCurrencyRate().doubleValue() / exchangeRate.getBondRate().doubleValue();
//                    if (exchangeRate.getCurrency().getName() == "SWIPE_ECOCASH") {
//                        new StockItem().setEcocashRate(new BigDecimal(rate));
//                    } else if (exchangeRate.getCurrency().getName() == "USD") {
//                        new StockItem().setUSDRate(new BigDecimal(rate));
//                    }
//                });
//    }
//
//}
