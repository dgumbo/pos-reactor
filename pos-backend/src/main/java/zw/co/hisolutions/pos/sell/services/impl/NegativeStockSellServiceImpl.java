package zw.co.hisolutions.pos.sell.services.impl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.sell.api_controller.SellController;
import zw.co.hisolutions.pos.sell.entity.NegativeStockSell;
import zw.co.hisolutions.pos.sell.entity.dao.NegativeStockSellDao;
import zw.co.hisolutions.pos.sell.services.NegativeStockSellService;

/**
 *
 * @author dgumbo
 */
@Service
public class NegativeStockSellServiceImpl implements NegativeStockSellService {

    private final NegativeStockSellDao negativeStockSellDao; 

    public NegativeStockSellServiceImpl(NegativeStockSellDao negativeStockSellDao) {
        this.negativeStockSellDao = negativeStockSellDao; 
    } 

    @Override
    public NegativeStockSell save(NegativeStockSell negativeStockSell) throws IllegalArgumentException {
        List<NegativeStockSell> checkList = this.findAll();
        NegativeStockSell toSave = null;
        if (checkList.size() >= 1) {
            toSave = checkList.get(0);
            toSave.setAllowAll(negativeStockSell.isAllowAll());
        } else {
            toSave = negativeStockSell;
        }

        return NegativeStockSellService.super.save(toSave);
    }

    @Override
    public NegativeStockSell create(NegativeStockSell negativeStockSell) throws IllegalArgumentException {
        return save(negativeStockSell);
    }

    @Override
    public Class getController() {
        return SellController.class;
    }

    @Override
    public JpaRepository<NegativeStockSell, Long> getDao() {
        return negativeStockSellDao;
    }
 }
