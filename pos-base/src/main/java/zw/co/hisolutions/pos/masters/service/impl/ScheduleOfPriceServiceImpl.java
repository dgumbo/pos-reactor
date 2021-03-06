package zw.co.hisolutions.pos.masters.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.backend.enums.ChargeType;
import zw.co.hisolutions.pos.masters.api_controller.ScheduleOfPriceController;
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.masters.service.ScheduleOfPriceService;
import zw.co.hisolutions.pos.stocks.entity.StockItem;
import zw.co.hisolutions.pos.masters.entity.dao.ScheduleOfPriceDao;
import zw.co.hisolutions.pos.masters.entity.ScheduleOfPrice;
import zw.co.hisolutions.pos.masters.service.CurrencyService;
import zw.co.hisolutions.pos.stocks.entity.dao.StockItemDao;

@Service
@Transactional
public class ScheduleOfPriceServiceImpl implements ScheduleOfPriceService {

    private final ScheduleOfPriceDao scheduleOfPriceDao;
    private final CurrencyService currencyService;
    private final StockItemDao stockItemDao;

    @Autowired
    public ScheduleOfPriceServiceImpl(ScheduleOfPriceDao scheduleOfChargeDao, StockItemDao stockItemDao, CurrencyService currencyService) {
        this.scheduleOfPriceDao = scheduleOfChargeDao;
        this.currencyService = currencyService;
        this.stockItemDao = stockItemDao;
    }

    @Override
    public JpaRepository<ScheduleOfPrice, Long> getDao() {
        return this.scheduleOfPriceDao;
    }

    @Override
    public Class getController() {
        return ScheduleOfPriceController.class;
    }

    @Override
    public StockItem updateBondPrice(StockItem product, BigDecimal newPrice, long currencyId) {
        List<ScheduleOfPrice> scheduleOfPriceList = scheduleOfPriceDao.findByProductAndActiveStatus(product, true);

        Currency currency = currencyService.find(currencyId);
//        System.out.println("\n\n\ncurrencyId : " + currencyId);
//        System.out.println("currency");
//        System.out.println(currency);

        ScheduleOfPrice bondScheduleOfPrice = scheduleOfPriceDao.findByProductAndCurrency(product, currency);

        if (bondScheduleOfPrice == null) {
            bondScheduleOfPrice = new ScheduleOfPrice();
            bondScheduleOfPrice.setCurrency(currency);
            bondScheduleOfPrice.setChargeType(ChargeType.PRIMARY);
            bondScheduleOfPrice.setProduct(product);
            scheduleOfPriceList.add(bondScheduleOfPrice);
        }

        bondScheduleOfPrice.setPrice(newPrice);

        try {
            bondScheduleOfPrice = save(bondScheduleOfPrice);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        product.setScheduleOfPrices(scheduleOfPriceList);
        return product;
    }

    @Override
    public void updateAllBondFromUsdPrice() {
        List<Currency> currencies = currencyService.findAll();

        Currency usdCurency = currencies
                .stream()
                .filter(c -> c.getName().trim().toLowerCase().equals("usd"))
                .findAny()
                .get();

        Currency bondCurency = currencies
                .stream()
                .filter(c -> c.getName().trim().toLowerCase().equals("bond"))
                .findAny()
                .get();

        if (usdCurency != null && bondCurency != null) {
            List<ScheduleOfPrice> bondPrices = scheduleOfPriceDao.findByCurrency(bondCurency);
            List<ScheduleOfPrice> usdPrices = scheduleOfPriceDao.findByCurrency(usdCurency);

            double usdBondRatio = usdCurency.getBondRate().doubleValue() / usdCurency.getCurrencyRate().doubleValue();

            List<StockItem> allProducts = stockItemDao.findAll();

            allProducts.forEach(prod -> {

                Optional<ScheduleOfPrice> usdPriceOpt = usdPrices
                        .stream()
                        .filter(bp -> bp.getProduct().getId() == prod.getId())
                        .findAny();
                ScheduleOfPrice usdPrice = usdPriceOpt.isPresent() ? usdPriceOpt.get() : null;

                if (usdPrice != null) {
                    ScheduleOfPrice bondPrice = bondPrices
                            .stream()
                            .filter(bp -> bp.getProduct().getId() == prod.getId())
                            .findAny()
                            .get();

                    if (bondPrice == null) {
                        bondPrice = new ScheduleOfPrice();
                        bondPrice.setCurrency(bondCurency);
                        bondPrice.setChargeType(ChargeType.PRIMARY);
                        bondPrice.setDescription("bondCurency");
                        bondPrice.setProduct(prod);
                    }

                    Double newPrice = usdPrice.getPrice().doubleValue() * usdBondRatio;

                    double doubleIntValue = new Double(newPrice.intValue());
                    double intValueDiff = newPrice - doubleIntValue;

                    double newDecValue;
                    if (intValueDiff <= 0.04) {
                        newDecValue = 0.00;
                    } else if (intValueDiff <= 0.11) {
                        newDecValue = 0.10;
                    } else if (intValueDiff <= 0.20) {
                        newDecValue = 0.20;
                    } else if (intValueDiff <= 0.26) {
                        newDecValue = 0.25;
                    } else if (intValueDiff <= 0.32) {
                        newDecValue = 0.30;
                    } else if (intValueDiff <= 0.42) {
                        newDecValue = 0.40;
                    } else if (intValueDiff <= 0.52) {
                        newDecValue = 0.50;
                    } else if (intValueDiff <= 0.62) {
                        newDecValue = 0.60;
                    } else if (intValueDiff <= 0.7) {
                        newDecValue = 0.70;
                    } else if (intValueDiff <= 0.76) {
                        newDecValue = 0.75;
                    } else if (intValueDiff <= 0.82) {
                        newDecValue = 0.80;
                    } else if (intValueDiff <= 0.92) {
                        newDecValue = 0.90;
                    } else {
                        newDecValue = 1.00;
                    } 
                    
                    double newAdjustedPrice = doubleIntValue + newDecValue;
                    
                    bondPrice.setPrice(new BigDecimal(newAdjustedPrice));
                    save(bondPrice);

                    System.out.println("\nproduct : " + prod);
                    System.out.println("usdBondRatio : " + usdBondRatio);
                    System.out.println("usdprice : " + usdPrice.getPrice());
                    System.out.println("bondPrice : " + bondPrice.getPrice()); 
                }
            });
        }
        System.out.println("\n");
    }

    @Override
    public void updateAllUsdFromBondPrice() {
        List<Currency> currencies = currencyService.findAll();

        Currency bondCurency = currencies
                .stream()
                .filter(c -> c.getName().trim().toLowerCase().equals("bond"))
                .findAny()
                .get();

        Currency usdCurency = currencies
                .stream()
                .filter(c -> c.getName().trim().toLowerCase().equals("usd"))
                .findAny()
                .get();

        if (bondCurency != null && usdCurency != null) {
            List<ScheduleOfPrice> usdPrices = scheduleOfPriceDao.findByCurrency(usdCurency);
            List<ScheduleOfPrice> bondPrices = scheduleOfPriceDao.findByCurrency(bondCurency);

            double bondUsdRatio = usdCurency.getCurrencyRate().doubleValue() / usdCurency.getBondRate().doubleValue();

            List<StockItem> allProducts = stockItemDao.findAll();

            allProducts.forEach(prod -> {

                Optional<ScheduleOfPrice> bondPriceOpt = bondPrices
                        .stream()
                        .filter(bp -> bp.getProduct().getId() == prod.getId())
                        .findAny();
                ScheduleOfPrice bondPrice = bondPriceOpt.isPresent() ? bondPriceOpt.get() : null;

                if (bondPrice != null) {
                    Optional<ScheduleOfPrice> usdPriceOpt = usdPrices
                            .stream()
                            .filter(bp -> bp.getProduct().getId() == prod.getId())
                            .findAny();
                    ScheduleOfPrice usdPrice = usdPriceOpt.isPresent() ? usdPriceOpt.get() : null;

                    if (usdPrice == null) {
                        usdPrice = new ScheduleOfPrice();
                        usdPrice.setCurrency(usdCurency);
                        usdPrice.setChargeType(ChargeType.RATED);
                        usdPrice.setDescription("usdCurency");
                        usdPrice.setProduct(prod);
                    }

                    double newPrice = bondPrice.getPrice().doubleValue() * bondUsdRatio;
                    usdPrice.setPrice(new BigDecimal(newPrice));
                    usdPrice = save(usdPrice);

                    System.out.println("\nproduct : " + prod);
                    System.out.println("usdprice : " + usdPrice.getPrice());
                    System.out.println("bondPrice : " + bondPrice.getPrice());
                    System.out.println("bondUsdRatio : " + bondUsdRatio);
                }
            });
        }
        System.out.println("\n");
    }
}
